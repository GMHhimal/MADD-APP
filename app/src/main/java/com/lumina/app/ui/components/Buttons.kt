package com.lumina.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lumina.app.ui.theme.Dimens
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySoft
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.SurfaceSunken
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary

enum class LuminaButtonStyle { Primary, Secondary, Soft, Neutral, Ghost }

/**
 * The Button component from the design system.
 *
 * 56dp tall, 18dp radius. Holding it scales to 96% over 180ms, which is the
 * "while pressing" interaction wired into the Figma prototype.
 */
@Composable
fun LuminaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: LuminaButtonStyle = LuminaButtonStyle.Primary,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    height: Dp = Dimens.buttonHeight
) {
    val interaction = remember { MutableInteractionSource() }

    val background: Color
    val content: Color
    var border: BorderStroke? = null
    when (style) {
        LuminaButtonStyle.Primary -> {
            background = Primary; content = Color.White
        }
        LuminaButtonStyle.Secondary -> {
            background = Color.Transparent; content = PrimaryDeep
            border = BorderStroke(1.5.dp, PrimarySoft)
        }
        LuminaButtonStyle.Soft -> {
            background = PrimarySurface; content = PrimaryDeep
        }
        LuminaButtonStyle.Neutral -> {
            background = SurfaceSunken; content = TextPrimary
        }
        LuminaButtonStyle.Ghost -> {
            background = Color.Transparent; content = PrimaryDeep
        }
    }

    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .pressScale(interaction)
            .then(
                if (style == LuminaButtonStyle.Primary && enabled) {
                    Modifier.shadow(10.dp, shape, clip = false, ambientColor = Primary, spotColor = Primary)
                } else Modifier
            )
            .clip(shape)
            .background(background)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .clickable(
                interactionSource = interaction,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .alpha(if (enabled) 1f else 0.45f)
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, null, tint = content, modifier = Modifier.size(20.dp))
            androidx.compose.foundation.layout.Spacer(Modifier.size(9.dp))
        }
        Text(text = text, style = MaterialTheme.typography.titleMedium, color = content)
    }
}

/** Compact inline action, e.g. "Watch" / "Start" / "Focus" on the free-time cards. */
@Composable
fun CompactAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = PrimarySurface,
    contentColor: Color = PrimaryDeep
) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 74.dp, minHeight = Dimens.minTouchTarget)
            .pressScale(interaction)
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = contentColor)
    }
}

/** Circular 44dp icon button — back arrows, close buttons, the calendar toggle. */
@Composable
fun CircleIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = SurfaceWhite,
    tint: Color = TextPrimary,
    elevation: Dp = 4.dp
) {
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(15.dp)
    Box(
        modifier = modifier
            .size(Dimens.minTouchTarget)
            .pressScale(interaction)
            .then(
                if (elevation > 0.dp) {
                    Modifier.shadow(elevation, shape, clip = false, ambientColor = LuminaShadow, spotColor = LuminaShadow)
                } else Modifier
            )
            .clip(shape)
            .background(background)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription, tint = tint, modifier = Modifier.size(20.dp))
    }
}

/** Extended floating action button with the brand-tinted shadow from the design. */
@Composable
fun LuminaFab(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = modifier
            .height(60.dp)
            .pressScale(interaction)
            .shadow(14.dp, shape, clip = false, ambientColor = Primary, spotColor = Primary)
            .clip(shape)
            .background(Primary)
            .clickable(interactionSource = interaction, indication = null, onClick = onClick)
            .padding(start = 22.dp, end = 26.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(22.dp))
        Text(text, style = MaterialTheme.typography.titleMedium, color = Color.White)
    }
}
