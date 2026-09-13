package com.lumina.app.data.repository

import com.lumina.app.data.local.DeadlineEntity
import com.lumina.app.data.model.FreeSlot
import com.lumina.app.data.local.HabitEntity
import com.lumina.app.data.local.UserProfileEntity
import com.lumina.app.ui.daysUntil
import com.lumina.app.ui.formatDuration
import com.lumina.app.ui.formatMinutes
import com.lumina.app.ui.nowMinutes

/**
 * The rules behind everything Lumina "notices".
 *
 * Kept as pure functions with no Android or database dependencies so the behaviour is
 * easy to reason about, easy to change, and unit-testable without an emulator.
 */
object SmartEngine {

    data class Suggestion(
        val title: String,
        val body: String,
        val primaryAction: String?,
        val secondaryAction: String?
    )

    data class Activity(
        val title: String,
        val subtitle: String,
        val minutes: Int,
        val actionLabel: String,
        val iconKey: String,
        val recommended: Boolean = false
    )

    /** How loaded the day looks, in the app's own voice. */
    fun balanceLabel(habitsDone: Int, habitsTotal: Int, freeMinutes: Int): String = when {
        habitsTotal == 0 -> "Nothing tracked yet"
        freeMinutes < 30 -> "Today is tightly packed"
        habitsDone.toFloat() / habitsTotal >= 0.6f -> "Your day looks balanced"
        freeMinutes > 180 -> "Plenty of room today"
        else -> "There's still time to catch up"
    }

    /**
     * The Home insight card.
     *
     * Priority order: an urgent deadline beats a free-time nudge, which beats a habit
     * nudge. Only one thing is surfaced at a time — a dashboard of competing advice is
     * noise, not help.
     */
    fun dailyInsight(
        freeSlots: List<FreeSlot>,
        deadlines: List<DeadlineEntity>,
        habits: List<HabitEntity>
    ): Suggestion {
        val upcomingSlot = freeSlots.firstOrNull { it.endMinutes > nowMinutes() } ?: freeSlots.firstOrNull()
        val slotMinutes = upcomingSlot?.durationMinutes ?: 0
        val urgent = deadlines
            .filter { daysUntil(it.dueAt) <= 1 && it.progress < 100 }
            .minByOrNull { it.dueAt }

        if (urgent != null && upcomingSlot != null && slotMinutes >= 25) {
            val focus = 25
            return Suggestion(
                title = "Lumina Suggestion",
                body = "You have ${formatDuration(slotMinutes)} free at " +
                    "${formatMinutes(upcomingSlot.startMinutes)}. ${urgent.title} is due " +
                    "${if (daysUntil(urgent.dueAt) == 0) "today" else "tomorrow"}, so spending " +
                    "$focus minutes on it still leaves you ${formatDuration(slotMinutes - focus)} to relax.",
                primaryAction = "Use this plan",
                secondaryAction = "Change"
            )
        }

        if (urgent != null) {
            return Suggestion(
                title = "Lumina Suggestion",
                body = "${urgent.title} is due soon and sits at ${urgent.progress}%. " +
                    "Even a short focus session today keeps it from becoming a late night.",
                primaryAction = "Start focus",
                secondaryAction = "Not now"
            )
        }

        if (upcomingSlot != null && slotMinutes >= 20) {
            return Suggestion(
                title = "Lumina Suggestion",
                body = "You have ${formatDuration(slotMinutes)} free at " +
                    "${formatMinutes(upcomingSlot.startMinutes)} with nothing scheduled. " +
                    "Good moment for something you actually enjoy.",
                primaryAction = "See options",
                secondaryAction = "Not now"
            )
        }

        val pending = habits.count { !it.isDone }
        if (pending > 0) {
            return Suggestion(
                title = "Lumina Suggestion",
                body = "$pending ${if (pending == 1) "habit is" else "habits are"} still open today. " +
                    "They are small on purpose — pick the easiest one first.",
                primaryAction = "View habits",
                secondaryAction = null
            )
        }

        return Suggestion(
            title = "Lumina Suggestion",
            body = "Everything on today's list is done. Protect the rest of the evening — " +
                "rest is part of the plan, not a reward for finishing it.",
            primaryAction = null,
            secondaryAction = null
        )
    }

    /**
     * What to offer in a specific gap.
     *
     * Activities are drawn from the user's own stated interests, filtered to what
     * actually fits the slot, with deadline work promoted when something is due soon.
     */
    fun freeTimeActivities(
        profile: UserProfileEntity?,
        slotMinutes: Int,
        deadlines: List<DeadlineEntity>
    ): List<Activity> {
        val interests = profile?.interests.orEmpty()
        val results = mutableListOf<Activity>()

        val urgent = deadlines
            .filter { daysUntil(it.dueAt) <= 2 && it.progress < 100 }
            .minByOrNull { it.dueAt }

        if ("Netflix & Movies" in interests || "TV Series" in interests) {
            results += Activity(
                "Continue watching", "Pick up your series where you left off",
                42, "Watch", "play"
            )
        }
        if ("Walking" in interests) {
            results += Activity("Take a walk", "Fresh air around campus", 20, "Start", "walk")
        }
        if (urgent != null) {
            results += Activity(
                "${urgent.title} focus",
                "Due ${if (daysUntil(urgent.dueAt) <= 1) "tomorrow" else "in ${daysUntil(urgent.dueAt)} days"}",
                25, "Focus", "target", recommended = true
            )
        }
        if ("Music" in interests) {
            results += Activity("Music break", "Something from your usual playlist", 15, "Play", "music")
        }
        if ("Gaming" in interests && slotMinutes >= 30) {
            results += Activity("Gaming session", "Enough time for one round", 30, "Play", "play")
        }
        if ("Reading" in interests) {
            results += Activity("Read a chapter", "Away from a screen", 20, "Start", "book")
        }
        results += Activity("Breathing session", "Reset before your evening", 5, "Start", "meditation")

        // Only offer what genuinely fits, then lead with the recommendation.
        return results
            .filter { it.minutes <= slotMinutes + 5 }
            .sortedByDescending { it.recommended }
            .take(6)
    }

    /**
     * "Tomorrow looks busy" — fires when the day ahead has more committed than usual.
     * Returns null when there is nothing worth interrupting the user about.
     */
    fun deadlineStressWarning(
        deadlines: List<DeadlineEntity>,
        totalScheduledMinutes: Int
    ): Suggestion? {
        val soon = deadlines.filter { daysUntil(it.dueAt) <= 1 && it.progress < 80 }
        if (soon.isEmpty() || totalScheduledMinutes < 300) return null
        return Suggestion(
            title = "Tomorrow looks busy",
            body = "You have ${soon.size} ${if (soon.size == 1) "deadline" else "deadlines"} " +
                "close and a full schedule. Lumina can move some of that work into tonight's free time.",
            primaryAction = "Optimise my day",
            secondaryAction = "Keep current plan"
        )
    }
}
