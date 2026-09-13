package com.lumina.app.feature.freetime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.model.FreeSlot
import com.lumina.app.data.repository.LuminaRepository
import com.lumina.app.data.repository.SmartEngine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FreeTimeUiState(
    val slot: FreeSlot? = null,
    val activities: List<SmartEngine.Activity> = emptyList(),
    val loading: Boolean = true
)

class FreeTimeViewModel(private val repository: LuminaRepository) : ViewModel() {

    private val shuffleSeed = MutableStateFlow(0)
    val surprise = shuffleSeed.asStateFlow()

    val uiState: StateFlow<FreeTimeUiState> = combine(
        repository.profile,
        repository.deadlines,
        shuffleSeed
    ) { profile, deadlines, seed ->
        val slots = repository.findFreeSlots(minimumMinutes = 15)
        val slot = slots.firstOrNull { it.endMinutes > com.lumina.app.ui.nowMinutes() }
            ?: slots.firstOrNull()
        val activities = SmartEngine.freeTimeActivities(
            profile = profile,
            slotMinutes = slot?.durationMinutes ?: 30,
            deadlines = deadlines
        )
        FreeTimeUiState(
            slot = slot,
            // "Surprise me" reshuffles without changing what is on offer.
            activities = if (seed == 0) activities else activities.shuffled(java.util.Random(seed.toLong())),
            loading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FreeTimeUiState())

    fun surpriseMe() {
        shuffleSeed.value = (1..9_999).random()
    }

    fun logActivityAsFocus(title: String, minutes: Int) {
        viewModelScope.launch { repository.logFocusSession(title, minutes, completed = true) }
    }
}
