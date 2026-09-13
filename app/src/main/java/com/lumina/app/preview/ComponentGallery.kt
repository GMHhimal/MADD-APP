package com.lumina.app.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lumina.app.data.model.Mood
import com.lumina.app.navigation.Routes
import com.lumina.app.ui.components.CircleIconButton
import com.lumina.app.ui.components.CompactAction
import com.lumina.app.ui.components.LinearMeter
import com.lumina.app.ui.components.LuminaBottomBar
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.components.LuminaFab
import com.lumina.app.ui.components.MoodFace
import com.lumina.app.ui.components.ProgressRing
import com.lumina.app.ui.components.SegmentedControl
import com.lumina.app.ui.components.SelectableChip
import com.lumina.app.ui.components.StepProgress
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.TextPrimary

/** A single viva-friendly preview of the reusable Compose design system. */
@Preview(name = "Lumina UI Components", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun ComponentGalleryPreview() {
    LuminaTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(Canvas)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Buttons", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
            LuminaButton("Primary button", onClick = {})
            LuminaButton("Secondary button", onClick = {}, style = LuminaButtonStyle.Secondary)
            LuminaButton("Soft button", onClick = {}, style = LuminaButtonStyle.Soft)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                CompactAction("Focus", onClick = {})
                CircleIconButton(LuminaIcons.ChevronLeft, "Back", onClick = {})
                LuminaFab("Add Task", LuminaIcons.Plus, onClick = {})
            }

            Text("Chips", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SelectableChip("Selected", selected = true, onClick = {})
                SelectableChip("Normal", selected = false, onClick = {})
            }
            SegmentedControl(listOf("Week", "Month", "3 Months"), selectedIndex = 0, onSelect = {})

            Text("Progress", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
            StepProgress(currentStep = 3, totalSteps = 5)
            LinearMeter(progress = 0.65f)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ProgressRing(progress = 0.72f) {
                    Text("72%", style = MaterialTheme.typography.titleMedium)
                }
                MoodFace(Mood.GOOD, size = 72.dp)
            }

            Text("Bottom navigation", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
            LuminaBottomBar(currentRoute = Routes.HOME, onNavigate = {})
            Spacer(Modifier.height(12.dp))
        }
    }
}
