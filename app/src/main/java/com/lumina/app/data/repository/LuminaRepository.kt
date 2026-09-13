package com.lumina.app.data.repository

import com.lumina.app.data.local.DeadlineEntity
import com.lumina.app.data.local.FocusSessionEntity
import com.lumina.app.data.local.HabitEntity
import com.lumina.app.data.local.HealthReminderEntity
import com.lumina.app.data.local.LuminaDatabase
import com.lumina.app.data.local.MoodEntryEntity
import com.lumina.app.data.local.TimelineEventEntity
import com.lumina.app.data.local.UserProfileEntity
import com.lumina.app.data.model.EventKind
import com.lumina.app.data.model.FreeSlot
import com.lumina.app.data.model.Mood
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for the app.
 * Everything the UI reads is a Room Flow.
 */
class LuminaRepository(private val db: LuminaDatabase) {

    // ---- profile --------------------------------------------------------------

    val profile: Flow<UserProfileEntity?> = db.profileDao().observe()

    suspend fun getProfile(): UserProfileEntity? =
        db.profileDao().get()

    suspend fun saveProfile(profile: UserProfileEntity) =
        db.profileDao().upsert(profile)
//    change

    suspend fun updateProfile(
        transform: (UserProfileEntity) -> UserProfileEntity
    ) {
        db.profileDao().get()?.let {
            db.profileDao().upsert(transform(it))
        }
    }

    suspend fun completeOnboarding() =
        updateProfile {
            it.copy(onboardingComplete = true)
        }

    // ---- habits ---------------------------------------------------------------

    val habits: Flow<List<HabitEntity>> =
        db.habitDao().observeAll()

    suspend fun addHabit(habit: HabitEntity) =
        db.habitDao().insert(habit)

    suspend fun deleteHabit(habit: HabitEntity) =
        db.habitDao().delete(habit)

    suspend fun advanceHabit(id: Long) {

        val habit = db.habitDao().byId(id) ?: return

        val updated = if (habit.targetCount > 1) {

            val next =
                (habit.currentCount + 1)
                    .coerceAtMost(habit.targetCount)

            habit.copy(
                currentCount = next,
                isDone = next >= habit.targetCount,
                completedAt =
                    if (next >= habit.targetCount)
                        System.currentTimeMillis()
                    else
                        null
            )

        } else {

            val done = !habit.isDone

            habit.copy(
                isDone = done,
                currentCount = if (done) 1 else 0,
                completedAt =
                    if (done)
                        System.currentTimeMillis()
                    else
                        null
            )
        }

        db.habitDao().update(updated)
    }

    suspend fun resetHabitsForNewDay() =
        db.habitDao().resetForNewDay()

    // ---- deadlines ------------------------------------------------------------

    val deadlines: Flow<List<DeadlineEntity>> =
        db.deadlineDao().observeAll()

    suspend fun addDeadline(deadline: DeadlineEntity) =
        db.deadlineDao().insert(deadline)

    suspend fun deleteDeadline(deadline: DeadlineEntity) =
        db.deadlineDao().delete(deadline)

    suspend fun bumpDeadlineProgress(
        id: Long,
        delta: Int
    ) {

        val deadline =
            db.deadlineDao().byId(id) ?: return

        db.deadlineDao().update(
            deadline.copy(
                progress =
                    (deadline.progress + delta)
                        .coerceIn(0, 100)
            )
        )
    }

    // ---- timeline -------------------------------------------------------------

    /**
     * Today's Timeline is read directly from Room Database.
     */
    val timeline: Flow<List<TimelineEventEntity>> =
        db.timelineDao().observeAll()

    /**
     * User-added timeline events are saved here.
     */
    suspend fun addTimelineEvent(
        event: TimelineEventEntity
    ) =
        db.timelineDao().insert(event)

    /**
     * Drag / move event.
     * Returns conflicting event if another event already exists
     * in the requested time slot.
     */
    suspend fun moveEvent(
        id: Long,
        newStartMinutes: Int
    ): TimelineEventEntity? {

        val event =
            db.timelineDao().byId(id) ?: return null

        val duration =
            event.endMinutes - event.startMinutes

        val newEnd =
            newStartMinutes + duration

        val conflict =
            db.timelineDao().findConflict(
                newStartMinutes,
                newEnd,
                id
            )

        if (conflict != null) {
            return conflict
        }

        db.timelineDao().update(
            event.copy(
                startMinutes = newStartMinutes,
                endMinutes = newEnd
            )
        )

        return null
    }

    /**
     * Force move an event.
     */
    suspend fun forceMoveEvent(
        id: Long,
        newStartMinutes: Int
    ) {

        val event =
            db.timelineDao().byId(id) ?: return

        val duration =
            event.endMinutes - event.startMinutes

        db.timelineDao().update(
            event.copy(
                startMinutes = newStartMinutes,
                endMinutes =
                    newStartMinutes + duration
            )
        )
    }

