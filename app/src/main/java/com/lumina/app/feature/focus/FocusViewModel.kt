package com.lumina.app.feature.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.repository.LuminaRepository
import com.lumina.app.ui.isToday
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FocusUiState(
    val taskTitle: String = "Focus session",
    val totalSeconds: Int = 25 * 60,
    val elapsedSeconds: Int = 0,
    val running: Boolean = true,
    val finished: Boolean = false
) {
    val remainingSeconds: Int get() = (totalSeconds - elapsedSeconds).coerceAtLeast(0)
    val progress: Float get() = (elapsedSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f)
}

/**
 * Smart Focus Mode: a 25-minute block during which Lumina holds its own notifications.
 * Completed sessions are written to the database so Progress can report real focus time.
 */
class FocusViewModel(private val repository: LuminaRepository) : ViewModel() {

    private val _state = MutableStateFlow(FocusUiState())
    val state: StateFlow<FocusUiState> = _state.asStateFlow()

    private var ticker: Job? = null

    val sessionsToday: StateFlow<Int> = repository.focusSessions
        .map { sessions -> sessions.count { isToday(it.startedAt) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val minutesToday: StateFlow<Int> = repository.focusSessions
        .map { sessions -> sessions.filter { isToday(it.startedAt) }.sumOf { it.durationMinutes } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun startFor(taskTitle: String) {
        _state.value = FocusUiState(taskTitle = taskTitle)
        ticker?.cancel()
        ticker = viewModelScope.launch {
            while (true) {
                delay(1_000)
                val current = _state.value
                if (!current.running || current.finished) continue
                val elapsed = current.elapsedSeconds + 1
                if (elapsed >= current.totalSeconds) {
                    _state.value = current.copy(
                        elapsedSeconds = current.totalSeconds,
                        running = false,
                        finished = true
                    )
                    repository.logFocusSession(current.taskTitle, current.totalSeconds / 60, true)
                } else {
                    _state.value = current.copy(elapsedSeconds = elapsed)
                }
            }
        }
    }

    fun togglePause() {
        _state.value = _state.value.copy(running = !_state.value.running)
    }

    /** Ending early still counts the minutes actually spent — partial effort is real effort. */
    fun endEarly() {
        val current = _state.value
        ticker?.cancel()
        val minutes = current.elapsedSeconds / 60
        if (minutes >= 1) {
            viewModelScope.launch {
                repository.logFocusSession(current.taskTitle, minutes, completed = false)
            }
        }
    }

    override fun onCleared() {
        ticker?.cancel()
        super.onCleared()
    }
}
