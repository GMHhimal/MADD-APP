package com.lumina.app.feature.mood

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.data.model.Mood
import com.lumina.app.preview.PreviewData
import com.lumina.app.feature.onboarding.WrapRow
import com.lumina.app.ui.components.CircleIconButton
import com.lumina.app.ui.components.CompactAction
import com.lumina.app.ui.components.DisclaimerNote
import com.lumina.app.ui.components.IconChip
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.LuminaVoiceBrush
import com.lumina.app.ui.components.MoodFace
import com.lumina.app.ui.components.SectionHeader
import com.lumina.app.ui.components.SelectableChip
import com.lumina.app.ui.components.color
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Lavender
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.Mint
import com.lumina.app.ui.theme.MintDeep
import com.lumina.app.ui.theme.MintSurface
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.PeachSurface
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.Success
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary

private val Causes = listOf(
    "Study", "Work", "Sleep", "Relationships", "Health", "Money", "No specific reason"
)

@Composable
fun MoodScreen(
    onBack: () -> Unit,
    onStartBreathing: () -> Unit,
    onOpenFreeTime: () -> Unit,
    viewModel: MoodViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: MoodViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.moodState
    else runtimeViewModel!!.uiState.collectAsStateWithLifecycle().value

    Box(Modifier.fillMaxSize().background(Canvas)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircleIconButton(LuminaIcons.ChevronLeft, "Back", onBack)
                    Spacer(Modifier.width(12.dp))
                    Text("Wellness", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text(
                        "How are you feeling today?",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary
                    )
                    Text(
                        "One tap. No pressure. Only you can see this.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Mood.entries.forEach { mood ->
                        MoodOption(
                            mood = mood,
                            selected = state.selectedMood == mood,
                            onClick = { runtimeViewModel?.selectMood(mood) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (state.loggedToday) {
                item {
                    LuminaCard(
                        background = MintSurface,
                        border = BorderStroke(1.dp, Mint.copy(alpha = 0.5f)),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconChip(
                                LuminaIcons.Heart, Success,
                                SurfaceWhite.copy(alpha = 0.85f),
                                size = 40.dp, iconSize = 20.dp, cornerRadius = 14.dp
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Thanks for checking in.",
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                    color = MintDeep
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "That's ${state.streakDays} " +
                                        "${if (state.streakDays == 1) "day" else "days"} in a row you've logged how you feel.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Anything affecting your mood?",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    WrapRow(spacing = 9.dp) {
                        Causes.forEach { cause ->
                            SelectableChip(
                                label = cause,
                                selected = cause in state.selectedCauses,
                                showCheckWhenSelected = false,
                                onClick = { runtimeViewModel?.toggleCause(cause) }
                            )
                        }
                    }
                }
            }

            item { SectionHeader("Recommended for you") }

            item {
                LuminaCard(
                    brush = LuminaVoiceBrush,
                    border = BorderStroke(1.2.dp, Lavender),
                    contentPadding = PaddingValues(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconChip(
                            LuminaIcons.Wave, LavenderDeep,
                            SurfaceWhite.copy(alpha = 0.9f),
                            size = 50.dp, iconSize = 25.dp, cornerRadius = 17.dp
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "5-minute calm breathing",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                "Slow your breathing and reset",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        CompactAction(
                            "Start", onStartBreathing,
                            background = Primary, contentColor = SurfaceWhite
                        )
                    }
                }
            }

            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AlternativeCard("Take a walk", LuminaIcons.Walk, Success, MintSurface, Modifier.weight(1f), onOpenFreeTime)
                    AlternativeCard("Listen to music", LuminaIcons.Music, PeachDeep, PeachSurface, Modifier.weight(1f), onOpenFreeTime)
                    AlternativeCard("Talk to someone", LuminaIcons.Users, PrimaryDeep, PrimarySurface, Modifier.weight(1f), onOpenFreeTime)
                }
            }

            item {
                DisclaimerNote(
                    title = null,
                    body = "Lumina supports everyday wellbeing. It does not diagnose or provide medical advice."
                )
            }
        }
    }
}

@Composable
private fun MoodOption(
    mood: Mood,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = mood.color()
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier
            .clip(shape)
            .background(if (selected) accent.copy(alpha = 0.12f) else SurfaceWhite)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) accent else BorderSubtle,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MoodFace(
            mood = mood,
            size = 40.dp,
            color = if (selected) accent else TextSecondary,
            fillColor = if (selected) SurfaceWhite else androidx.compose.ui.graphics.Color.Transparent
        )
        // The label is what makes the scale readable without relying on the drawing.
        Text(
            mood.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = if (selected) accent else TextSecondary
        )
    }
}

@Composable
private fun AlternativeCard(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: androidx.compose.ui.graphics.Color,
    tintSurface: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    LuminaCard(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(13.dp),
        onClick = onClick
    ) {
        IconChip(icon, tint, tintSurface, size = 36.dp, iconSize = 19.dp, cornerRadius = 12.dp)
        Spacer(Modifier.height(9.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = TextPrimary
        )
    }
}

@Preview(name = "Mood", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun MoodScreenPreview() {
    LuminaTheme {
        MoodScreen(onBack = {}, onStartBreathing = {}, onOpenFreeTime = {})
    }
}
