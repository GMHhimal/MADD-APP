package com.lumina.app.feature.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.model.Mood
import com.lumina.app.data.repository.LuminaRepository
import com.lumina.app.ui.isToday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MoodUiState(
    val selectedMood: Mood? = null,
    val selectedCauses: List<String> = emptyList(),
    val loggedToday: Boolean = false,
    val streakDays: Int = 0
)

class MoodViewModel(private val repository: LuminaRepository) : ViewModel() {

    private val draftMood = MutableStateFlow<Mood?>(null)
    private val draftCauses = MutableStateFlow<List<String>>(emptyList())

    val uiState: StateFlow<MoodUiState> = combine(
        repository.moodEntries,
        draftMood,
        draftCauses
    ) { entries, mood, causes ->
        val today = entries.firstOrNull { isToday(it.recordedAt) }
        MoodUiState(
            selectedMood = mood ?: today?.mood,
            selectedCauses = if (causes.isEmpty()) today?.causes.orEmpty() else causes,
            loggedToday = today != null,
            streakDays = consecutiveDays(entries.map { it.recordedAt })
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MoodUiState())

    fun selectMood(mood: Mood) {
        draftMood.value = mood
        viewModelScope.launch { repository.logMood(mood, draftCauses.value) }
    }

    fun toggleCause(cause: String) {
        val current = draftCauses.value
        draftCauses.value = if (cause in current) current - cause else current + cause
    }

    /** How many days in a row end with a logged mood, counting back from today. */
    private fun consecutiveDays(timestamps: List<Long>): Int {
        if (timestamps.isEmpty()) return 0
        val dayMillis = 24L * 60 * 60 * 1000
        val days = timestamps.map { it / dayMillis }.distinct().sortedDescending()
        val today = System.currentTimeMillis() / dayMillis
        if (days.first() < today - 1) return 0
        var streak = 1
        for (index in 1 until days.size) {
            if (days[index] == days[index - 1] - 1) streak++ else break
        }
        return streak
    }
}
