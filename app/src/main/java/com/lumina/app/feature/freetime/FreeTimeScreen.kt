package com.lumina.app.feature.freetime

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.data.repository.SmartEngine
import com.lumina.app.preview.PreviewData
import com.lumina.app.ui.components.CircleIconButton
import com.lumina.app.ui.components.CompactAction
import com.lumina.app.ui.components.IconChip
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.Pill
import com.lumina.app.ui.components.SectionHeader
import com.lumina.app.ui.formatDuration
import com.lumina.app.ui.formatRange
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.Peach
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.SurfaceSunken
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary

@Composable
fun FreeTimeScreen(
    onBack: () -> Unit,
    onStartFocus: (String) -> Unit,
    onStartBreathing: () -> Unit,
    viewModel: FreeTimeViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: FreeTimeViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.freeTimeState
    else runtimeViewModel!!.uiState.collectAsStateWithLifecycle().value

    Box(Modifier.fillMaxSize().background(Canvas)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircleIconButton(LuminaIcons.ChevronLeft, "Back", onBack)
                    Spacer(Modifier.width(12.dp))
                    Text("Free Time", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
                }
            }

            item {
                LuminaCard(
                    shape = RoundedCornerShape(26.dp),
                    brush = Brush.linearGradient(listOf(Color(0xFFFFE8D6), Color(0xFFE6F0FC))),
                    border = BorderStroke(1.2.dp, Peach.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconChip(
                            LuminaIcons.Clock, PeachDeep,
                            SurfaceWhite.copy(alpha = 0.9f),
                            size = 52.dp, iconSize = 26.dp, cornerRadius = 18.dp
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = state.slot
                                    ?.let { "${formatDuration(it.durationMinutes)} free" }
                                    ?: "No gaps right now",
                                style = MaterialTheme.typography.headlineLarge,
                                color = TextPrimary
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = state.slot
                                    ?.let { "${formatRange(it.startMinutes, it.endMinutes)} · nothing scheduled" }
                                    ?: "Your day is fully booked. That is fine too.",
                                style = MaterialTheme.typography.labelMedium,
                                color = PeachDeep
                            )
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceWhite.copy(alpha = 0.75f))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(LuminaIcons.Sparkle, null, tint = LavenderDeep, modifier = Modifier.size(16.dp))
                        Text(
                            "Picked from your interests and today's deadlines.",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            item {
                SectionHeader("What fits right now") {
                    Text(
                        "${state.activities.size} options",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }

            items(state.activities, key = { it.title }) { activity ->
                ActivityCard(
                    activity = activity,
                    onAction = {
                        when {
                            activity.iconKey == "target" -> onStartFocus(activity.title)
                            activity.iconKey == "meditation" -> onStartBreathing()
                            else -> runtimeViewModel?.logActivityAsFocus(activity.title, activity.minutes)
                        }
                    }
                )
            }

            item {
                LuminaButton(
                    text = "Surprise me",
                    onClick = { runtimeViewModel?.surpriseMe() },
                    style = LuminaButtonStyle.Secondary,
                    leadingIcon = LuminaIcons.Dice,
                    height = 52.dp
                )
            }
        }
    }
}

@Composable
private fun ActivityCard(
    activity: SmartEngine.Activity,
    onAction: () -> Unit
) {
    LuminaCard(
        background = if (activity.recommended) PrimarySurface.copy(alpha = 0.55f) else SurfaceWhite,
        border = if (activity.recommended) BorderStroke(1.8.dp, Primary) else null,
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconChip(
                icon = LuminaIcons.byKey(activity.iconKey),
                tint = PrimaryDeep,
                background = if (activity.recommended) SurfaceWhite else PrimarySurface,
                size = 48.dp, iconSize = 23.dp, cornerRadius = 16.dp
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                if (activity.recommended) {
                    Pill("RECOMMENDED", Primary, Color.White)
                    Spacer(Modifier.height(6.dp))
                }
                Text(
                    activity.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    activity.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceSunken)
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        formatDuration(activity.minutes),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TextSecondary
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            CompactAction(
                text = activity.actionLabel,
                onClick = onAction,
                background = if (activity.recommended) Primary else PrimarySurface,
                contentColor = if (activity.recommended) Color.White else PrimaryDeep
            )
        }
    }
}

@Preview(name = "Free Time", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun FreeTimeScreenPreview() {
    LuminaTheme {
        FreeTimeScreen(onBack = {}, onStartFocus = {}, onStartBreathing = {})
    }
}
