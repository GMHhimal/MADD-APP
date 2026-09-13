package com.lumina.app.feature.habits

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.preview.PreviewData
import com.lumina.app.ui.components.EmptyState
import com.lumina.app.ui.components.HabitRow
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.LuminaFab
import com.lumina.app.ui.components.Pill
import com.lumina.app.ui.components.ProgressRing
import com.lumina.app.ui.components.SectionHeader
import com.lumina.app.ui.formatMinutes
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.todayLabel
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Peach
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.PeachSurface
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.Success
import com.lumina.app.ui.theme.SuccessSurface
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
import com.lumina.app.ui.theme.TextTertiary

@Composable
fun HabitsScreen(
    onOpenHealth: () -> Unit,
    viewModel: HabitsViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: HabitsViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.habitsState
    else runtimeViewModel!!.uiState.collectAsStateWithLifecycle().value
    var showAdd by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Canvas)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Daily Habits", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
                        Spacer(Modifier.height(2.dp))
                        Text(todayLabel(), style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                    }
                    Pill(
                        text = "${state.streakDays} days",
                        background = PeachSurface,
                        contentColor = PeachDeep,
                        leadingIcon = LuminaIcons.Flame,
                        border = BorderStroke(1.dp, Peach.copy(alpha = 0.6f))
                    )
                }
            }

            if (state.habits.isEmpty() && !state.loading) {
                item {
                    EmptyState(
                        icon = LuminaIcons.Sprout,
                        title = "Start with one small habit",
                        body = "Pick something you can finish in two minutes. Lumina will remind you at the right time, not all day long.",
                        actionLabel = "Create my first habit",
                        onAction = { showAdd = true },
                        accent = Success,
                        accentSurface = SuccessSurface
                    )
                }
            } else {
                item {
                    LuminaCard(shape = RoundedCornerShape(24.dp), contentPadding = PaddingValues(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ProgressRing(progress = state.progress, size = 112.dp) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "${state.doneCount}",
                                        style = MaterialTheme.typography.displayLarge,
                                        color = TextPrimary
                                    )
                                    Text(
                                        "of ${state.totalCount}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                            }
                            Spacer(Modifier.width(18.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "${state.doneCount} of ${state.totalCount} completed",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = remainingSummary(state),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Spacer(Modifier.height(10.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    MiniStat("Today", "${state.weeklyConsistency}%", SuccessSurface, Success, Modifier.weight(1f))
                                    MiniStat("Streak", "${state.streakDays} d", PrimarySurface, PrimaryDeep, Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                item {
                    SectionHeader("Today's habits") {
                        Text("Tap to complete", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                    }
                }

                items(state.habits, key = { it.id }) { habit ->
                    HabitRow(
                        title = habit.title,
                        detail = habitDetail(habit.title, habit.detail, habit.currentCount, habit.targetCount, habit.isDone, habit.scheduledMinutes),
                        icon = LuminaIcons.byKey(habit.iconKey),
                        isDone = habit.isDone,
                        onToggle = { runtimeViewModel?.toggleHabit(habit.id) }
                    )
                }

                item {
                    LuminaCard(contentPadding = PaddingValues(16.dp), onClick = onOpenHealth) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            com.lumina.app.ui.components.IconChip(
                                LuminaIcons.HeartPulse, PeachDeep, PeachSurface, size = 42.dp, iconSize = 21.dp
                            )
                            Spacer(Modifier.width(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "Health reminders",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "Checkups and tests you have asked Lumina to remember",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                            androidx.compose.material3.Icon(
                                LuminaIcons.ChevronRight, null,
                                tint = TextTertiary,
                                modifier = Modifier.width(20.dp)
                            )
                        }
                    }
                }
            }
        }

        LuminaFab(
            text = "New Habit",
            icon = LuminaIcons.Plus,
            onClick = { showAdd = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 24.dp, bottom = 24.dp)
        )
    }

    if (showAdd) {
        AddHabitDialog(
            onDismiss = { showAdd = false },
            onSave = { title, detail, iconKey, target ->
                runtimeViewModel?.addHabit(title, detail, iconKey, target)
                showAdd = false
            }
        )
    }
}

private fun remainingSummary(state: HabitsUiState): String {
    val pending = state.habits.filter { !it.isDone }
    return when {
        pending.isEmpty() && state.totalCount > 0 -> "Everything done. Enjoy the evening."
        pending.size == 1 -> "One left: ${pending.first().title.lowercase()}."
        else -> "Still open: ${pending.take(2).joinToString(" and ") { it.title.lowercase() }}."
    }
}

private fun habitDetail(
    title: String,
    detail: String,
    current: Int,
    target: Int,
    isDone: Boolean,
    scheduledMinutes: Int?
): String = when {
    isDone -> "Completed"
    target > 1 -> "$current / $target · $detail"
    scheduledMinutes != null -> "${formatMinutes(scheduledMinutes)} · $detail"
    else -> detail
}

@Composable
private fun MiniStat(
    label: String,
    value: String,
    background: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Text(
            value,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = contentColor
        )
    }
}

@Preview(name = "Habits", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HabitsScreenPreview() {
    LuminaTheme { HabitsScreen(onOpenHealth = {}) }
}
