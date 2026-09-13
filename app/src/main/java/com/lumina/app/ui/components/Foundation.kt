package com.lumina.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.OverlineStyle
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
import com.lumina.app.ui.theme.TextTertiary
import androidx.compose.material3.MaterialTheme

/** The soft, slightly blue shadow used on every raised surface in the design. */
val LuminaShadow = Color(0xFF16202E).copy(alpha = 0.55f)

/**
 * Shrinks a control to 96% while it is held.
 *
 * This is the button micro-interaction from the prototype's motion spec:
 * 100% -> 96%, 180ms, ease-out.
 */
@Composable
fun Modifier.pressScale(
    interactionSource: MutableInteractionSource,
    pressedScale: Float = 0.96f
): Modifier {
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = tween(durationMillis = 180),
        label = "pressScale"
    )
    return this.graphicsLayer { scaleX = scale; scaleY = scale }
}

/** The standard raised card. Defaults match the Figma card style exactly. */
@Composable
fun LuminaCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(22.dp),
    background: Color = SurfaceWhite,
    brush: Brush? = null,
    border: BorderStroke? = null,
    elevation: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .then(
                if (onClick != null) Modifier.pressScale(interaction, 0.985f) else Modifier
            )
            .shadow(elevation, shape, clip = false, ambientColor = LuminaShadow, spotColor = LuminaShadow)
            .clip(shape)
            .then(if (brush != null) Modifier.background(brush) else Modifier.background(background))
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interaction,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .padding(contentPadding),
        content = content
    )
}

/** Rounded, tinted square holding a single icon. Used everywhere as a leading affordance. */
@Composable
fun IconChip(
    icon: ImageVector,
    tint: Color,
    background: Color,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    iconSize: Dp = 22.dp,
    cornerRadius: Dp = 15.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

/** Small rounded label, e.g. "12 days", "HIGH", "in 25 days". */
@Composable
fun Pill(
    text: String,
    background: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    border: BorderStroke? = null
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(11.dp))
            .background(background)
            .then(if (border != null) Modifier.border(border, RoundedCornerShape(11.dp)) else Modifier)
            .padding(horizontal = 11.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, null, tint = contentColor, modifier = Modifier.size(15.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}

/** Section title with an optional trailing element on the right. */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        trailing?.invoke(this)
    }
}

/** Tracked-out label above a value, e.g. "FOCUS TIME". */
@Composable
fun OverlineLabel(text: String, modifier: Modifier = Modifier, color: Color = TextTertiary) {
    Text(text = text, style = OverlineStyle, color = color, modifier = modifier)
}

@Composable
fun HairlineDivider(modifier: Modifier = Modifier, color: Color = BorderSubtle) {
    Box(modifier.fillMaxWidth().height(1.dp).background(color))
}

@Composable
fun VerticalHairline(height: Dp, modifier: Modifier = Modifier, color: Color = BorderSubtle) {
    Box(modifier.height(height).background(color))
}

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextSecondary
) {
    Text(text = text, style = MaterialTheme.typography.bodyMedium, color = color, modifier = modifier)
}

/**
 * The soft out-of-focus colour wash behind the welcome and immersive screens.
 *
 * Real Gaussian blur is expensive and only available from API 31, so the blobs are drawn
 * as radial gradients that fade to transparent. The result is visually equivalent and
 * costs nothing.
 */
@Composable
fun AuroraBackdrop(
    modifier: Modifier = Modifier,
    blobs: List<Triple<Offset, Float, Color>>
) {
    Box(
        modifier.drawBehind {
            blobs.forEach { (relativeCenter, relativeRadius, color) ->
                val center = Offset(
                    x = relativeCenter.x * size.width,
                    y = relativeCenter.y * size.height
                )
                val radius = relativeRadius * size.width
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color, color.copy(alpha = 0f)),
                        center = center,
                        radius = radius
                    ),
                    radius = radius,
                    center = center
                )
            }
        }
    )
}

@Composable
fun VSpace(height: Dp) = Spacer(Modifier.height(height))
