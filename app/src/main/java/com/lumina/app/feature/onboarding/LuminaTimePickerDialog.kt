package com.lumina.app.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.PrimarySurface
import com.lumina.app.ui.theme.SurfaceSunken
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary

/**
 * Wraps the Material 3 time picker so routine rows can edit an anchor time.
 * Values are stored as minutes-from-midnight throughout the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuminaTimePickerDialog(
    initialMinutes: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = (initialMinutes / 60) % 24,
        initialMinute = initialMinutes % 60,
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(28.dp),
        title = {
            Text("Pick a time", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        },
        text = {
            Column(Modifier.fillMaxWidth()) {
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = SurfaceSunken,
                        selectorColor = Primary,
                        containerColor = SurfaceWhite,
                        periodSelectorSelectedContainerColor = PrimarySurface,
                        periodSelectorSelectedContentColor = PrimaryDeep,
                        timeSelectorSelectedContainerColor = PrimarySurface,
                        timeSelectorSelectedContentColor = PrimaryDeep,
                        timeSelectorUnselectedContainerColor = SurfaceSunken,
                        timeSelectorUnselectedContentColor = TextSecondary
                    )
                )
            }
        },
        confirmButton = {
            Row(
                Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LuminaButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    style = LuminaButtonStyle.Neutral,
                    height = 48.dp
                )
                LuminaButton(
                    text = "Save",
                    onClick = { onConfirm(state.hour * 60 + state.minute) },
                    modifier = Modifier.weight(1f),
                    height = 48.dp
                )
            }
        }
    )
}

@Preview(name = "Time Picker", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun LuminaTimePickerDialogPreview() {
    LuminaTheme { LuminaTimePickerDialog(initialMinutes = 7 * 60 + 30, onDismiss = {}, onConfirm = {}) }
}
