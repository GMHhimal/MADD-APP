package com.lumina.app.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.preview.PreviewData
import com.lumina.app.ui.components.IconChip
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.SelectableChip
import com.lumina.app.ui.formatMinutes
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.BorderStrong
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.LavenderSurface
import com.lumina.app.ui.theme.MintSurface
import com.lumina.app.ui.theme.Peach
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.PeachSurface
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.Success
import com.lumina.app.ui.theme.SurfaceSunken
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary

// ---------------------------------------------------------------------------
// Step 1 — lifestyle
// ---------------------------------------------------------------------------

private data class Goal(
    val label: String,
    val icon: ImageVector,
    val tint: Color,
    val tintSurface: Color
)

private val Goals = listOf(
    Goal("Manage Stress", LuminaIcons.Wave, LavenderDeep, LavenderSurface),
    Goal("Build Habits", LuminaIcons.Sprout, Success, MintSurface),
    Goal("Manage Deadlines", LuminaIcons.Target, PrimaryDeep, PrimarySurface),
    Goal("Find Free Time", LuminaIcons.Clock, PeachDeep, PeachSurface),
    Goal("Exercise Regularly", LuminaIcons.Dumbbell, PrimaryDeep, PrimarySurface),
    Goal("Eat on Time", LuminaIcons.Meal, PeachDeep, PeachSurface),
    Goal("Sleep Better", LuminaIcons.Moon, LavenderDeep, LavenderSurface),
    Goal("Balance Work & Life", LuminaIcons.Balance, Success, MintSurface)
)

private val Roles = listOf(
    "University Student", "School Student", "Working Professional", "Freelancer", "Other"
)

