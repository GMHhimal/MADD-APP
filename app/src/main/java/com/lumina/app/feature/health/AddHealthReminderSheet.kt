package com.lumina.app.feature.health

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lumina.app.feature.onboarding.WrapRow
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.components.SelectableChip
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.BorderStrong
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
import com.lumina.app.ui.theme.TextTertiary

private val ReminderIcons = listOf(
    "blood" to "Blood test",
    "cholesterol" to "Cholesterol",
    "dental" to "Dental",
    "eye" to "Eye"
)

private val RepeatOptions = listOf(
    0 to "One-time",
    3 to "3 months",
    6 to "6 months",
    12 to "Yearly"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHealthReminderSheet(
    onDismiss: () -> Unit,
    onSave: (title: String, iconKey: String, daysFromNow: Int, repeatMonths: Int, note: String, notify: Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var title by remember { mutableStateOf("") }
    var iconKey by remember { mutableStateOf("blood") }
    var days by remember { mutableIntStateOf(30) }
    var repeatMonths by remember { mutableIntStateOf(12) }
    var note by remember { mutableStateOf("") }
    var notify by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text("Add health reminder", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Text(
                    "You choose what to track. Lumina only reminds you.",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
            }

            Field(title, { title = it }, "REMINDER NAME", "Full blood count")

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("TYPE", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                WrapRow(spacing = 8.dp) {
                    ReminderIcons.forEach { (key, label) ->
                        SelectableChip(
                            label = label,
                            selected = iconKey == key,
                            leadingIcon = LuminaIcons.byKey(key),
                            showCheckWhenSelected = false,
                            onClick = { iconKey = key }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("DUE IN", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                WrapRow(spacing = 8.dp) {
                    listOf(7 to "1 week", 30 to "1 month", 90 to "3 months", 180 to "6 months")
                        .forEach { (value, label) ->
                            SelectableChip(
                                label = label,
                                selected = days == value,
                                showCheckWhenSelected = false,
                                onClick = { days = value }
                            )
                        }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("REPEAT", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                WrapRow(spacing = 8.dp) {
                    RepeatOptions.forEach { (months, label) ->
                        SelectableChip(
                            label = label,
                            selected = repeatMonths == months,
                            showCheckWhenSelected = false,
                            onClick = { repeatMonths = months }
                        )
                    }
                }
            }

            Field(note, { note = it }, "NOTE (OPTIONAL)", "Fasting required, book a morning slot")

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Canvas, RoundedCornerShape(16.dp))
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        "Remind me 7 days before",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Plus a reminder on the day",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                Spacer(Modifier.width(12.dp))
                Switch(
                    checked = notify,
                    onCheckedChange = { notify = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Primary,
                        checkedThumbColor = Color.White,
                        uncheckedTrackColor = BorderStrong,
                        uncheckedThumbColor = Color.White,
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LuminaButton("Cancel", onDismiss, Modifier.weight(1f), LuminaButtonStyle.Neutral, height = 54.dp)
                LuminaButton(
                    text = "Save reminder",
                    onClick = { onSave(title.trim(), iconKey, days, repeatMonths, note.trim(), notify) },
                    modifier = Modifier.weight(1f),
                    enabled = title.isNotBlank(),
                    height = 54.dp
                )
            }
        }
    }
}

@Composable
private fun Field(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextTertiary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextTertiary) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Canvas, RoundedCornerShape(16.dp))
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Canvas,
                unfocusedContainerColor = Canvas,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = Primary
            ),
            textStyle = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(name = "Add Health Reminder", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddHealthReminderSheetPreview() {
    LuminaTheme { AddHealthReminderSheet(onDismiss = {}, onSave = { _, _, _, _, _, _ -> }) }
}
