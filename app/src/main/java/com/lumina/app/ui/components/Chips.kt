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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.Dimens
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary

/**
 * Selectable chip.
 *
 * Selection is signalled three ways — fill, border weight and a tick icon — so state is
 * never carried by colour alone (WCAG 1.4.1).
 */
@Composable
fun SelectableChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    selectedColor: Color = Primary,
    selectedContentColor: Color = PrimaryDeep,
    selectedBackground: Color = PrimarySurface,
    showCheckWhenSelected: Boolean = true
) {
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = modifier
            .height(Dimens.chipHeight)
            .pressScale(interaction, 0.97f)
            .clip(shape)
            .background(if (selected) selectedBackground else SurfaceWhite)
            .border(
                BorderStroke(
                    width = if (selected) 1.8.dp else 1.1.dp,
                    color = if (selected) selectedColor else BorderSubtle
                ),
                shape
            )
            .clickable(
                interactionSource = interaction,
                indication = null,
                role = Role.Checkbox,
                onClick = onClick
            )
            .padding(start = 14.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        if (leadingIcon != null) {
            Icon(
                leadingIcon, null,
                tint = if (selected) selectedContentColor else TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = if (selected) selectedContentColor else TextPrimary
        )
        if (selected && showCheckWhenSelected) {
            Box(
                Modifier.size(18.dp).clip(RoundedCornerShape(9.dp)).background(selectedColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(LuminaIcons.Check, null, tint = Color.White, modifier = Modifier.size(12.dp))
            }
        }
    }
}

/** Segmented control used for the Week / Month / 3 Months range filter. */
@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(com.lumina.app.ui.theme.SurfaceSunken)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 40.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(if (selected) SurfaceWhite else Color.Transparent)
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                    ),
                    color = if (selected) TextPrimary else TextSecondary,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
    }
}
