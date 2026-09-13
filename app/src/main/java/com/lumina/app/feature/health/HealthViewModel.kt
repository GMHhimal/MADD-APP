package com.lumina.app.feature.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.local.HealthReminderEntity
import com.lumina.app.data.repository.LuminaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class HealthUiState(
    val reminders: List<HealthReminderEntity> = emptyList(),
    val nextUp: HealthReminderEntity? = null,
    val loading: Boolean = true
)

class HealthViewModel(private val repository: LuminaRepository) : ViewModel() {

    val uiState: StateFlow<HealthUiState> = repository.healthReminders
        .map { reminders ->
            HealthUiState(
                reminders = reminders.drop(if (reminders.isEmpty()) 0 else 1),
                nextUp = reminders.firstOrNull(),
                loading = false
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HealthUiState())

    fun addReminder(
        title: String,
        iconKey: String,
        daysFromNow: Int,
        repeatMonths: Int,
        note: String,
        notify: Boolean
    ) {
        viewModelScope.launch {
            val nextDue = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, daysFromNow)
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
            }.timeInMillis
            repository.addHealthReminder(
                HealthReminderEntity(
                    title = title,
                    iconKey = iconKey,
                    lastDoneAt = null,
                    nextDueAt = nextDue,
                    repeatMonths = repeatMonths,
                    note = note,
                    notifyEnabled = notify
                )
            )
        }
    }

    fun markBooked(reminder: HealthReminderEntity) {
        viewModelScope.launch { repository.markHealthReminderDone(reminder) }
    }

    fun delete(reminder: HealthReminderEntity) {
        viewModelScope.launch { repository.deleteHealthReminder(reminder) }
    }
}
