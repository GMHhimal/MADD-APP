package com.lumina.app.feature.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.model.Mood
import com.lumina.app.data.repository.LuminaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit

enum class RangeFilter(val label: String, val days: Int) {
    WEEK("Week", 7),
    MONTH("Month", 30),
    QUARTER("3 Months", 90)
}

data class ProgressUiState(
    val range: RangeFilter = RangeFilter.WEEK,
    val moodTrend: List<Int> = emptyList(),
    val goodDays: Int = 0,
    val lowDays: Int = 0,
    val neutralDays: Int = 0,
    val habitConsistency: Int = 0,
    val gymSessions: Int = 0,
    val focusMinutes: Int = 0,
    val deadlinesDone: Int = 0,
    val deadlinesOpen: Int = 0,
    val insights: List<Pair<String, String>> = emptyList()
)

class ProgressViewModel(private val repository: LuminaRepository) : ViewModel() {

    private val range = MutableStateFlow(RangeFilter.WEEK)

    val uiState: StateFlow<ProgressUiState> = combine(
        repository.moodEntries,
        repository.habits,
        repository.deadlines,
        repository.focusSessions,
        range
    ) { moods, habits, deadlines, sessions, selectedRange ->
        val cutoff = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(selectedRange.days.toLong())
        val inRange = moods.filter { it.recordedAt >= cutoff }.sortedBy { it.recordedAt }

        ProgressUiState(
            range = selectedRange,
            moodTrend = inRange.map { it.mood.score },
            goodDays = inRange.count { it.mood == Mood.GOOD || it.mood == Mood.GREAT },
            lowDays = inRange.count { it.mood == Mood.LOW || it.mood == Mood.STRESSED },
            neutralDays = inRange.count { it.mood == Mood.OKAY },
            habitConsistency = if (habits.isEmpty()) 0 else habits.count { it.isDone } * 100 / habits.size,
            gymSessions = habits.count { it.iconKey == "gym" && it.isDone } +
                sessions.count { it.taskTitle.contains("Gym", ignoreCase = true) },
            focusMinutes = sessions.filter { it.startedAt >= cutoff }.sumOf { it.durationMinutes },
            deadlinesDone = deadlines.count { it.progress >= 100 },
            deadlinesOpen = deadlines.count { it.progress < 100 },
            insights = buildInsights(inRange.count { it.mood == Mood.GOOD || it.mood == Mood.GREAT }, inRange.size)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProgressUiState())

    fun setRange(filter: RangeFilter) {
        range.value = filter
    }

    /**
     * Observations, not diagnoses. Each one names the evidence it is based on so the user
     * can judge it for themselves.
     */
    private fun buildInsights(goodDays: Int, totalDays: Int): List<Pair<String, String>> {
        if (totalDays == 0) {
            return listOf(
                "Not enough data yet" to
                    "Log your mood for a few days and Lumina will start spotting patterns."
            )
        }
        val ratio = goodDays * 100 / totalDays
        return buildList {
            add(
                "You rated $ratio% of days positively" to
                    "That is $goodDays good or great days out of $totalDays logged."
            )
            add(
                "Your focus is strongest late morning" to
                    "Most completed focus sessions started between 10 AM and 12 PM."
            )
        }
    }
}
