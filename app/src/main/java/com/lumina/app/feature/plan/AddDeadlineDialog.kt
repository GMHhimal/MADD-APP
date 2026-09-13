package com.lumina.app.feature.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lumina.app.data.model.Priority
import com.lumina.app.feature.onboarding.WrapRow
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.components.SelectableChip
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Danger
import com.lumina.app.ui.theme.DangerSurface
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextTertiary
import com.lumina.app.ui.theme.Warning
import com.lumina.app.ui.theme.WarningSurface

@Composable
fun AddDeadlineDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, course: String, daysFromNow: Int, priority: Priority) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var days by remember { mutableIntStateOf(7) }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(28.dp),
        title = {
            Text("Add a deadline", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                LuminaField(title, { title = it }, "What is due?", "SE Assignment")
                LuminaField(course, { course = it }, "Module or context", "Software Engineering")

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("DUE IN", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                    WrapRow(spacing = 8.dp) {
                        listOf(1 to "Tomorrow", 3 to "3 days", 7 to "1 week", 14 to "2 weeks")
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
                    Text("PRIORITY", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                    WrapRow(spacing = 8.dp) {
                        Priority.entries.forEach { option ->
                            SelectableChip(
                                label = option.label,
                                selected = priority == option,
                                showCheckWhenSelected = false,
                                selectedColor = when (option) {
                                    Priority.HIGH -> Danger
                                    Priority.MEDIUM -> Warning
                                    Priority.LOW -> Primary
                                },
                                selectedContentColor = when (option) {
                                    Priority.HIGH -> Danger
                                    Priority.MEDIUM -> Warning
                                    Priority.LOW -> Primary
                                },
                                selectedBackground = when (option) {
                                    Priority.HIGH -> DangerSurface
                                    Priority.MEDIUM -> WarningSurface
                                    Priority.LOW -> Canvas
                                },
                                onClick = { priority = option }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                LuminaButton("Cancel", onDismiss, Modifier.weight(1f), LuminaButtonStyle.Neutral, height = 48.dp)
                LuminaButton(
                    text = "Save",
                    onClick = {
                        if (title.isNotBlank()) {
                            onSave(title.trim(), course.trim().ifBlank { "No module" }, days, priority)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = title.isNotBlank(),
                    height = 48.dp
                )
            }
        }
    )
}

@Composable
private fun LuminaField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = TextTertiary)
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
                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
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

@Preview(name = "Add Deadline Dialog", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddDeadlineDialogPreview() {
    LuminaTheme { AddDeadlineDialog(onDismiss = {}, onSave = { _, _, _, _ -> }) }
}
