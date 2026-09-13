package com.lumina.app.feature.health

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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.preview.PreviewData
import com.lumina.app.data.local.HealthReminderEntity
import com.lumina.app.ui.components.CircleIconButton
import com.lumina.app.ui.components.CompactAction
import com.lumina.app.ui.components.DisclaimerNote
import com.lumina.app.ui.components.EmptyState
import com.lumina.app.ui.components.IconChip
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.LuminaFab
import com.lumina.app.ui.components.Pill
import com.lumina.app.ui.components.SectionHeader
import com.lumina.app.ui.daysUntil
import com.lumina.app.ui.formatDate
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Danger
import com.lumina.app.ui.theme.DangerSurface
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.LavenderSurface
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.PeachSurface
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.SurfaceSunken
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
import com.lumina.app.ui.theme.Warning
import com.lumina.app.ui.theme.WarningSurface

@Composable
fun HealthScreen(
    onBack: () -> Unit,
    viewModel: HealthViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: HealthViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.healthState
    else runtimeViewModel!!.uiState.collectAsStateWithLifecycle().value
    var showSheet by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize().background(Canvas)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircleIconButton(LuminaIcons.ChevronLeft, "Back", onBack)
                    Spacer(Modifier.width(12.dp))
                    Text("Health Reminders", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                }
            }

            item {
                DisclaimerNote(
                    title = "You control these reminders",
                    body = "Lumina only reminds you of checkups you add. It does not diagnose or give medical advice.",
                    icon = LuminaIcons.Shield
                )
            }

            val nextUp = state.nextUp
            if (nextUp == null && !state.loading) {
                item {
                    EmptyState(
                        icon = LuminaIcons.HeartPulse,
                        title = "No checkups tracked yet",
                        body = "Add reminders for tests or checkups you already do. Lumina will never diagnose. It just remembers for you.",
                        actionLabel = "Add health reminder",
                        onAction = { showSheet = true },
                        accent = PeachDeep,
                        accentSurface = PeachSurface
                    )
                }
            } else if (nextUp != null) {
                item { NextUpCard(nextUp, onMarkBooked = { runtimeViewModel?.markBooked(nextUp) }) }

                if (state.reminders.isNotEmpty()) {
                    item {
                        SectionHeader("All reminders") {
                            Text(
                                "${state.reminders.size + 1} tracked",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                    items(state.reminders, key = { it.id }) { reminder ->
                        ReminderCard(
                            reminder = reminder,
                            onMarkBooked = { runtimeViewModel?.markBooked(reminder) },
                            onDelete = { runtimeViewModel?.delete(reminder) }
                        )
                    }
                }
            }
        }

        LuminaFab(
            text = "Add Reminder",
            icon = LuminaIcons.Plus,
            onClick = { showSheet = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 24.dp, bottom = 24.dp)
        )
    }

    if (showSheet) {
        AddHealthReminderSheet(
            onDismiss = { showSheet = false },
            onSave = { title, icon, days, repeat, note, notify ->
                runtimeViewModel?.addReminder(title, icon, days, repeat, note, notify)
                showSheet = false
            }
        )
    }
}

@Composable
private fun NextUpCard(reminder: HealthReminderEntity, onMarkBooked: () -> Unit) {
    val days = daysUntil(reminder.nextDueAt)
    LuminaCard(
        shape = RoundedCornerShape(24.dp),
        brush = Brush.linearGradient(listOf(Color(0xFFFDEBE9), PrimarySurface)),
        border = BorderStroke(1.2.dp, Danger.copy(alpha = 0.4f)),
        contentPadding = PaddingValues(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Pill("NEXT UP", Danger, Color.White)
            Spacer(Modifier.weight(1f))
            Text(
                "in $days days",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Danger
            )
        }
        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconChip(
                LuminaIcons.byKey(reminder.iconKey), Danger,
                SurfaceWhite.copy(alpha = 0.9f), size = 50.dp, iconSize = 25.dp, cornerRadius = 17.dp
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(reminder.title, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                Spacer(Modifier.height(3.dp))
                Text(
                    "Due ${formatDate(reminder.nextDueAt)}" +
                        if (reminder.repeatMonths > 0) " · every ${reminder.repeatMonths} months" else "",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }
        }
        if (reminder.note.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(reminder.note, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            LuminaButton("Mark as booked", onMarkBooked, Modifier.weight(1f), height = 46.dp)
            LuminaButton("Reschedule", onMarkBooked, Modifier.weight(1f), LuminaButtonStyle.Secondary, height = 46.dp)
        }
    }
}

@Composable
private fun ReminderCard(
    reminder: HealthReminderEntity,
    onMarkBooked: () -> Unit,
    onDelete: () -> Unit
) {
    val days = daysUntil(reminder.nextDueAt)
    val urgent = days <= 30
    LuminaCard(contentPadding = PaddingValues(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconChip(
                LuminaIcons.byKey(reminder.iconKey),
                if (urgent) Warning else PrimaryDeep,
                if (urgent) WarningSurface else PrimarySurface,
                size = 44.dp
            )
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(reminder.title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Spacer(Modifier.height(3.dp))
                Text(
                    "Next: ${formatDate(reminder.nextDueAt)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            Spacer(Modifier.width(10.dp))
            Pill(
                text = "in $days days",
                background = if (urgent) WarningSurface else SurfaceSunken,
                contentColor = if (urgent) Warning else TextSecondary
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (reminder.repeatMonths > 0) {
                Row(
                    Modifier
                        .background(SurfaceSunken, RoundedCornerShape(9.dp))
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(LuminaIcons.Repeat, null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Text(
                        "Every ${reminder.repeatMonths} months",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
            reminder.lastDoneAt?.let { last ->
                Row(
                    Modifier
                        .background(SurfaceSunken, RoundedCornerShape(9.dp))
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(LuminaIcons.Calendar, null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Text(
                        "Last: ${formatDate(last, "d MMM yyyy")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CompactAction("Mark done", onMarkBooked, Modifier.weight(1f))
            CompactAction(
                "Remove", onDelete, Modifier.weight(1f),
                background = SurfaceSunken, contentColor = TextSecondary
            )
        }
    }
}

@Preview(name = "Health", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HealthScreenPreview() {
    LuminaTheme { HealthScreen(onBack = {}) }
}
