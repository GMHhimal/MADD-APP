package com.lumina.app.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lumina.app.ui.components.CircleIconButton
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.StepProgress
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.TextSecondary

/**
 * Shared chrome for the three setup steps: back button, "Step n of 5", the segment
 * indicator, and a footer that fades the scrolling content out behind the primary action.
 */
@Composable
fun OnboardingScaffold(
    step: Int,
    title: String,
    subtitle: String,
    ctaLabel: String,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Box(Modifier.fillMaxSize().background(Canvas)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 140.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircleIconButton(LuminaIcons.ChevronLeft, "Back", onBack)
                        Spacer(Modifier.weight(1f))
                        Text(
                            "Step $step of 5",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                    }
                    StepProgress(currentStep = step, totalSteps = 5)
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.headlineLarge)
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
            content()
        }

        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0f to Canvas.copy(alpha = 0f),
                        0.42f to Canvas,
                        1f to Canvas
                    )
                )
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 34.dp, bottom = 20.dp)
        ) {
            LuminaButton(ctaLabel, onContinue)
        }
    }
}

@Preview(name = "Onboarding Scaffold", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OnboardingScaffoldPreview() {
    LuminaTheme {
        OnboardingScaffold(
            step = 1,
            title = "Let's understand\nyour lifestyle.",
            subtitle = "A few quick answers help Lumina build your day.",
            ctaLabel = "Next",
            onBack = {},
            onContinue = {}
        ) {
            item {
                Text(
                    "Preview content appears here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}
