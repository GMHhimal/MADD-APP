package com.lumina.app.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.lumina.app.ui.icons.LuminaIcons

object Routes {
    const val WELCOME = "welcome"
    const val ONBOARDING_LIFESTYLE = "onboarding/lifestyle"
    const val ONBOARDING_INTERESTS = "onboarding/interests"
    const val ONBOARDING_ROUTINE = "onboarding/routine"

    const val HOME = "home"
    const val PLAN = "plan"
    const val HABITS = "habits"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"

    const val FREE_TIME = "freetime"
    const val MOOD = "mood"
    const val BREATHING = "breathing"
    const val HEALTH = "health"
    const val FOCUS = "focus"
}

/** The five persistent tabs. Order matches the bottom bar in the prototype. */
enum class TopLevelDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    Home(Routes.HOME, "Home", LuminaIcons.Home),
    Plan(Routes.PLAN, "Plan", LuminaIcons.Plan),
    Wellness(Routes.HABITS, "Wellness", LuminaIcons.Wellness),
    Progress(Routes.PROGRESS, "Progress", LuminaIcons.Progress),
    Profile(Routes.PROFILE, "Profile", LuminaIcons.Profile)
}
