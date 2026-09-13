package com.lumina.app.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lumina.app.feature.breathing.BreathingScreen
import com.lumina.app.feature.focus.FocusModeScreen
import com.lumina.app.feature.freetime.FreeTimeScreen
import com.lumina.app.feature.habits.HabitsScreen
import com.lumina.app.feature.health.HealthScreen
import com.lumina.app.feature.home.HomeScreen
import com.lumina.app.feature.mood.MoodScreen
import com.lumina.app.feature.onboarding.OnboardingInterestsScreen
import com.lumina.app.feature.onboarding.OnboardingLifestyleScreen
import com.lumina.app.feature.onboarding.OnboardingRoutineScreen
import com.lumina.app.feature.plan.PlanScreen
import com.lumina.app.feature.profile.ProfileScreen
import com.lumina.app.feature.progress.ProgressScreen
import com.lumina.app.feature.welcome.WelcomeScreen
import com.lumina.app.ui.theme.LuminaTheme

/**
 * One-file preview gallery for viva/demo use.
 * Open this file and choose Split or Design in Android Studio to render every main screen.
 */
private const val PREVIEW_WIDTH = 412
private const val PREVIEW_HEIGHT = 915

@Preview(name = "01 Welcome", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryWelcome() = LuminaTheme {
    WelcomeScreen(onGetStarted = {}, onSignIn = {})
}

@Preview(name = "02 Onboarding - Lifestyle", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryOnboardingLifestyle() = LuminaTheme {
    OnboardingLifestyleScreen(onBack = {}, onContinue = {})
}

@Preview(name = "03 Onboarding - Interests", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryOnboardingInterests() = LuminaTheme {
    OnboardingInterestsScreen(onBack = {}, onContinue = {})
}

@Preview(name = "04 Onboarding - Routine", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryOnboardingRoutine() = LuminaTheme {
    OnboardingRoutineScreen(onBack = {}, onFinish = {})
}

@Preview(name = "05 Home", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryHome() = LuminaTheme {
    HomeScreen(
        onOpenMood = {},
        onOpenHabits = {},
        onOpenPlan = {},
        onOpenFreeTime = {},
        onStartFocus = {}
    )
}

@Preview(name = "06 Plan", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryPlan() = LuminaTheme {
    PlanScreen(onStartFocus = {})
}

@Preview(name = "07 Habits", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryHabits() = LuminaTheme {
    HabitsScreen(onOpenHealth = {})
}

@Preview(name = "08 Mood", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryMood() = LuminaTheme {
    MoodScreen(onBack = {}, onStartBreathing = {}, onOpenFreeTime = {})
}

@Preview(name = "09 Free Time", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryFreeTime() = LuminaTheme {
    FreeTimeScreen(onBack = {}, onStartFocus = {}, onStartBreathing = {})
}

@Preview(name = "10 Breathing", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryBreathing() = LuminaTheme {
    BreathingScreen(onClose = {})
}

@Preview(name = "11 Focus", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryFocus() = LuminaTheme {
    FocusModeScreen(taskTitle = "SE Assignment", onExit = {})
}

@Preview(name = "12 Health", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryHealth() = LuminaTheme {
    HealthScreen(onBack = {})
}

@Preview(name = "13 Progress", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryProgress() = LuminaTheme {
    ProgressScreen()
}

@Preview(name = "14 Profile", group = "Lumina Screens", widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
@Composable
private fun GalleryProfile() = LuminaTheme {
    ProfileScreen(onRestartSetup = {}, onOpenHealth = {})
}
