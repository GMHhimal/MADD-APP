package com.lumina.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lumina.app.navigation.TopLevelDestination
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextTertiary

/**
 * The persistent five-tab bar.
 *
 * Hand-built rather than using M3's NavigationBar so the pill indicator, label weight and
 * spacing match the design exactly. Each item is 52dp tall and at least 44dp wide.
 */
@Composable
fun LuminaBottomBar(
    currentRoute: String?,
    onNavigate: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .background(SurfaceWhite)
    ) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
        Row(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopLevelDestination.entries.forEach { destination ->
                val selected = currentRoute == destination.route
                val interaction = remember { MutableInteractionSource() }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clickable(
                            interactionSource = interaction,
                            indication = null,
                            role = Role.Tab
                        ) { onNavigate(destination) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        Modifier
                            .width(52.dp)
                            .height(28.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (selected) PrimarySurface else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            destination.icon,
                            contentDescription = destination.label,
                            tint = if (selected) PrimaryDeep else TextTertiary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = destination.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                        ),
                        color = if (selected) PrimaryDeep else TextTertiary,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }
        }
    }
}
