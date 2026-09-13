package com.lumina.app.feature.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas as DrawCanvas
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.preview.PreviewData
import com.lumina.app.ui.components.IconChip
import com.lumina.app.ui.components.LuminaCard
import com.lumina.app.ui.components.LuminaVoiceBrush
import com.lumina.app.ui.components.Pill
import com.lumina.app.ui.components.SectionHeader
import com.lumina.app.ui.components.SegmentedControl
import com.lumina.app.ui.components.StatTile
import com.lumina.app.ui.formatDuration
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Lavender
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.LavenderSurface
import com.lumina.app.ui.theme.MintSurface
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.PeachSurface
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.Success
import com.lumina.app.ui.theme.SuccessSurface
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: ProgressViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.progressState
    else runtimeViewModel!!.uiState.collectAsStateWithLifecycle().value

    Box(Modifier.fillMaxSize().background(Canvas)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Text("Your Progress", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
            }

            item {
                SegmentedControl(
                    options = RangeFilter.entries.map { it.label },
                    selectedIndex = RangeFilter.entries.indexOf(state.range),
                    onSelect = { runtimeViewModel?.setRange(RangeFilter.entries[it]) }
                )
            }

            item {
                LuminaCard(shape = RoundedCornerShape(24.dp), contentPadding = PaddingValues(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Mood balance", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            Text(
                                "Last ${state.range.days} days",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                        Pill(
                            text = if (state.goodDays >= state.lowDays) "Mostly positive" else "A tougher stretch",
                            background = if (state.goodDays >= state.lowDays) SuccessSurface else LavenderSurface,
                            contentColor = if (state.goodDays >= state.lowDays) Success else LavenderDeep
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    MoodTrendChart(state.moodTrend)
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        LegendDot("${state.goodDays} good", Primary)
                        LegendDot("${state.lowDays} low", Lavender)
                        LegendDot("${state.neutralDays} neutral", BorderSubtle)
                    }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        "HABIT CONSISTENCY", "${state.habitConsistency}%",
                        LuminaIcons.CheckCircle, Success, SuccessSurface, Modifier.weight(1f),
                        caption = "of today's habits"
                    )
                    StatCard(
                        "FOCUS TIME", formatDuration(state.focusMinutes),
                        LuminaIcons.Target, PrimaryDeep, PrimarySurface, Modifier.weight(1f),
                        caption = "this ${state.range.label.lowercase()}"
                    )
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        "GYM SESSIONS", "${state.gymSessions}",
                        LuminaIcons.Dumbbell, LavenderDeep, LavenderSurface, Modifier.weight(1f),
                        caption = "logged"
                    )
                    StatCard(
                        "DEADLINES", "${state.deadlinesDone} done",
                        LuminaIcons.Calendar, PeachDeep, PeachSurface, Modifier.weight(1f),
                        caption = "${state.deadlinesOpen} still open"
                    )
                }
            }

            item { SectionHeader("What Lumina noticed") }

            items(state.insights.size) { index ->
                val (title, body) = state.insights[index]
                LuminaCard(
                    brush = LuminaVoiceBrush,
                    border = BorderStroke(1.1.dp, Lavender.copy(alpha = 0.8f)),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Row {
                        Icon(LuminaIcons.Sparkle, null, tint = LavenderDeep, modifier = Modifier.size(19.dp))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                title,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = PrimaryDeep
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(body, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Mood trend, drawn directly on a Canvas.
 *
 * A charting library would be a heavy dependency for one line, and drawing it here keeps
 * the curve on the brand palette.
 */
@Composable
private fun MoodTrendChart(scores: List<Int>) {
    if (scores.size < 2) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(104.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Canvas),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Log a few more days to see your trend",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
        return
    }

    DrawCanvas(Modifier.fillMaxWidth().height(104.dp)) {
        val width = size.width
        val height = size.height
        val minScore = 1f
        val maxScore = 5f
        val stepX = width / (scores.size - 1).coerceAtLeast(1)

        fun pointFor(index: Int): Offset {
            val normalised = (scores[index] - minScore) / (maxScore - minScore)
            return Offset(index * stepX, height - 14f - normalised * (height - 34f))
        }

        // Gridlines give the curve something to read against.
        listOf(0.25f, 0.5f, 0.75f).forEach { fraction ->
            drawLine(
                color = BorderSubtle,
                start = Offset(0f, height * fraction),
                end = Offset(width, height * fraction),
                strokeWidth = 1.4f
            )
        }

        val linePath = Path().apply {
            moveTo(pointFor(0).x, pointFor(0).y)
            for (index in 1 until scores.size) lineTo(pointFor(index).x, pointFor(index).y)
        }
        val areaPath = Path().apply {
            addPath(linePath)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = areaPath,
            brush = Brush.verticalGradient(
                listOf(Primary.copy(alpha = 0.30f), Primary.copy(alpha = 0f))
            )
        )
        drawPath(linePath, Primary, style = Stroke(width = 2.6f, cap = StrokeCap.Round))

        scores.indices.forEach { index ->
            val point = pointFor(index)
            drawCircle(Color.White, radius = 4.6f, center = point)
            drawCircle(Primary, radius = 4.6f, center = point, style = Stroke(width = 2.4f))
        }
    }
}

@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    tintSurface: Color,
    modifier: Modifier = Modifier,
    caption: String
) {
    LuminaCard(modifier = modifier, shape = RoundedCornerShape(20.dp), contentPadding = PaddingValues(0.dp)) {
        StatTile(
            label = label,
            value = value,
            icon = icon,
            tint = tint,
            tintBackground = tintSurface,
            caption = caption
        )
    }
}

@Preview(name = "Progress", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun ProgressScreenPreview() {
    LuminaTheme { ProgressScreen() }
}
