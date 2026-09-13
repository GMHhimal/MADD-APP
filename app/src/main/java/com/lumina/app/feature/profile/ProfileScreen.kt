package com.lumina.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.preview.PreviewData
import com.lumina.app.feature.onboarding.WrapRow
import com.lumina.app.notifications.LuminaNotifications
import com.lumina.app.notifications.ReminderScheduler
import com.lumina.app.ui.components.DisclaimerNote
import com.lumina.app.ui.components.HairlineDivider
import com.lumina.app.ui.components.IconChip
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.Pill
import com.lumina.app.ui.components.SectionHeader
import com.lumina.app.ui.components.SelectableChip
import com.lumina.app.ui.formatMinutes
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.BorderStrong
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Danger
import com.lumina.app.ui.theme.DangerSurface
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.SurfaceSunken
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
import com.lumina.app.ui.theme.TextTertiary

@Composable
fun ProfileScreen(
    onRestartSetup: () -> Unit,
    onOpenHealth: () -> Unit,
    viewModel: ProfileViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: ProfileViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val profile = if (inspectionMode) PreviewData.profile
    else runtimeViewModel!!.profile.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    Box(Modifier.fillMaxSize().background(Canvas)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF7FB9EE), Primary))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            profile?.displayName?.take(1)?.uppercase() ?: "L",
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            profile?.displayName ?: "Your profile",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            profile?.role ?: "",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            item {
                LuminaCard(contentPadding = PaddingValues(16.dp)) {
                    Text("Your routine", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Spacer(Modifier.height(12.dp))
                    profile?.let { p ->
                        RoutineLine(LuminaIcons.Sun, "Wake up", formatMinutes(p.wakeUpMinutes))
                        RoutineLine(LuminaIcons.Coffee, "Breakfast", formatMinutes(p.breakfastMinutes))
                        RoutineLine(LuminaIcons.Meal, "Lunch", formatMinutes(p.lunchMinutes))
                        RoutineLine(LuminaIcons.Meal, "Dinner", formatMinutes(p.dinnerMinutes))
                        RoutineLine(LuminaIcons.Dumbbell, "Exercise", formatMinutes(p.exerciseMinutes))
                        RoutineLine(LuminaIcons.Moon, "Sleep", formatMinutes(p.sleepMinutes))
                    }
                }
            }

            item {
                LuminaCard(contentPadding = PaddingValues(16.dp)) {
                    Text("Interests", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Spacer(Modifier.height(12.dp))
                    WrapRow(spacing = 8.dp) {
                        profile?.interests.orEmpty().forEach { interest ->
                            SelectableChip(
                                label = interest,
                                selected = true,
                                showCheckWhenSelected = false,
                                onClick = onRestartSetup
                            )
                        }
                    }
                    if (profile?.interests.orEmpty().isEmpty()) {
                        Text(
                            "No interests chosen yet. Free-time suggestions will stay generic until you add some.",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            item { SectionHeader("Notifications") }

            item {
                LuminaCard(contentPadding = PaddingValues(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconChip(LuminaIcons.Bulb, Primary, PrimarySurface, size = 42.dp, iconSize = 21.dp)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Smart suggestions",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = TextPrimary
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                "Nudge me when free time appears",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
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
                    Spacer(Modifier.height(14.dp))
                    HairlineDivider()
                    Spacer(Modifier.height(14.dp))
                    Text(
                        "Schedule reminders from your routine",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(10.dp))
                    LuminaButton(
                        text = "Turn on routine reminders",
                        onClick = {
                            profile?.let { p ->
                                ReminderScheduler.scheduleRoutine(
                                    context = context,
                                    breakfastMinutes = p.breakfastMinutes,
                                    lunchMinutes = p.lunchMinutes,
                                    dinnerMinutes = p.dinnerMinutes,
                                    exerciseMinutes = p.exerciseMinutes,
                                    sleepMinutes = p.sleepMinutes
                                )
                            }
                        },
                        style = LuminaButtonStyle.Soft,
                        height = 48.dp
                    )
                }
            }

            item {
                LuminaCard(contentPadding = PaddingValues(16.dp)) {
                    Text(
                        "Preview a notification",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Fires the real notification so you can see how each type behaves.",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(12.dp))
                    WrapRow(spacing = 8.dp) {
                        listOf(
                            "Habit" to { LuminaNotifications.habitReminder(context, "drink water", "You're 2 glasses behind today's goal.") },
                            "Gym" to { LuminaNotifications.gymReminder(context, 30) },
                            "Meal" to { LuminaNotifications.mealReminder(context, "Lunch", formatMinutes(profile?.lunchMinutes ?: 750)) },
                            "Deadline" to { LuminaNotifications.deadlineAlert(context, "SE Assignment", 65) },
                            "Free time" to { LuminaNotifications.freeTimeSuggestion(context, 45, "4:00 PM") },
                            "Wellness" to { LuminaNotifications.wellnessCheckIn(context) },
                            "Health" to { LuminaNotifications.healthReminder(context, "Cholesterol test", 20) }
                        ).forEach { (label, action) ->
                            SelectableChip(
                                label = label,
                                selected = false,
                                leadingIcon = LuminaIcons.Bell,
                                onClick = action
                            )
                        }
                    }
                }
            }

            item { SectionHeader("Settings") }

            item {
                LuminaCard(contentPadding = PaddingValues(0.dp)) {
                    SettingsRow(LuminaIcons.Profile, "Personal preferences", onRestartSetup)
                    HairlineDivider()
                    SettingsRow(LuminaIcons.Heart, "Interests and activities", onRestartSetup)
                    HairlineDivider()
                    SettingsRow(LuminaIcons.Calendar, "Daily routine", onRestartSetup)
                    HairlineDivider()
                    SettingsRow(LuminaIcons.HeartPulse, "Health reminders", onOpenHealth)
                    HairlineDivider()
                    SettingsRow(LuminaIcons.Repeat, "Reset today's habits") { runtimeViewModel?.resetHabitsForNewDay() }
                }
            }

            item {
                DisclaimerNote(
                    title = "Your data stays on this device",
                    body = "Moods, habits and health reminders are stored in a local database. Nothing is uploaded.",
                    icon = LuminaIcons.Shield
                )
            }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(17.dp))
                        .background(DangerSurface)
                        .clickable {
                            runtimeViewModel?.restartSetup()
                            onRestartSetup()
                        }
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(LuminaIcons.Logout, null, tint = Danger, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Run setup again",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = Danger
                    )
                }
            }
        }
    }
}

@Composable
private fun RoutineLine(icon: ImageVector, label: String, time: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.labelLarge, color = TextPrimary, modifier = Modifier.weight(1f))
        Pill(time, SurfaceSunken, TextPrimary)
    }
}

@Composable
private fun SettingsRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconChip(icon, TextSecondary, SurfaceSunken, size = 36.dp, iconSize = 19.dp, cornerRadius = 12.dp)
        Spacer(Modifier.width(13.dp))
        Text(label, style = MaterialTheme.typography.labelLarge, color = TextPrimary, modifier = Modifier.weight(1f))
        Icon(LuminaIcons.ChevronRight, null, tint = TextTertiary, modifier = Modifier.size(17.dp))
    }
}

@Preview(name = "Profile", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun ProfileScreenPreview() {
    LuminaTheme { ProfileScreen(onRestartSetup = {}, onOpenHealth = {}) }
}
