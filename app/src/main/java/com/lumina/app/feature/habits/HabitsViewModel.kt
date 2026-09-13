package com.lumina.app.feature.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.local.HabitEntity
import com.lumina.app.data.repository.LuminaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class HabitsUiState(
    val habits: List<HabitEntity> = emptyList(),
    val doneCount: Int = 0,
    val totalCount: Int = 0,
    val streakDays: Int = 0,
    val weeklyConsistency: Int = 0,
    val loading: Boolean = true
) {
    val progress: Float get() = if (totalCount == 0) 0f else doneCount.toFloat() / totalCount
}

class HabitsViewModel(private val repository: LuminaRepository) : ViewModel() {

    val uiState: StateFlow<HabitsUiState> = combine(
        repository.habits,
        repository.moodEntries
    ) { habits, moodEntries ->
        HabitsUiState(
            habits = habits,
            doneCount = habits.count { it.isDone },
            totalCount = habits.size,
            // A logged mood is the daily "I opened the app" signal, so it doubles as the streak.
            streakDays = moodEntries.map { it.recordedAt / TimeUnit.DAYS.toMillis(1) }
                .distinct().size.coerceAtMost(60),
            weeklyConsistency = if (habits.isEmpty()) 0
            else (habits.count { it.isDone } * 100 / habits.size),
            loading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HabitsUiState())

    val bestStreak: StateFlow<Int> = repository.moodEntries
        .map { it.size.coerceAtMost(60) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun toggleHabit(id: Long) {
        viewModelScope.launch { repository.advanceHabit(id) }
    }

    fun addHabit(title: String, detail: String, iconKey: String, targetCount: Int) {
        viewModelScope.launch {
            repository.addHabit(
                HabitEntity(
                    title = title,
                    detail = detail,
                    iconKey = iconKey,
                    scheduledMinutes = null,
                    targetCount = targetCount,
                    sortOrder = 100
                )
            )
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch { repository.deleteHabit(habit) }
    }
}
