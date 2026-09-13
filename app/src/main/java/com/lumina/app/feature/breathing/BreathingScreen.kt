package com.lumina.app.feature.breathing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.lumina.app.ui.components.CircleIconButton
import com.lumina.app.ui.components.LinearMeter
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.NightBottom
import com.lumina.app.ui.theme.NightMid
import com.lumina.app.ui.theme.NightTextSoft
import com.lumina.app.ui.theme.NightTop
import com.lumina.app.ui.theme.NumericDisplay

@Composable
fun BreathingScreen(
    onClose: () -> Unit,
    viewModel: BreathingViewModel? = null
) {
    val inspectionMode = LocalInspectionMode.current
    val runtimeViewModel: BreathingViewModel? = if (inspectionMode) null
    else viewModel ?: composeViewModel(factory = LuminaViewModelFactory)
    val state = if (inspectionMode) PreviewData.breathingState
    else runtimeViewModel!!.state.collectAsStateWithLifecycle().value

    // The circle is the timer: it grows on the inhale, holds, then contracts.
    val targetScale = when (state.phase) {
        BreathPhase.INHALE -> 1f
        BreathPhase.HOLD -> 1f
        BreathPhase.EXHALE -> 0.72f
    }
    val scale by animateFloatAsState(
        targetValue = if (state.running) targetScale else 0.9f,
        animationSpec = tween(durationMillis = state.phase.seconds * 1000),
        label = "breathScale"
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(NightTop, NightMid, NightBottom)))
    ) {
        AuroraBackdrop(
            modifier = Modifier.fillMaxSize(),
            blobs = listOf(
                Triple(Offset(0.05f, 0.18f), 0.65f, Color(0xFF6F8FE0).copy(alpha = 0.45f)),
                Triple(Offset(0.90f, 0.62f), 0.60f, Color(0xFF9C86D8).copy(alpha = 0.40f)),
                Triple(Offset(0.20f, 0.85f), 0.55f, Color(0xFF5FA8D8).copy(alpha = 0.32f))
            )
        )

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(Modifier.fillMaxWidth().padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.14f))
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFFA9D0F2)))
                    Text(
                        "Calm breathing",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White
                    )
                }
                Spacer(Modifier.weight(1f))
                CircleIconButton(
                    icon = LuminaIcons.Close,
                    contentDescription = "End session",
                    onClick = onClose,
                    background = Color.White.copy(alpha = 0.14f),
                    tint = Color.White,
                    elevation = 0.dp
                )
            }

            Spacer(Modifier.weight(1f))

            Box(Modifier.fillMaxWidth().height(320.dp), contentAlignment = Alignment.Center) {
                listOf(320.dp to 0.16f, 260.dp to 0.24f, 196.dp to 0.34f).forEach { (ringSize, alpha) ->
                    Box(
                        Modifier
                            .size(ringSize)
                            .border(1.6.dp, Color.White.copy(alpha = alpha), CircleShape)
                    )
                }
                Box(
                    Modifier
                        .size(196.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFFBFDCFA).copy(alpha = 0.95f),
                                    Color(0xFF9C8FE0).copy(alpha = 0.85f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            state.phase.label,
                            style = MaterialTheme.typography.titleLarge,
                            color = NightTop.copy(alpha = 0.85f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            state.secondsLeftInPhase.toString(),
                            style = NumericDisplay.copy(fontSize = 56.sp),
                            color = NightTop
                        )
                        Text("seconds", style = MaterialTheme.typography.labelSmall, color = NightMid)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                BreathPhase.entries.forEach { phase ->
                    PhaseChip(
                        label = phase.label,
                        duration = "${phase.seconds}s",
                        active = state.phase == phase,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Session", style = MaterialTheme.typography.labelSmall, color = NightTextSoft)
                Spacer(Modifier.weight(1f))
                Text(
                    "${formatClock(state.elapsedSeconds)} / ${formatClock(state.totalSeconds)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(9.dp))
            LinearMeter(
                progress = state.progress,
                color = Color.White.copy(alpha = 0.92f),
                trackColor = Color.White.copy(alpha = 0.18f),
                height = 6.dp,
                animate = false
            )

            Spacer(Modifier.weight(1f))

            if (state.finished) {
                Text(
                    "Session complete. Notice how your shoulders feel now.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)
                )
                LuminaButton("Start again", onClick = { runtimeViewModel?.restart() }, height = 58.dp)
            } else {
                LuminaButton(
                    text = if (state.running) "Pause" else "Resume",
                    onClick = { runtimeViewModel?.togglePause() },
                    leadingIcon = if (state.running) LuminaIcons.Pause else null,
                    style = LuminaButtonStyle.Neutral,
                    height = 58.dp
                )
            }
            Spacer(Modifier.height(12.dp))
            LuminaButton("End session", onClose, style = LuminaButtonStyle.Ghost, height = 52.dp)

            Spacer(Modifier.height(14.dp))
            Text(
                "Follow the circle. Let your shoulders drop.",
                style = MaterialTheme.typography.labelSmall,
                color = NightTextSoft,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PhaseChip(
    label: String,
    duration: String,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = if (active) 0.20f else 0.08f))
            .then(
                if (active) Modifier.border(1.4.dp, Color.White.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
                else Modifier
            )
            .padding(vertical = 11.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White)
        Text(duration, style = MaterialTheme.typography.labelSmall, color = NightTextSoft)
    }
}

private fun formatClock(seconds: Int): String =
    "%02d:%02d".format(seconds / 60, seconds % 60)

@Preview(name = "Breathing", widthDp = 412, heightDp = 915)
@Composable
private fun BreathingScreenPreview() {
    LuminaTheme { BreathingScreen(onClose = {}) }
}
