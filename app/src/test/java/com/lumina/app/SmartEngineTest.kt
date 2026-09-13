package com.lumina.app

import com.lumina.app.data.local.DeadlineEntity
import com.lumina.app.data.local.HabitEntity
import com.lumina.app.data.model.FreeSlot
import com.lumina.app.data.model.Priority
import com.lumina.app.data.repository.SmartEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * The suggestion rules are pure functions, so they can be verified without an emulator.
 * These are the cases that decide what the user actually sees on the Home screen.
 */
class SmartEngineTest {

    private fun deadline(
        title: String = "SE Assignment",
        inDays: Long = 1,
        progress: Int = 65,
        priority: Priority = Priority.HIGH
    ) = DeadlineEntity(
        id = 1,
        title = title,
        course = "Software Engineering",
        dueAt = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(inDays),
        progress = progress,
        priority = priority
    )

    private fun habit(done: Boolean) = HabitEntity(
        title = "Drink Water", iconKey = "water", detail = "8 glasses",
        scheduledMinutes = null, isDone = done
    )

    @Test
    fun `an urgent deadline plus a long enough gap produces a concrete plan`() {
        val insight = SmartEngine.dailyInsight(
            freeSlots = listOf(FreeSlot(16 * 60, 16 * 60 + 45)),
            deadlines = listOf(deadline()),
            habits = emptyList()
        )
        assertTrue(insight.body.contains("SE Assignment"))
        assertEquals("Use this plan", insight.primaryAction)
    }

    @Test
    fun `with no deadlines and no gaps the open habits are surfaced instead`() {
        val insight = SmartEngine.dailyInsight(
            freeSlots = emptyList(),
            deadlines = emptyList(),
            habits = listOf(habit(false), habit(true))
        )
        assertTrue(insight.body.contains("1 habit"))
    }

    @Test
    fun `a finished day is left alone rather than filled with advice`() {
        val insight = SmartEngine.dailyInsight(
            freeSlots = emptyList(),
            deadlines = emptyList(),
            habits = listOf(habit(true), habit(true))
        )
        assertNull(insight.primaryAction)
    }

    @Test
    fun `activities never exceed the time actually available`() {
        val profile = com.lumina.app.data.local.UserProfileEntity(
            displayName = "Himal", role = "University Student",
            goals = emptyList(),
            interests = listOf("Netflix & Movies", "Walking", "Music"),
            preferredFreeTime = "20–30 min",
            wakeUpMinutes = 390, sleepMinutes = 1380,
            breakfastMinutes = 450, lunchMinutes = 750, dinnerMinutes = 1170,
            exerciseMinutes = 1080, exerciseDays = listOf("Mon"),
            smartSuggestions = true, onboardingComplete = true
        )
        val activities = SmartEngine.freeTimeActivities(profile, slotMinutes = 20, deadlines = emptyList())
        assertTrue(activities.isNotEmpty())
        assertTrue(activities.all { it.minutes <= 25 })
    }

    @Test
    fun `the deadline recommendation is promoted to the top of the list`() {
        val activities = SmartEngine.freeTimeActivities(
            profile = null, slotMinutes = 45, deadlines = listOf(deadline())
        )
        assertTrue(activities.first().recommended)
    }

    @Test
    fun `stress warning stays quiet on a light day`() {
        assertNull(SmartEngine.deadlineStressWarning(listOf(deadline()), totalScheduledMinutes = 120))
    }

    @Test
    fun `stress warning fires when deadlines meet a full schedule`() {
        assertNotNull(SmartEngine.deadlineStressWarning(listOf(deadline()), totalScheduledMinutes = 480))
    }

    @Test
    fun `balance label reacts to how packed the day is`() {
        assertEquals("Today is tightly packed", SmartEngine.balanceLabel(2, 6, freeMinutes = 10))
        assertEquals("Your day looks balanced", SmartEngine.balanceLabel(4, 6, freeMinutes = 90))
        assertEquals("Nothing tracked yet", SmartEngine.balanceLabel(0, 0, freeMinutes = 500))
    }
}
