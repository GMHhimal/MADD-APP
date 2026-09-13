package com.lumina.app.data.repository

import com.lumina.app.data.local.DeadlineEntity
import com.lumina.app.data.local.FocusSessionEntity
import com.lumina.app.data.local.HabitEntity
import com.lumina.app.data.local.HealthReminderEntity
import com.lumina.app.data.local.MoodEntryEntity
import com.lumina.app.data.local.TimelineEventEntity
import com.lumina.app.data.local.UserProfileEntity
import com.lumina.app.data.model.EventKind
import com.lumina.app.data.model.Mood
import com.lumina.app.data.model.Priority
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * First-run content. Mirrors the sample data used throughout the Figma prototype so the
 * app looks like the design the moment it is installed, without needing a backend.
 */
object SeedData {

    private fun daysFromNow(days: Int, hour: Int = 9, minute: Int = 0): Long =
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, days)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    fun profile() = UserProfileEntity(
        displayName = "Himal",
        role = "University Student",
        goals = listOf("Manage Deadlines", "Find Free Time"),
        interests = listOf("Netflix & Movies", "Gaming", "Music", "Gym", "Walking"),
        preferredFreeTime = "20–30 min",
        wakeUpMinutes = 6 * 60 + 30,
        sleepMinutes = 23 * 60,
        breakfastMinutes = 7 * 60 + 30,
        lunchMinutes = 12 * 60 + 30,
        dinnerMinutes = 19 * 60 + 30,
        exerciseMinutes = 18 * 60,
        exerciseDays = listOf("Mon", "Wed", "Fri"),
        smartSuggestions = true,
        onboardingComplete = false
    )

    fun habits() = listOf(
        HabitEntity(title = "Drink Water", iconKey = "water", detail = "8 glasses",
            scheduledMinutes = null, targetCount = 8, currentCount = 6, sortOrder = 0),
        HabitEntity(title = "Gym Session", iconKey = "gym", detail = "Push day",
            scheduledMinutes = 18 * 60, sortOrder = 1),
        HabitEntity(title = "Breakfast", iconKey = "meal", detail = "Protected meal break",
            scheduledMinutes = 7 * 60 + 30, isDone = true,
            completedAt = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(6), sortOrder = 2),
        HabitEntity(title = "Morning Stretch", iconKey = "stretch", detail = "5 minutes",
            scheduledMinutes = 7 * 60 + 10, isDone = true,
            completedAt = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(7), sortOrder = 3),
        HabitEntity(title = "Lunch", iconKey = "meal", detail = "Protected meal break",
            scheduledMinutes = 12 * 60 + 30, isDone = true,
            completedAt = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2), sortOrder = 4),
        HabitEntity(title = "Meditation", iconKey = "meditation", detail = "10 minutes",
            scheduledMinutes = null, isDone = true,
            completedAt = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3), sortOrder = 5),
        HabitEntity(title = "Evening Walk", iconKey = "walk", detail = "20 minutes",
            scheduledMinutes = 18 * 60 + 40, isDone = true,
            completedAt = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(40), sortOrder = 6)
    )

    fun deadlines() = listOf(
        DeadlineEntity(title = "SE Assignment", course = "Software Engineering · Group 07",
            dueAt = daysFromNow(1, 23, 59), progress = 65, priority = Priority.HIGH),
        DeadlineEntity(title = "Research Proposal", course = "Final Year Research Project",
            dueAt = daysFromNow(13, 17, 0), progress = 30, priority = Priority.MEDIUM)
    )

    fun timeline() = listOf(
        TimelineEventEntity(startMinutes = 8 * 60, endMinutes = 12 * 60,
            title = "University Lecture", meta = "Software Engineering · Hall B", kind = EventKind.FIXED),
        TimelineEventEntity(startMinutes = 12 * 60 + 30, endMinutes = 13 * 60,
            title = "Lunch", meta = "Protected meal break · 30 min", kind = EventKind.FIXED),
        TimelineEventEntity(startMinutes = 14 * 60, endMinutes = 15 * 60,
            title = "Work on SE Assignment", meta = "Focus session · 60 min", kind = EventKind.FLEXIBLE),
        TimelineEventEntity(startMinutes = 16 * 60, endMinutes = 16 * 60 + 45,
            title = "Free Time", meta = "45 minutes · nothing scheduled", kind = EventKind.FREE),
        TimelineEventEntity(startMinutes = 18 * 60, endMinutes = 19 * 60,
            title = "Gym", meta = "Push day · Mon / Wed / Fri", kind = EventKind.FLEXIBLE),
        TimelineEventEntity(startMinutes = 19 * 60 + 30, endMinutes = 20 * 60,
            title = "Dinner", meta = "Protected meal break", kind = EventKind.FIXED),
        TimelineEventEntity(startMinutes = 22 * 60 + 30, endMinutes = 23 * 60,
            title = "Wind Down", meta = "Screens off · sleep by 11:00 PM", kind = EventKind.OPTIONAL)
    )

    /** 30 days of history so the Progress screen has a real trend to draw. */
    fun moodHistory(): List<MoodEntryEntity> {
        val pattern = listOf(
            Mood.GOOD, Mood.GREAT, Mood.OKAY, Mood.GOOD, Mood.GOOD, Mood.GREAT, Mood.LOW,
            Mood.GOOD, Mood.GOOD, Mood.OKAY, Mood.GREAT, Mood.GOOD, Mood.LOW, Mood.GOOD,
            Mood.GREAT, Mood.GOOD, Mood.OKAY, Mood.GOOD, Mood.STRESSED, Mood.GOOD, Mood.GREAT,
            Mood.GOOD, Mood.OKAY, Mood.GOOD, Mood.LOW, Mood.GREAT, Mood.GOOD, Mood.GOOD,
            Mood.OKAY, Mood.GOOD
        )
        val now = System.currentTimeMillis()
        return pattern.mapIndexed { index, mood ->
            MoodEntryEntity(
                recordedAt = now - TimeUnit.DAYS.toMillis((pattern.size - 1 - index).toLong()),
                mood = mood,
                causes = if (mood == Mood.LOW || mood == Mood.STRESSED) listOf("Study", "Sleep") else emptyList()
            )
        }
    }

    fun healthReminders() = listOf(
        HealthReminderEntity(title = "Full Blood Count", iconKey = "blood",
            lastDoneAt = daysFromNow(-340), nextDueAt = daysFromNow(25),
            repeatMonths = 12, note = "Fasting required — book a morning slot"),
        HealthReminderEntity(title = "Cholesterol Test", iconKey = "cholesterol",
            lastDoneAt = daysFromNow(-164), nextDueAt = daysFromNow(20),
            repeatMonths = 6, note = ""),
        HealthReminderEntity(title = "Dental Checkup", iconKey = "dental",
            lastDoneAt = daysFromNow(-111), nextDueAt = daysFromNow(73),
            repeatMonths = 6, note = ""),
        HealthReminderEntity(title = "Eye Checkup", iconKey = "eye",
            lastDoneAt = daysFromNow(-218), nextDueAt = daysFromNow(147),
            repeatMonths = 12, note = "")
    )

    fun focusSessions(): List<FocusSessionEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            FocusSessionEntity(startedAt = now - TimeUnit.HOURS.toMillis(4), durationMinutes = 25,
                taskTitle = "SE Assignment", completed = true),
            FocusSessionEntity(startedAt = now - TimeUnit.HOURS.toMillis(2), durationMinutes = 23,
                taskTitle = "SE Assignment", completed = true),
            FocusSessionEntity(startedAt = now - TimeUnit.DAYS.toMillis(1), durationMinutes = 50,
                taskTitle = "Research Proposal", completed = true)
        )
    }
}
