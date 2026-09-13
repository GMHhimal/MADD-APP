package com.lumina.app.preview

import com.lumina.app.data.local.UserProfileEntity
import com.lumina.app.data.model.FreeSlot
import com.lumina.app.data.model.Mood
import com.lumina.app.data.repository.SeedData
import com.lumina.app.data.repository.SmartEngine
import com.lumina.app.feature.breathing.BreathPhase
import com.lumina.app.feature.breathing.BreathingUiState
import com.lumina.app.feature.focus.FocusUiState
import com.lumina.app.feature.freetime.FreeTimeUiState
import com.lumina.app.feature.habits.HabitsUiState
import com.lumina.app.feature.health.HealthUiState
import com.lumina.app.feature.home.HomeUiState
import com.lumina.app.feature.mood.MoodUiState
import com.lumina.app.feature.plan.PlanUiState
import com.lumina.app.feature.progress.ProgressUiState
import com.lumina.app.feature.progress.RangeFilter

/**
 * Static sample data used only by Android Studio Compose previews.
 * Runtime screens still use their real ViewModels + Room database.
 */
object PreviewData {
    val profile: UserProfileEntity = SeedData.profile().copy(
        onboardingComplete = true,
        smartSuggestions = true
    )

    private val habits = SeedData.habits().mapIndexed { index, item -> item.copy(id = (index + 1).toLong()) }
    private val deadlines = SeedData.deadlines().mapIndexed { index, item -> item.copy(id = (index + 1).toLong()) }
    private val timeline = SeedData.timeline().mapIndexed { index, item -> item.copy(id = (index + 1).toLong()) }
    private val reminders = SeedData.healthReminders().mapIndexed { index, item -> item.copy(id = (index + 1).toLong()) }

    val homeState = HomeUiState(
        loading = false,
        name = profile.displayName,
        greeting = "Good evening",
        dateLabel = "Friday, 12 September",
        balanceLabel = "Your day looks balanced",
        mood = Mood.GOOD,
        habitsDone = habits.count { it.isDone },
        habitsTotal = habits.size,
        focusMinutes = 48,
        freeMinutes = 75,
        timeline = timeline,
        insight = SmartEngine.Suggestion(
            title = "Lumina Suggestion",
            body = "You have 45 minutes free this afternoon. Spend 25 minutes on your SE Assignment and keep the rest for yourself.",
            primaryAction = "Use this plan",
            secondaryAction = "Change"
        )
    )

    val planState = PlanUiState(
        deadlines = deadlines,
        focusSlots = listOf(
            FreeSlot(16 * 60, 16 * 60 + 45),
            FreeSlot(20 * 60, 21 * 60)
        ),
        loading = false
    )

    val habitsState = HabitsUiState(
        habits = habits,
        doneCount = habits.count { it.isDone },
        totalCount = habits.size,
        streakDays = 7,
        weeklyConsistency = 71,
        loading = false
    )

    val moodState = MoodUiState(
        selectedMood = Mood.GOOD,
        selectedCauses = listOf("Study", "Sleep"),
        loggedToday = true,
        streakDays = 7
    )

    val freeTimeState = FreeTimeUiState(
        slot = FreeSlot(16 * 60, 16 * 60 + 45),
        activities = listOf(
            SmartEngine.Activity(
                title = "SE Assignment focus",
                subtitle = "Due tomorrow",
                minutes = 25,
                actionLabel = "Focus",
                iconKey = "target",
                recommended = true
            ),
            SmartEngine.Activity(
                title = "Take a walk",
                subtitle = "Fresh air around campus",
                minutes = 20,
                actionLabel = "Start",
                iconKey = "walk"
            ),
            SmartEngine.Activity(
                title = "Music break",
                subtitle = "Something from your usual playlist",
                minutes = 15,
                actionLabel = "Play",
                iconKey = "music"
            ),
            SmartEngine.Activity(
                title = "Breathing session",
                subtitle = "Reset before your evening",
                minutes = 5,
                actionLabel = "Start",
                iconKey = "meditation"
            )
        ),
        loading = false
    )

    val breathingState = BreathingUiState(
        phase = BreathPhase.INHALE,
        secondsLeftInPhase = 3,
        elapsedSeconds = 72,
        totalSeconds = 5 * 60,
        running = true,
        finished = false
    )

    val focusState = FocusUiState(
        taskTitle = "SE Assignment",
        totalSeconds = 25 * 60,
        elapsedSeconds = 8 * 60 + 15,
        running = true,
        finished = false
    )

    val healthState = HealthUiState(
        reminders = reminders.drop(1),
        nextUp = reminders.firstOrNull(),
        loading = false
    )

    val progressState = ProgressUiState(
        range = RangeFilter.WEEK,
        moodTrend = listOf(4, 5, 3, 4, 2, 4, 5),
        goodDays = 5,
        lowDays = 1,
        neutralDays = 1,
        habitConsistency = 71,
        gymSessions = 3,
        focusMinutes = 148,
        deadlinesDone = 2,
        deadlinesOpen = 2,
        insights = listOf(
            "You rated 71% of days positively" to "That is 5 good or great days out of 7 logged.",
            "Your focus is strongest late morning" to "Most completed focus sessions started between 10 AM and 12 PM."
        )
    )
}
