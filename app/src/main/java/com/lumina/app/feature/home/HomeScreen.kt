package com.lumina.app.feature.home

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.data.model.EventKind
import com.lumina.app.preview.PreviewData
import com.lumina.app.ui.components.HairlineDivider
import com.lumina.app.ui.components.IconChip
import com.lumina.app.ui.components.InsightCard
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.Pill
import com.lumina.app.ui.components.SectionHeader
import com.lumina.app.ui.components.StatTile
import com.lumina.app.ui.components.TimelineRow
import com.lumina.app.ui.components.VerticalHairline
import com.lumina.app.ui.formatDuration
import com.lumina.app.ui.formatMinutes
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.nowMinutes
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.LavenderSurface
import com.lumina.app.ui.theme.MintDeep
import com.lumina.app.ui.theme.MintSurface
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.PeachSurface
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.Success
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
//for commits
//.......
@Composable
fun HomeScreen(
    onOpenMood: () -> Unit,
    onOpenHabits: () -> Unit,
    onOpenPlan: () -> Unit,
    onOpenFreeTime: () -> Unit,
    onStartFocus: () -> Unit,
    viewModel: HomeViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: HomeViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.homeState
    else runtimeViewModel!!.uiState.collectAsStateWithLifecycle().value
    val now = nowMinutes()

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Color(0xFFE2EFFC),
                    0.22f to Canvas,
                    1f to Canvas
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item { HomeHeader(state) }

            item {
                Pill(
                    text = state.balanceLabel,
                    background = MintSurface,
                    contentColor = MintDeep,
                    leadingIcon = LuminaIcons.CheckCircle
                )
            }

            item { SummaryCard(state, onOpenMood, onOpenHabits, onOpenPlan, onOpenFreeTime) }

            state.insight?.let { insight ->
                item {
                    InsightCard(
                        title = insight.title,
                        body = insight.body,
                        primaryActionLabel = insight.primaryAction,
                        onPrimaryAction = {
                            when (insight.primaryAction) {
                                "Use this plan", "Start focus" -> onStartFocus()
                                "See options" -> onOpenFreeTime()
                                "View habits" -> onOpenHabits()
                            }
                        },
                        secondaryActionLabel = insight.secondaryAction,
                        onSecondaryAction = onOpenPlan
                    )
                }
            }

            item {
                SectionHeader("Today's Timeline") {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = onOpenPlan)
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "See all",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = PrimaryDeep
                        )
                        Icon(LuminaIcons.ChevronRight, null, tint = PrimaryDeep, modifier = Modifier.size(14.dp))
                    }
                }
            }

            itemsIndexed(state.timeline, key = { _, event -> event.id }) { index, event ->
                TimelineRow(
                    time = formatMinutes(event.startMinutes),
                    title = event.title,
                    meta = event.meta,
                    kind = event.kind,
                    isNow = now in event.startMinutes until event.endMinutes,
                    showConnector = index != state.timeline.lastIndex,
                    onClick = if (event.kind == EventKind.FREE) onOpenFreeTime else onOpenPlan
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(state: HomeUiState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = if (state.name.isBlank()) state.greeting else "${state.greeting}, ${state.name}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary
                )
                Icon(LuminaIcons.Sun, null, tint = PeachDeep, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(3.dp))
            Text(state.dateLabel, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        }
        Spacer(Modifier.width(12.dp))
        Box(
            Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF7FB9EE), Primary))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                state.name.take(1).uppercase().ifBlank { "L" },
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun SummaryCard(
    state: HomeUiState,
    onOpenMood: () -> Unit,
    onOpenHabits: () -> Unit,
    onOpenPlan: () -> Unit,
    onOpenFreeTime: () -> Unit
) {
    LuminaCard(
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            StatTile(
                label = "MOOD",
                value = state.mood?.label ?: "Not logged",
                icon = LuminaIcons.Smile,
                tint = PeachDeep,
                tintBackground = PeachSurface,
                modifier = Modifier.weight(1f),
                onClick = onOpenMood
            )
            VerticalHairline(height = 118.dp)
            StatTile(
                label = "HABITS",
                value = "${state.habitsDone} / ${state.habitsTotal}",
                icon = LuminaIcons.CheckCircle,
                tint = Success,
                tintBackground = MintSurface,
                modifier = Modifier.weight(1f),
                onClick = onOpenHabits
            )
        }
        HairlineDivider()
        Row(Modifier.fillMaxWidth()) {
            StatTile(
                label = "FOCUS TIME",
                value = if (state.focusMinutes == 0) "—" else formatDuration(state.focusMinutes),
                icon = LuminaIcons.Target,
                tint = PrimaryDeep,
                tintBackground = PrimarySurface,
                modifier = Modifier.weight(1f),
                onClick = onOpenPlan
            )
            VerticalHairline(height = 118.dp)
            StatTile(
                label = "FREE TIME",
                value = if (state.freeMinutes == 0) "—" else formatDuration(state.freeMinutes),
                icon = LuminaIcons.Clock,
                tint = LavenderDeep,
                tintBackground = LavenderSurface,
                modifier = Modifier.weight(1f),
                onClick = onOpenFreeTime
            )
        }
    }
}

@Preview(name = "Home", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun HomeScreenPreview() {
    LuminaTheme {
        HomeScreen(
            onOpenMood = {},
            onOpenHabits = {},
            onOpenPlan = {},
            onOpenFreeTime = {},
            onStartFocus = {}
        )
    }
}
