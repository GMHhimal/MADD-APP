package com.lumina.app.feature.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel as composeViewModel
import com.lumina.app.LuminaViewModelFactory
import com.lumina.app.preview.PreviewData
import com.lumina.app.ui.components.AuroraBackdrop
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.components.ProgressRing
import com.lumina.app.ui.formatDuration
import com.lumina.app.ui.formatMinutes
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.nowMinutes
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.NightBottom
import com.lumina.app.ui.theme.NightMid
import com.lumina.app.ui.theme.NightTextSoft
import com.lumina.app.ui.theme.NightTop
import com.lumina.app.ui.theme.NumericDisplay

@Composable
fun FocusModeScreen(
    taskTitle: String,
    onExit: () -> Unit,
    viewModel: FocusViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: FocusViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.focusState.copy(taskTitle = taskTitle)
    else runtimeViewModel!!.state.collectAsStateWithLifecycle().value
    val sessionsToday = if (inspectionMode) 2
    else runtimeViewModel!!.sessionsToday.collectAsStateWithLifecycle().value
    val minutesToday = if (inspectionMode) 48
    else runtimeViewModel!!.minutesToday.collectAsStateWithLifecycle().value

    LaunchedEffect(taskTitle, inspectionMode) {
        if (!inspectionMode) runtimeViewModel?.startFor(taskTitle)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFF12233F), Color(0xFF24457A), Color(0xFF3C6AA8))))
    ) {
        AuroraBackdrop(
            modifier = Modifier.fillMaxSize(),
            blobs = listOf(
                Triple(Offset(0.05f, 0.22f), 0.62f, Color(0xFF5E9BE0).copy(alpha = 0.42f)),
                Triple(Offset(0.88f, 0.70f), 0.58f, Color(0xFF86A8E8).copy(alpha = 0.34f))
            )
        )

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Row(
                Modifier
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color.White.copy(alpha = 0.16f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Icon(LuminaIcons.BellOff, null, tint = Color.White, modifier = Modifier.size(16.dp))
                Text("Notifications paused", style = MaterialTheme.typography.labelMedium, color = Color.White)
            }

            Spacer(Modifier.weight(0.8f))

            ProgressRing(
                progress = state.progress,
                size = 268.dp,
                strokeWidth = 14.dp,
                color = Color.White.copy(alpha = 0.95f),
                trackColor = Color.White.copy(alpha = 0.16f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        formatClock(state.remainingSeconds),
                        style = NumericDisplay.copy(fontSize = 58.sp),
                        color = Color.White
                    )
                    Text(
                        "of ${formatClock(state.totalSeconds)} remaining",
                        style = MaterialTheme.typography.labelMedium,
                        color = NightTextSoft
                    )
                }
            }

            Spacer(Modifier.height(28.dp))
            Text(state.taskTitle, style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(Modifier.height(5.dp))
            Text(
                "Ends at ${formatMinutes(nowMinutes() + state.remainingSeconds / 60)}",
                style = MaterialTheme.typography.labelMedium,
                color = NightTextSoft
            )

            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FocusStat("$sessionsToday", "sessions today", Modifier.weight(1f))
                FocusStat(formatDuration(minutesToday), "focused", Modifier.weight(1f))
                FocusStat(
                    formatMinutes(nowMinutes() + state.remainingSeconds / 60),
                    "ends at",
                    Modifier.weight(1f)
                )
            }

            Spacer(Modifier.weight(1f))

            if (state.finished) {
                Text(
                    "Session done. That is 25 minutes your future self will not have to find.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                LuminaButton("Back to my plan", onExit, height = 58.dp)
            } else {
                LuminaButton(
                    text = if (state.running) "Pause focus" else "Resume focus",
                    onClick = { runtimeViewModel?.togglePause() },
                    leadingIcon = if (state.running) LuminaIcons.Pause else null,
                    style = LuminaButtonStyle.Neutral,
                    height = 58.dp
                )
                Spacer(Modifier.height(12.dp))
                LuminaButton(
                    text = "End session early",
                    onClick = {
                        runtimeViewModel?.endEarly()
                        onExit()
                    },
                    style = LuminaButtonStyle.Ghost,
                    height = 52.dp
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "Lumina is holding your alerts. Nothing urgent will be missed.",
                style = MaterialTheme.typography.labelSmall,
                color = NightTextSoft,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FocusStat(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(17.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = NightTextSoft)
    }
}

private fun formatClock(seconds: Int): String = "%02d:%02d".format(seconds / 60, seconds % 60)

@Preview(name = "Focus Mode", widthDp = 412, heightDp = 915)
@Composable
private fun FocusModeScreenPreview() {
    LuminaTheme { FocusModeScreen(taskTitle = "SE Assignment", onExit = {}) }
}
