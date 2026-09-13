package com.lumina.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lumina.app.data.local.DeadlineEntity
import com.lumina.app.data.local.HabitEntity
import com.lumina.app.data.local.MoodEntryEntity
import com.lumina.app.data.local.TimelineEventEntity
import com.lumina.app.data.local.UserProfileEntity
import com.lumina.app.data.model.EventKind
import com.lumina.app.data.model.Mood
import com.lumina.app.data.repository.LuminaRepository
import com.lumina.app.data.repository.SmartEngine
import com.lumina.app.ui.greetingForNow
import com.lumina.app.ui.isToday
import com.lumina.app.ui.todayLabel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val loading: Boolean = true,
    val name: String = "",
    val greeting: String = greetingForNow(),
    val dateLabel: String = todayLabel(),
    val balanceLabel: String = "",
    val mood: Mood? = null,
    val habitsDone: Int = 0,
    val habitsTotal: Int = 0,
    val focusMinutes: Int = 0,
    val freeMinutes: Int = 0,
    val timeline: List<TimelineEventEntity> = emptyList(),
    val insight: SmartEngine.Suggestion? = null,
    val stressWarning: SmartEngine.Suggestion? = null
)

/** Bundles the four "plan" flows so the final combine stays readable. */
private data class PlanSnapshot(
    val profile: UserProfileEntity?,
    val habits: List<HabitEntity>,
    val timeline: List<TimelineEventEntity>,
    val deadlines: List<DeadlineEntity>
)

class HomeViewModel(private val repository: LuminaRepository) : ViewModel() {

    private val planSnapshot: Flow<PlanSnapshot> = combine(
        repository.profile,
        repository.habits,
        repository.timeline,
        repository.deadlines
    ) { profile, habits, timeline, deadlines ->
        PlanSnapshot(profile, habits, timeline, deadlines)
    }

    val uiState: StateFlow<HomeUiState> = combine(
        planSnapshot,
        repository.latestMood,
        repository.focusSessions
    ) { plan, latestMood: MoodEntryEntity?, sessions ->
        val freeSlots = repository.findFreeSlots(minimumMinutes = 20)
        val freeMinutes = freeSlots.sumOf { it.durationMinutes }
        val scheduledMinutes = plan.timeline
            .filter { it.kind != EventKind.FREE }
            .sumOf { it.endMinutes - it.startMinutes }

        HomeUiState(
            loading = plan.profile == null,
            name = plan.profile?.displayName.orEmpty(),
            balanceLabel = SmartEngine.balanceLabel(
                habitsDone = plan.habits.count { it.isDone },
                habitsTotal = plan.habits.size,
                freeMinutes = freeMinutes
            ),
            mood = latestMood?.takeIf { isToday(it.recordedAt) }?.mood,
            habitsDone = plan.habits.count { it.isDone },
            habitsTotal = plan.habits.size,
            focusMinutes = sessions.filter { isToday(it.startedAt) }.sumOf { it.durationMinutes },
            freeMinutes = freeMinutes,
            timeline = plan.timeline,
            insight = SmartEngine.dailyInsight(freeSlots, plan.deadlines, plan.habits),
            stressWarning = SmartEngine.deadlineStressWarning(plan.deadlines, scheduledMinutes)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    /**
     * Accepting "Optimise my day" moves the first flexible block into the next free slot
     * big enough to hold it.
     */
    fun optimiseDay() {
        viewModelScope.launch {
            val events = repository.timeline.first()
            val movable = events.firstOrNull { it.kind == EventKind.FLEXIBLE } ?: return@launch
            val needed = movable.endMinutes - movable.startMinutes
            val slot = repository.findFreeSlots(minimumMinutes = needed).firstOrNull() ?: return@launch
            repository.forceMoveEvent(movable.id, slot.startMinutes)
        }
    }

    /** Drag-to-reschedule from the Home timeline. Returns the clash, or null on success. */
    suspend fun moveEvent(id: Long, newStartMinutes: Int): TimelineEventEntity? =
        repository.moveEvent(id, newStartMinutes)

    fun forceMove(id: Long, newStartMinutes: Int) {
        viewModelScope.launch { repository.forceMoveEvent(id, newStartMinutes) }
    }
}
