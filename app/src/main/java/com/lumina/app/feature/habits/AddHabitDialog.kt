package com.lumina.app.feature.habits

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lumina.app.feature.onboarding.WrapRow
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.components.SelectableChip
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.BorderSubtle
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.SurfaceWhite
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextTertiary

private val IconChoices = listOf(
    "water" to "Water",
    "meal" to "Meal",
    "gym" to "Exercise",
    "meditation" to "Meditation",
    "walk" to "Walk",
    "sleep" to "Sleep",
    "stretch" to "Stretch",
    "book" to "Reading"
)

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, detail: String, iconKey: String, targetCount: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    var iconKey by remember { mutableStateOf("water") }
    var target by remember { mutableIntStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(28.dp),
        title = {
            Text("New habit", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    "Keep it small enough that a bad day cannot break it.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = com.lumina.app.ui.theme.TextSecondary
                )

                Field(title, { title = it }, "HABIT", "Drink water")
                Field(detail, { detail = it }, "DETAIL (OPTIONAL)", "Before each meal")

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("ICON", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                    WrapRow(spacing = 8.dp) {
                        IconChoices.forEach { (key, label) ->
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
                    Text("TIMES PER DAY", style = MaterialTheme.typography.labelSmall, color = TextTertiary)
                    WrapRow(spacing = 8.dp) {
                        listOf(1, 2, 4, 8).forEach { count ->
                            SelectableChip(
                                label = if (count == 1) "Once" else "$count times",
                                selected = target == count,
                                showCheckWhenSelected = false,
                                onClick = { target = count }
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
                    text = "Create habit",
                    onClick = {
                        onSave(
                            title.trim(),
                            detail.trim().ifBlank { if (target > 1) "Through the day" else "Any time today" },
                            iconKey,
                            target
                        )
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

@Preview(name = "Add Habit Dialog", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddHabitDialogPreview() {
    LuminaTheme { AddHabitDialog(onDismiss = {}, onSave = { _, _, _, _ -> }) }
}
