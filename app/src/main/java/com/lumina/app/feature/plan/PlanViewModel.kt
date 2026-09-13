package com.lumina.app.feature.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.local.DeadlineEntity
import com.lumina.app.data.model.FreeSlot
import com.lumina.app.data.model.Priority
import com.lumina.app.data.repository.LuminaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class PlanUiState(
    val deadlines: List<DeadlineEntity> = emptyList(),
    val focusSlots: List<FreeSlot> = emptyList(),
    val loading: Boolean = true
)

class PlanViewModel(private val repository: LuminaRepository) : ViewModel() {

    val uiState: StateFlow<PlanUiState> = combine(
        repository.deadlines,
        repository.timeline
    ) { deadlines, _ ->
        PlanUiState(
            deadlines = deadlines,
            focusSlots = repository.findFreeSlots(minimumMinutes = 25),
            loading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlanUiState())

    fun addDeadline(title: String, course: String, daysFromNow: Int, priority: Priority) {
        viewModelScope.launch {
            val dueAt = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, daysFromNow)
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
            }.timeInMillis
            repository.addDeadline(
                DeadlineEntity(
                    title = title, course = course, dueAt = dueAt,
                    progress = 0, priority = priority
                )
            )
        }
    }

    fun deleteDeadline(deadline: DeadlineEntity) {
        viewModelScope.launch { repository.deleteDeadline(deadline) }
    }

    /** "Add to my day" turns a detected gap into a real, blocked-out focus session. */
    fun scheduleFocusSlot(slot: FreeSlot, title: String) {
        viewModelScope.launch {
            repository.addTimelineEvent(
                com.lumina.app.data.local.TimelineEventEntity(
                    startMinutes = slot.startMinutes,
                    endMinutes = minOf(slot.endMinutes, slot.startMinutes + 60),
                    title = title,
                    meta = "Focus session",
                    kind = com.lumina.app.data.model.EventKind.FLEXIBLE
                )
            )
        }
    }
}
