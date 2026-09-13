package com.lumina.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lumina.app.data.model.Mood
import com.lumina.app.ui.theme.Danger
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.Success

/** Brand colour for each point on the mood scale. */
fun Mood.color(): Color = when (this) {
    Mood.GREAT -> Success
    Mood.GOOD -> Primary
    Mood.OKAY -> PeachDeep
    Mood.LOW -> LavenderDeep
    Mood.STRESSED -> Danger
}

/**
 * The five mood faces, drawn rather than imported.
 *
 * System emoji were rejected during design: coverage is inconsistent across devices and
 * the faces need to sit exactly on the brand palette. Drawing them keeps the scale
 * legible at any size and lets each face carry its own colour.
 */
@Composable
fun MoodFace(
    mood: Mood,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    color: Color = mood.color(),
    fillColor: Color = Color.Transparent
) {
    Canvas(modifier.size(size)) {
        val w = this.size.width
        val unit = w / 48f              // faces are authored on a 48x48 grid
        val stroke = 2.4f * unit
        val center = Offset(24f * unit, 24f * unit)
        val radius = 20.5f * unit

        if (fillColor != Color.Transparent) {
            drawCircle(fillColor, radius, center)
        }
        drawCircle(color, radius, center, style = Stroke(width = stroke))

        fun eyeDot(x: Float, y: Float, r: Float = 2f) =
            drawCircle(color, r * unit, Offset(x * unit, y * unit))

        fun arc(fromX: Float, fromY: Float, toX: Float, toY: Float, controlY: Float) {
            val path = Path().apply {
                moveTo(fromX * unit, fromY * unit)
                quadraticBezierTo(
                    ((fromX + toX) / 2f) * unit, controlY * unit,
                    toX * unit, toY * unit
                )
            }
            drawPath(path, color, style = Stroke(width = 2.6f * unit, cap = StrokeCap.Round))
        }

        when (mood) {
            Mood.GREAT -> {
                // Closed, upturned eyes plus a wide smile.
                listOf(15.5f to 21.5f, 26.5f to 21.5f).forEach { (sx, sy) ->
                    val path = Path().apply {
                        moveTo(sx * unit, sy * unit)
                        quadraticBezierTo((sx + 3f) * unit, (sy - 4f) * unit, (sx + 6f) * unit, sy * unit)
                    }
                    drawPath(path, color, style = Stroke(width = 2.4f * unit, cap = StrokeCap.Round))
                }
                arc(15f, 27.4f, 33f, 27.4f, 36.4f)
            }
            Mood.GOOD -> {
                eyeDot(18.2f, 20.4f); eyeDot(29.8f, 20.4f)
                arc(16.6f, 28.4f, 31.4f, 28.4f, 34.6f)
            }
            Mood.OKAY -> {
                eyeDot(18.2f, 20.4f); eyeDot(29.8f, 20.4f)
                drawLine(
                    color,
                    Offset(17.4f * unit, 29.6f * unit),
                    Offset(30.6f * unit, 29.6f * unit),
                    strokeWidth = 2.6f * unit,
                    cap = StrokeCap.Round
                )
            }
            Mood.LOW -> {
                eyeDot(18.2f, 20.8f); eyeDot(29.8f, 20.8f)
                arc(17f, 31.6f, 31f, 31.6f, 26f)
            }
            Mood.STRESSED -> {
                // Angled brows read as tension even in greyscale.
                drawLine(color, Offset(14.8f * unit, 16.8f * unit), Offset(20.6f * unit, 19.6f * unit),
                    strokeWidth = 2.4f * unit, cap = StrokeCap.Round)
                drawLine(color, Offset(33.2f * unit, 16.8f * unit), Offset(27.4f * unit, 19.6f * unit),
                    strokeWidth = 2.4f * unit, cap = StrokeCap.Round)
                eyeDot(18.4f, 23.4f, 1.9f); eyeDot(29.6f, 23.4f, 1.9f)
                arc(17.6f, 32.4f, 30.4f, 32.4f, 27f)
            }
        }
    }
}