@Composable
fun OnboardingLifestyleScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit,
    viewModel: OnboardingViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: OnboardingViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val profile = if (inspectionMode) PreviewData.profile
    else runtimeViewModel!!.profile.collectAsStateWithLifecycle().value

    OnboardingScaffold(
        step = 1,
        title = "Let's understand\nyour lifestyle.",
        subtitle = "A few quick answers help Lumina build your day.",
        ctaLabel = "Next",
        onBack = onBack,
        onContinue = onContinue
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "What best describes you?",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                WrapRow(spacing = 10.dp) {
                    Roles.forEach { role ->
                        SelectableChip(
                            label = role,
                            selected = profile?.role == role,
                            onClick = { runtimeViewModel?.setRole(role) }
                        )
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "What would you like Lumina to help with?",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                val selected = profile?.goals.orEmpty()
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Goals.chunked(2).forEach { pair ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            pair.forEach { goal ->
                                GoalCard(
                                    goal = goal,
                                    selected = goal.label in selected,
                                    onClick = { runtimeViewModel?.toggleGoal(goal.label) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (pair.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalCard(
    goal: Goal,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier
            .clip(shape)
            .background(if (selected) PrimarySurface else SurfaceWhite)
            .border(
                width = if (selected) 1.8.dp else 1.dp,
                color = if (selected) Primary else BorderSubtle,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconChip(
                icon = goal.icon,
                tint = if (selected) PrimaryDeep else goal.tint,
                background = if (selected) SurfaceWhite else goal.tintSurface,
                size = 34.dp, iconSize = 20.dp, cornerRadius = 12.dp
            )
            Spacer(Modifier.weight(1f))
            if (selected) {
                Box(
                    Modifier.size(18.dp).clip(RoundedCornerShape(9.dp)).background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(LuminaIcons.Check, null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            goal.label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = if (selected) PrimaryDeep else TextPrimary
        )
    }
}

// ---------------------------------------------------------------------------
// Step 2 — interests
// ---------------------------------------------------------------------------

private val Interests = listOf(
    "Netflix & Movies" to LuminaIcons.Play,
    "TV Series" to LuminaIcons.Tv,
    "Gaming" to LuminaIcons.Game,
    "Music" to LuminaIcons.Music,
    "Reading" to LuminaIcons.Book,
    "Exercise" to LuminaIcons.Pulse,
    "Gym" to LuminaIcons.Dumbbell,
    "Walking" to LuminaIcons.Walk,
    "Meditation" to LuminaIcons.Wave,
    "Learning" to LuminaIcons.Cap,
    "Podcasts" to LuminaIcons.Mic,
    "Socializing" to LuminaIcons.Users,
    "Creative" to LuminaIcons.Sparkle,
    "Photography" to LuminaIcons.Camera,
    "Drawing" to LuminaIcons.Pen,
    "Coding" to LuminaIcons.Code
)

private val FreeTimeDurations = listOf("10–15 min", "20–30 min", "30–60 min", "1+ hour")

@Composable
fun OnboardingInterestsScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit,
    viewModel: OnboardingViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: OnboardingViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val profile = if (inspectionMode) PreviewData.profile
    else runtimeViewModel!!.profile.collectAsStateWithLifecycle().value
    val selected = profile?.interests.orEmpty()

    OnboardingScaffold(
        step = 2,
        title = "What do you enjoy doing?",
        subtitle = "Lumina uses this to make your free time more meaningful.",
        ctaLabel = "Next",
        onBack = onBack,
        onContinue = onContinue
    ) {
        item {
            WrapRow(spacing = 9.dp) {
                Interests.forEach { (label, icon) ->
                    SelectableChip(
                        label = label,
                        selected = label in selected,
                        leadingIcon = icon,
                        showCheckWhenSelected = false,
                        onClick = { runtimeViewModel?.toggleInterest(label) }
                    )
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Preferred free-time length",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                WrapRow(spacing = 9.dp) {
                    FreeTimeDurations.forEach { duration ->
                        SelectableChip(
                            label = duration,
                            selected = profile?.preferredFreeTime == duration,
                            selectedColor = Peach,
                            selectedContentColor = PeachDeep,
                            selectedBackground = PeachSurface,
                            showCheckWhenSelected = false,
                            onClick = { runtimeViewModel?.setPreferredFreeTime(duration) }
                        )
                    }
                }
            }
        }
        item {
            LuminaCard(contentPadding = PaddingValues(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconChip(LuminaIcons.Bulb, Primary, PrimarySurface, size = 42.dp, iconSize = 21.dp)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Suggest activities when I have free time",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            "Lumina will nudge you with something you enjoy.",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Switch(
                        checked = profile?.smartSuggestions ?: true,
                        onCheckedChange = { runtimeViewModel?.setSmartSuggestions(it) },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Primary,
                            checkedThumbColor = Color.White,
                            uncheckedTrackColor = BorderStrong,
                            uncheckedThumbColor = Color.White,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Step 3 — routine
// ---------------------------------------------------------------------------

private val WeekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

@Composable
fun OnboardingRoutineScreen(
    onBack: () -> Unit,
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: OnboardingViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val profile = if (inspectionMode) PreviewData.profile
    else runtimeViewModel!!.profile.collectAsStateWithLifecycle().value

    OnboardingScaffold(
        step = 3,
        title = "Shape your daily rhythm",
        subtitle = "Lumina plans around these anchors. You can change them any time.",
        ctaLabel = "Finish Setup",
        onBack = onBack,
        onContinue = { runtimeViewModel?.finish(onFinish) }
    ) {
        item {
            RoutineCard("Daily Routine") {
                TimeRow(LuminaIcons.Sun, PeachDeep, PeachSurface, "Wake up",
                    profile?.wakeUpMinutes ?: 390) { runtimeViewModel?.setWakeUp(it) }
                RoutineConnector()
                TimeRow(LuminaIcons.Moon, LavenderDeep, LavenderSurface, "Sleep",
                    profile?.sleepMinutes ?: 1380) { runtimeViewModel?.setSleep(it) }
            }
        }
        item {
            RoutineCard("Meals") {
                TimeRow(LuminaIcons.Coffee, PeachDeep, PeachSurface, "Breakfast",
                    profile?.breakfastMinutes ?: 450) { runtimeViewModel?.setBreakfast(it) }
                RoutineConnector()
                TimeRow(LuminaIcons.Meal, Success, MintSurface, "Lunch",
                    profile?.lunchMinutes ?: 750) { runtimeViewModel?.setLunch(it) }
                RoutineConnector()
                TimeRow(LuminaIcons.Meal, PrimaryDeep, PrimarySurface, "Dinner",
                    profile?.dinnerMinutes ?: 1170) { runtimeViewModel?.setDinner(it) }
            }
        }
        item {
            RoutineCard("Exercise") {
                TimeRow(LuminaIcons.Dumbbell, PrimaryDeep, PrimarySurface, "Gym",
                    profile?.exerciseMinutes ?: 1080) { runtimeViewModel?.setExercise(it) }
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier.padding(start = 54.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WeekDays.forEach { day ->
                        val on = day in profile?.exerciseDays.orEmpty()
                        Box(
                            Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (on) Primary else SurfaceSunken)
                                .clickable { runtimeViewModel?.toggleExerciseDay(day) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                day.first().toString(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = if (on) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineCard(title: String, content: @Composable () -> Unit) {
    LuminaCard(contentPadding = PaddingValues(16.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(Modifier.height(10.dp))
        content()
    }
}

@Composable
private fun RoutineConnector() {
    Box(
        Modifier
            .padding(start = 19.dp)
            .width(2.dp)
            .height(12.dp)
            .background(BorderSubtle)
    )
}

@Composable
private fun TimeRow(
    icon: ImageVector,
    tint: Color,
    tintSurface: Color,
    label: String,
    minutes: Int,
    onPick: (Int) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth().wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconChip(icon, tint, tintSurface, size = 40.dp, iconSize = 20.dp, cornerRadius = 14.dp)
        Spacer(Modifier.width(14.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Box(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceSunken)
                .clickable { showPicker = true }
                .padding(horizontal = 13.dp, vertical = 9.dp)
        ) {
            Text(
                formatMinutes(minutes),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary
            )
        }
    }

    if (showPicker) {
        LuminaTimePickerDialog(
            initialMinutes = minutes,
            onDismiss = { showPicker = false },
            onConfirm = {
                onPick(it)
                showPicker = false
            }
        )
    }
}

/** Wraps children onto new rows when they run out of width. Used by every chip group. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WrapRow(
    spacing: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        content()
    }
}

@Preview(name = "Onboarding 1 - Lifestyle", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OnboardingLifestylePreview() {
    LuminaTheme { OnboardingLifestyleScreen(onBack = {}, onContinue = {}) }
}

@Preview(name = "Onboarding 2 - Interests", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OnboardingInterestsPreview() {
    LuminaTheme { OnboardingInterestsScreen(onBack = {}, onContinue = {}) }
}

@Preview(name = "Onboarding 3 - Routine", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OnboardingRoutinePreview() {
    LuminaTheme { OnboardingRoutineScreen(onBack = {}, onFinish = {}) }
}
