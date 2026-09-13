package com.lumina.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lumina.app.data.model.EventKind
import com.lumina.app.data.model.Mood
import com.lumina.app.data.model.Priority

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val displayName: String,
    val role: String,
    val goals: List<String>,
    val interests: List<String>,
    val preferredFreeTime: String,
    val wakeUpMinutes: Int,
    val sleepMinutes: Int,
    val breakfastMinutes: Int,
    val lunchMinutes: Int,
    val dinnerMinutes: Int,
    val exerciseMinutes: Int,
    val exerciseDays: List<String>,
    val smartSuggestions: Boolean,
    val onboardingComplete: Boolean
)

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val iconKey: String,
    /** Free-text detail, e.g. "Push day" or "Before 11:00 PM". */
    val detail: String,
    /** Minutes from midnight the habit is scheduled for, or null if any time. */
    val scheduledMinutes: Int?,
    val targetCount: Int = 1,
    val currentCount: Int = 0,
    val isDone: Boolean = false,
    /** Epoch millis the habit was completed, or null. */
    val completedAt: Long? = null,
    val sortOrder: Int = 0
)

@Entity(tableName = "deadlines")
data class DeadlineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val course: String,
    val dueAt: Long,
    val progress: Int,
    val priority: Priority
)

@Entity(tableName = "timeline_events")
data class TimelineEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startMinutes: Int,
    val endMinutes: Int,
    val title: String,
    val meta: String,
    val kind: EventKind
)

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordedAt: Long,
    val mood: Mood,
    val causes: List<String>
)

@Entity(tableName = "health_reminders")
data class HealthReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val iconKey: String,
    val lastDoneAt: Long?,
    val nextDueAt: Long,
    /** 0 means one-time. */
    val repeatMonths: Int,
    val note: String,
    val notifyEnabled: Boolean = true
)

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startedAt: Long,
    val durationMinutes: Int,
    val taskTitle: String,
    val completed: Boolean
)
