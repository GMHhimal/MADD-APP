package com.lumina.app.feature.breathing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** The 4-2-6 cycle from the design: inhale 4s, hold 2s, exhale 6s. */
enum class BreathPhase(val label: String, val seconds: Int) {
    INHALE("Breathe in", 4),
    HOLD("Hold", 2),
    EXHALE("Breathe out", 6);

    fun next(): BreathPhase = when (this) {
        INHALE -> HOLD
        HOLD -> EXHALE
        EXHALE -> INHALE
    }
}

data class BreathingUiState(
    val phase: BreathPhase = BreathPhase.INHALE,
    val secondsLeftInPhase: Int = BreathPhase.INHALE.seconds,
    val elapsedSeconds: Int = 0,
    val totalSeconds: Int = 5 * 60,
    val running: Boolean = true,
    val finished: Boolean = false
) {
    val progress: Float get() = (elapsedSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f)
}

class BreathingViewModel : ViewModel() {

    private val _state = MutableStateFlow(BreathingUiState())
    val state: StateFlow<BreathingUiState> = _state.asStateFlow()

    private var ticker: Job? = null

    init { start() }

    private fun start() {
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
                    continue
                }

                val remainingInPhase = current.secondsLeftInPhase - 1
                _state.value = if (remainingInPhase <= 0) {
                    val nextPhase = current.phase.next()
                    current.copy(
                        phase = nextPhase,
                        secondsLeftInPhase = nextPhase.seconds,
                        elapsedSeconds = elapsed
                    )
                } else {
                    current.copy(secondsLeftInPhase = remainingInPhase, elapsedSeconds = elapsed)
                }
            }
        }
    }

    fun togglePause() {
        _state.value = _state.value.copy(running = !_state.value.running)
    }

    fun restart() {
        _state.value = BreathingUiState()
    }

    override fun onCleared() {
        ticker?.cancel()
        super.onCleared()
    }
}
