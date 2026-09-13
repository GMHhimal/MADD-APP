package com.lumina.app.navigation

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lumina.app.ServiceLocator
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
import com.lumina.app.ui.components.LuminaBottomBar
import com.lumina.app.ui.theme.Canvas

private val TopLevelRoutes = TopLevelDestination.entries.map { it.route }.toSet()

/** Screens that take over the whole display: no bottom bar, no chrome. */
private val ImmersiveRoutes = setOf(Routes.BREATHING, Routes.FOCUS, Routes.WELCOME)

@Composable
fun LuminaApp(
    startRouteFromNotification: String? = null,
    onNotificationRouteHandled: () -> Unit = {}
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val profile by ServiceLocator.repository.profile
        .collectAsStateWithLifecycle(initialValue = null)

    // Skip Welcome for returning users, once the profile row has actually loaded.
    var startDecided by remember { mutableStateOf(false) }
    LaunchedEffect(profile) {
        val loaded = profile ?: return@LaunchedEffect
        if (startDecided) return@LaunchedEffect
        startDecided = true
        if (loaded.onboardingComplete) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.WELCOME) { inclusive = true }
            }
        }
    }

    // A notification tap carries the route it should land on.
    LaunchedEffect(startRouteFromNotification, startDecided) {
        val route = startRouteFromNotification ?: return@LaunchedEffect
        if (!startDecided) return@LaunchedEffect
        if (route == Routes.FOCUS) {
            navController.navigate("${Routes.FOCUS}/${Uri.encode("Focus session")}")
        } else {
            navController.navigate(route)
        }
        onNotificationRouteHandled()
    }

    val showBottomBar = currentRoute in TopLevelRoutes

    Scaffold(
        containerColor = Canvas,
        bottomBar = {
            if (showBottomBar) {
                LuminaBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { destination -> navController.switchTab(destination) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .background(Canvas)
                .padding(bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp)
        ) {
            LuminaNavHost(navController)
        }
    }
}

@Composable
private fun LuminaNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME,
        // Forward motion slides slightly and fades; back mirrors it. 320ms / 220ms
        // matches the timing spec on the Figma handoff board.
        enterTransition = {
            slideInHorizontally(tween(320)) { it / 14 } + fadeIn(tween(220))
        },
        exitTransition = { fadeOut(tween(180)) },
        popEnterTransition = { fadeIn(tween(220)) },
        popExitTransition = {
            slideOutHorizontally(tween(280)) { it / 14 } + fadeOut(tween(180))
        }
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onGetStarted = { navController.navigate(Routes.ONBOARDING_LIFESTYLE) },
                onSignIn = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING_LIFESTYLE) {
            OnboardingLifestyleScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Routes.ONBOARDING_INTERESTS) }
            )
        }
        composable(Routes.ONBOARDING_INTERESTS) {
            OnboardingInterestsScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Routes.ONBOARDING_ROUTINE) }
            )
        }
        composable(Routes.ONBOARDING_ROUTINE) {
            OnboardingRoutineScreen(
                onBack = { navController.popBackStack() },
                onFinish = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onOpenMood = { navController.navigate(Routes.MOOD) },
                onOpenHabits = { navController.switchTab(TopLevelDestination.Wellness) },
                onOpenPlan = { navController.switchTab(TopLevelDestination.Plan) },
                onOpenFreeTime = { navController.navigate(Routes.FREE_TIME) },
                onStartFocus = { navController.navigate("${Routes.FOCUS}/${Uri.encode("Focus session")}") }
            )
        }

        composable(Routes.PLAN) {
            PlanScreen(
                onStartFocus = { task -> navController.navigate("${Routes.FOCUS}/${Uri.encode(task)}") }
            )
        }

        composable(Routes.HABITS) {
            HabitsScreen(onOpenHealth = { navController.navigate(Routes.HEALTH) })
        }

        composable(Routes.PROGRESS) { ProgressScreen() }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onRestartSetup = {
                    navController.navigate(Routes.ONBOARDING_LIFESTYLE)
                },
                onOpenHealth = { navController.navigate(Routes.HEALTH) }
            )
        }

        composable(Routes.FREE_TIME) {
            FreeTimeScreen(
                onBack = { navController.popBackStack() },
                onStartFocus = { task -> navController.navigate("${Routes.FOCUS}/${Uri.encode(task)}") },
                onStartBreathing = { navController.navigate(Routes.BREATHING) }
            )
        }

        composable(Routes.MOOD) {
            MoodScreen(
                onBack = { navController.popBackStack() },
                onStartBreathing = { navController.navigate(Routes.BREATHING) },
                onOpenFreeTime = { navController.navigate(Routes.FREE_TIME) }
            )
        }

        composable(Routes.BREATHING) {
            BreathingScreen(onClose = { navController.popBackStack() })
        }

        composable(Routes.HEALTH) {
            HealthScreen(onBack = { navController.popBackStack() })
        }

        composable("${Routes.FOCUS}/{task}") { entry ->
            FocusModeScreen(
                taskTitle = entry.arguments?.getString("task")?.let(Uri::decode) ?: "Focus session",
                onExit = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Tab switching keeps a single copy of each destination on the stack and restores the
 * scroll position you left it at.
 */
private fun NavHostController.switchTab(destination: TopLevelDestination) {
    navigate(destination.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
