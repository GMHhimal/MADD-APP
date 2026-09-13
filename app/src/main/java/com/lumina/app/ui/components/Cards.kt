package com.lumina.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lumina.app.data.model.EventKind
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.Lavender
import com.lumina.app.ui.theme.LavenderDeep
import com.lumina.app.ui.theme.LavenderSurface
import com.lumina.app.ui.theme.Peach
import com.lumina.app.ui.theme.PeachDeep
import com.lumina.app.ui.theme.PeachSurface
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySoft
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.Success
import com.lumina.app.ui.theme.SuccessSurface
import com.lumina.app.ui.theme.SurfaceSunken
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
import com.lumina.app.ui.theme.TextTertiary

/** The lavender-to-blue wash that marks anything Lumina is saying rather than the user. */
val LuminaVoiceBrush = Brush.linearGradient(listOf(LavenderSurface, PrimarySurface))

/**
 * One row of the Today timeline: time gutter, connector rail with a status dot, and the
 * event card itself.
 */
@Composable
fun TimelineRow(
    time: String,
    title: String,
    meta: String,
    kind: EventKind,
    isNow: Boolean,
    showConnector: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val free = kind == EventKind.FREE
    val dotColor = when {
        isNow -> Primary
        free -> Peach
        else -> PrimarySoft
    }
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = time,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = TextSecondary,
            modifier = Modifier.width(58.dp).padding(top = 2.dp),
            textAlign = TextAlign.End
        )

        // Connector rail. The line runs past the bottom of the card so consecutive rows join up.
        Box(Modifier.width(14.dp)) {
            if (showConnector) {
                Box(
                    Modifier
                        .padding(start = 6.dp, top = 16.dp)
                        .width(2.dp)
                        .height(120.dp)
                        .background(BorderSubtle)
                )
            }
            Box(
                Modifier
                    .padding(top = 2.dp)
                    .size(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(dotColor)
            )
        }

        LuminaCard(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(18.dp),
            background = if (free) PeachSurface else SurfaceWhite,
            border = when {
                isNow -> BorderStroke(1.5.dp, Primary)
                free -> BorderStroke(1.5.dp, Peach)
                else -> null
            },
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            onClick = onClick
        ) {
            if (isNow) {
                Row(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimarySurface)
                        .padding(horizontal = 9.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(Modifier.size(6.dp).clip(RoundedCornerShape(3.dp)).background(Primary))
                    Text(
                        "HAPPENING NOW",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.6.sp
                        ),
                        color = PrimaryDeep
                    )
                }
                Spacer(Modifier.height(6.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                color = if (free) PeachDeep else TextPrimary
            )
            Spacer(Modifier.height(4.dp))
            Text(meta, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

/**
 * A tracked habit. Done state changes fill, border and icon together, so completion is
 * readable without relying on the green.
 */
@Composable
fun HabitRow(
    title: String,
    detail: String,
    icon: ImageVector,
    isDone: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    LuminaCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        background = if (isDone) SuccessSurface else SurfaceWhite,
        border = if (isDone) BorderStroke(1.2.dp, Success.copy(alpha = 0.35f)) else null,
        contentPadding = PaddingValues(start = 14.dp, end = 16.dp, top = 14.dp, bottom = 14.dp),
        onClick = onToggle
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconChip(
                icon = icon,
                tint = Primary,
                background = if (isDone) SurfaceWhite else PrimarySurface,
                size = 46.dp
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                    color = TextPrimary
                )
                Spacer(Modifier.height(3.dp))
                Text(detail, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
            Spacer(Modifier.width(12.dp))
            Box(
                Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(if (isDone) Success else PrimarySurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isDone) LuminaIcons.Check else LuminaIcons.Plus,
                    contentDescription = if (isDone) "Completed" else "Mark complete",
                    tint = if (isDone) Color.White else Primary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

/** Lumina's suggestion surface. */
@Composable
fun InsightCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null
) {
    LuminaCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        brush = LuminaVoiceBrush,
        border = BorderStroke(1.2.dp, Lavender),
        contentPadding = PaddingValues(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(LuminaIcons.Sparkle, null, tint = LavenderDeep, modifier = Modifier.size(20.dp))
            Text(title, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold), color = PrimaryDeep)
        }
        Spacer(Modifier.height(10.dp))
        Text(body, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        if (primaryActionLabel != null) {
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LuminaButton(
                    text = primaryActionLabel,
                    onClick = { onPrimaryAction?.invoke() },
                    modifier = Modifier.weight(1f),
                    height = 46.dp
                )
                if (secondaryActionLabel != null) {
                    LuminaButton(
                        text = secondaryActionLabel,
                        onClick = { onSecondaryAction?.invoke() },
                        modifier = Modifier.weight(1f),
                        style = LuminaButtonStyle.Secondary,
                        height = 46.dp
                    )
                }
            }
        }
    }
}

/** Two-line stat tile used in the Home summary and the Progress grid. */
@Composable
fun StatTile(
    label: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    tintBackground: Color,
    modifier: Modifier = Modifier,
    caption: String? = null,
    captionColor: Color = TextSecondary,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp)
    ) {
        IconChip(icon, tint, tintBackground, size = 32.dp, iconSize = 18.dp, cornerRadius = 11.dp)
        Spacer(Modifier.height(8.dp))
        OverlineLabel(label)
        Spacer(Modifier.height(2.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )
        if (caption != null) {
            Spacer(Modifier.height(2.dp))
            Text(caption, style = MaterialTheme.typography.labelSmall, color = captionColor)
        }
    }
}

/** Empty states: an invitation to act, never a dead end. */
@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    body: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    accent: Color = Primary,
    accentSurface: Color = PrimarySurface
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(176.dp), contentAlignment = Alignment.Center) {
            Box(
                Modifier.size(176.dp).clip(RoundedCornerShape(88.dp))
                    .background(accentSurface.copy(alpha = 0.55f))
            )
            Box(
                Modifier.size(124.dp).clip(RoundedCornerShape(62.dp)).background(accentSurface)
            )
            Icon(icon, null, tint = accent, modifier = Modifier.size(58.dp))
        }
        Spacer(Modifier.height(22.dp))
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(9.dp))
        Text(
            body,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.height(22.dp))
        LuminaButton(
            text = actionLabel,
            onClick = onAction,
            leadingIcon = LuminaIcons.Plus
        )
    }
}

/** Neutral informational strip — used for the non-diagnostic health disclaimers. */
@Composable
fun DisclaimerNote(
    title: String?,
    body: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = LuminaIcons.Info
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceSunken)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Icon(icon, null, tint = TextTertiary, modifier = Modifier.size(18.dp))
        Column {
            if (title != null) {
                Text(
                    title,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TextPrimary
                )
                Spacer(Modifier.height(2.dp))
            }
            Text(body, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}