    /**
     * Finds free gaps between timeline events.
     */
    suspend fun findFreeSlots(
        minimumMinutes: Int = 15
    ): List<FreeSlot> {

        val profile =
            db.profileDao().get()
                ?: return emptyList()

        val events =
            db.timelineDao().getAll()

        val slots =
            mutableListOf<FreeSlot>()

        var cursor =
            profile.wakeUpMinutes

        for (
        event in events.sortedBy {
            it.startMinutes
        }
        ) {

            if (event.kind == EventKind.FREE) {
                continue
            }

            if (
                event.startMinutes - cursor
                >= minimumMinutes
            ) {

                slots += FreeSlot(
                    cursor,
                    event.startMinutes
                )
            }

            cursor =
                maxOf(
                    cursor,
                    event.endMinutes
                )
        }

        if (
            profile.sleepMinutes - cursor
            >= minimumMinutes
        ) {

            slots += FreeSlot(
                cursor,
                profile.sleepMinutes
            )
        }

        return slots
    }

    // ---- mood -----------------------------------------------------------------

    val moodEntries:
            Flow<List<MoodEntryEntity>> =
        db.moodDao().observeAll()

    val latestMood:
            Flow<MoodEntryEntity?> =
        db.moodDao().observeLatest()

    suspend fun logMood(
        mood: Mood,
        causes: List<String>
    ) =
        db.moodDao().insert(
            MoodEntryEntity(
                recordedAt =
                    System.currentTimeMillis(),
                mood = mood,
                causes = causes
            )
        )

    // ---- health ---------------------------------------------------------------

    val healthReminders:
            Flow<List<HealthReminderEntity>> =
        db.healthReminderDao().observeAll()

    suspend fun addHealthReminder(
        reminder: HealthReminderEntity
    ) =
        db.healthReminderDao()
            .insert(reminder)

    suspend fun updateHealthReminder(
        reminder: HealthReminderEntity
    ) =
        db.healthReminderDao()
            .update(reminder)

    suspend fun deleteHealthReminder(
        reminder: HealthReminderEntity
    ) =
        db.healthReminderDao()
            .delete(reminder)

    /**
     * Mark health reminder as done.
     */
    suspend fun markHealthReminderDone(
        reminder: HealthReminderEntity
    ) {

        val now =
            System.currentTimeMillis()

        val next =
            if (reminder.repeatMonths > 0) {

                java.util.Calendar
                    .getInstance()
                    .apply {
                        timeInMillis = now

                        add(
                            java.util.Calendar.MONTH,
                            reminder.repeatMonths
                        )
                    }
                    .timeInMillis

            } else {

                reminder.nextDueAt
            }

        db.healthReminderDao().update(
            reminder.copy(
                lastDoneAt = now,
                nextDueAt = next
            )
        )
    }

    // ---- focus ----------------------------------------------------------------

    val focusSessions:
            Flow<List<FocusSessionEntity>> =
        db.focusSessionDao().observeAll()

    suspend fun logFocusSession(
        taskTitle: String,
        minutes: Int,
        completed: Boolean
    ) =
        db.focusSessionDao().insert(
            FocusSessionEntity(
                startedAt =
                    System.currentTimeMillis(),
                durationMinutes = minutes,
                taskTitle = taskTitle,
                completed = completed
            )
        )

    // ---- first run ------------------------------------------------------------

    suspend fun seedIfEmpty() {

        if (
            db.profileDao().get() == null
        ) {
            db.profileDao().upsert(
                SeedData.profile()
            )
        }

        if (
            db.habitDao().count() == 0
        ) {
            db.habitDao().insertAll(
                SeedData.habits()
            )
        }

        if (
            db.deadlineDao().count() == 0
        ) {
            db.deadlineDao().insertAll(
                SeedData.deadlines()
            )
        }

        /*
         * Timeline seed data REMOVED.
         *
         * Before:
         *
         * if (db.timelineDao().count() == 0)
         *     db.timelineDao().insertAll(SeedData.timeline())
         *
         * Now only user-added events appear
         * in Today's Timeline.
         */

        if (
            db.moodDao().count() == 0
        ) {
            db.moodDao().insertAll(
                SeedData.moodHistory()
            )
        }

        if (
            db.healthReminderDao().count() == 0
        ) {
            db.healthReminderDao().insertAll(
                SeedData.healthReminders()
            )
        }

        if (
            db.focusSessionDao().count() == 0
        ) {
            db.focusSessionDao().insertAll(
                SeedData.focusSessions()
            )
        }
    }
}