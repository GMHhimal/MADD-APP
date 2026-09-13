package com.lumina.app.feature.plan

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.data.local.DeadlineEntity
import com.lumina.app.data.model.Priority
import com.lumina.app.preview.PreviewData
import com.lumina.app.ui.components.CompactAction
import com.lumina.app.ui.components.EmptyState
import com.lumina.app.ui.components.IconChip
import com.lumina.app.ui.components.LinearMeter
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.LuminaFab
import com.lumina.app.ui.components.LuminaVoiceBrush
import com.lumina.app.ui.components.Pill
import com.lumina.app.ui.components.SectionHeader
import com.lumina.app.ui.formatDate
import com.lumina.app.ui.formatDueLabel
import com.lumina.app.ui.formatDuration
import com.lumina.app.ui.formatRange
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Danger
import com.lumina.app.ui.theme.DangerSurface
import com.lumina.app.ui.theme.Lavender
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.Peach
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.SurfaceSunken
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
import com.lumina.app.ui.theme.TextTertiary
import com.lumina.app.ui.theme.Warning
import com.lumina.app.ui.theme.WarningSurface

@Composable
fun PlanScreen(
    onStartFocus: (String) -> Unit,
    viewModel: PlanViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: PlanViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.planState
    else runtimeViewModel!!.uiState.collectAsStateWithLifecycle().value
    var showAddDialog by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Canvas)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("My Plan", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            formatDate(System.currentTimeMillis(), "MMMM yyyy"),
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                    }
                    IconChip(LuminaIcons.Calendar, TextPrimary, SurfaceWhite, size = 44.dp, iconSize = 20.dp)
                }
            }

            item { WeekStrip() }

            if (state.deadlines.isEmpty() && !state.loading) {
                item {
                    EmptyState(
                        icon = LuminaIcons.Leaf,
                        title = "Nothing urgent right now",
                        body = "Enjoy the breathing room. When a deadline appears, Lumina will help you plan the time for it.",
                        actionLabel = "Add a deadline",
                        onAction = { showAddDialog = true }
                    )
                }
            } else {
                item {
                    SectionHeader("Important Deadlines") {
                        Text(
                            "${state.deadlines.count { it.progress < 100 }} active",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
                items(state.deadlines, key = { it.id }) { deadline ->
                    DeadlineCard(
                        deadline = deadline,
                        onContinue = { onStartFocus(deadline.title) },
                        onDelete = { runtimeViewModel?.deleteDeadline(deadline) }
                    )
                }
            }

            if (state.focusSlots.isNotEmpty()) {
                item { SectionHeader("Smart Time Allocation") }
                item {
                    LuminaCard(
                        brush = LuminaVoiceBrush,
                        border = BorderStroke(1.2.dp, Lavender),
                        contentPadding = PaddingValues(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(LuminaIcons.Sparkle, null, tint = LavenderDeep, modifier = Modifier.size(18.dp))
                            Text(
                                "Lumina found ${state.focusSlots.size} focus " +
                                    "${if (state.focusSlots.size == 1) "period" else "periods"} today.",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = PrimaryDeep
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        state.focusSlots.take(3).forEach { slot ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(SurfaceWhite.copy(alpha = 0.85f))
                                    .padding(start = 14.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(LuminaIcons.Clock, null, tint = PrimaryDeep, modifier = Modifier.size(17.dp))
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        formatRange(slot.startMinutes, slot.endMinutes),
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        "${formatDuration(slot.durationMinutes)} available",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                                Box(
                                    Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(PrimarySurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(LuminaIcons.Plus, "Add to my day", tint = PrimaryDeep, modifier = Modifier.size(17.dp))
                                }
                            }
                        }
                        LuminaButton(
                            text = "Add to my day",
                            onClick = {
                                state.focusSlots.firstOrNull()?.let {
                                    runtimeViewModel?.scheduleFocusSlot(
                                        it,
                                        state.deadlines.firstOrNull()?.title ?: "Focus session"
                                    )
                                }
                            },
                            height = 48.dp
                        )
                    }
                }
            }
        }

        LuminaFab(
            text = "Add Task",
            icon = LuminaIcons.Plus,
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 24.dp)
        )
    }

    if (showAddDialog) {
        AddDeadlineDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, course, days, priority ->
                runtimeViewModel?.addDeadline(title, course, days, priority)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun WeekStrip() {
    val calendar = java.util.Calendar.getInstance()
    val today = calendar.get(java.util.Calendar.DAY_OF_WEEK)
    val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    // Monday-first index of today.
    val todayIndex = ((today + 5) % 7)
    val startOfWeek = java.util.Calendar.getInstance().apply {
        add(java.util.Calendar.DAY_OF_YEAR, -todayIndex)
    }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        labels.forEachIndexed { index, label ->
            val day = java.util.Calendar.getInstance().apply {
                timeInMillis = startOfWeek.timeInMillis
                add(java.util.Calendar.DAY_OF_YEAR, index)
            }
            val selected = index == todayIndex
            Column(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selected) Primary else SurfaceWhite)
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) PrimarySurface else TextTertiary
                )
                Text(
                    day.get(java.util.Calendar.DAY_OF_MONTH).toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (selected) Color.White else TextPrimary
                )
                Box(
                    Modifier
                        .size(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (selected) Color.White else Peach.copy(alpha = 0.7f))
                )
            }
        }
    }
}

@Composable
private fun DeadlineCard(
    deadline: DeadlineEntity,
    onContinue: () -> Unit,
    onDelete: () -> Unit
) {
    val accent = when (deadline.priority) {
        Priority.HIGH -> Danger
        Priority.MEDIUM -> Warning
        Priority.LOW -> Primary
    }
    val accentSurface = when (deadline.priority) {
        Priority.HIGH -> DangerSurface
        Priority.MEDIUM -> WarningSurface
        Priority.LOW -> PrimarySurface
    }

    LuminaCard(
        border = BorderStroke(1.2.dp, accent.copy(alpha = 0.45f)),
        contentPadding = PaddingValues(18.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(deadline.title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Spacer(Modifier.height(3.dp))
                Text(deadline.course, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Spacer(Modifier.width(10.dp))
            Pill(
                text = deadline.priority.label.uppercase(),
                background = accentSurface,
                contentColor = accent,
                leadingIcon = LuminaIcons.Alert
            )
        }
        Spacer(Modifier.height(14.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Icon(LuminaIcons.Clock, null, tint = accent, modifier = Modifier.size(15.dp))
            Text(
                formatDueLabel(deadline.dueAt),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = accent
            )
        }
        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Progress", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Spacer(Modifier.weight(1f))
            Text(
                "${deadline.progress}%",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TextPrimary
            )
        }
        Spacer(Modifier.height(7.dp))
        LinearMeter(progress = deadline.progress / 100f, color = accent)

        if (deadline.priority == Priority.HIGH) {
            Spacer(Modifier.height(14.dp))
            LuminaButton("Continue task", onContinue, height = 48.dp)
        } else {
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CompactAction("Focus", onContinue, modifier = Modifier.weight(1f))
                CompactAction(
                    "Remove", onDelete, modifier = Modifier.weight(1f),
                    background = SurfaceSunken, contentColor = TextSecondary
                )
            }
        }
    }
}

@Preview(name = "Plan", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun PlanScreenPreview() {
    LuminaTheme { PlanScreen(onStartFocus = {}) }
}
