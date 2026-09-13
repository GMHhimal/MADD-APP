package com.lumina.app.feature.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lumina.app.ui.components.AuroraBackdrop
import com.lumina.app.ui.components.LuminaButton
import com.lumina.app.ui.components.LuminaButtonStyle
import com.lumina.app.ui.icons.LuminaIcons
import com.lumina.app.ui.theme.LuminaTheme
import com.lumina.app.ui.theme.Canvas
import com.lumina.app.ui.theme.Lavender
import com.lumina.app.ui.theme.Mint
import com.lumina.app.ui.theme.Peach
import com.lumina.app.ui.theme.Primary
import com.lumina.app.ui.theme.PrimaryDeep
import com.lumina.app.ui.theme.TextPrimary
import com.lumina.app.ui.theme.TextSecondary
import com.lumina.app.ui.theme.TextTertiary

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Color(0xFFE4F0FC),
                    0.55f to Canvas,
                    1f to Color(0xFFFFF4EC)
                )
            )
    ) {
        AuroraBackdrop(
            modifier = Modifier.fillMaxSize(),
            blobs = listOf(
                Triple(Offset(0.08f, 0.20f), 0.62f, Lavender.copy(alpha = 0.34f)),
                Triple(Offset(0.92f, 0.10f), 0.52f, Peach.copy(alpha = 0.30f)),
                Triple(Offset(0.10f, 0.44f), 0.50f, Mint.copy(alpha = 0.26f)),
                Triple(Offset(0.85f, 0.40f), 0.56f, Primary.copy(alpha = 0.24f))
            )
        )

        Column(
            Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.9f))

            // Concentric rings: the same mark used on the app icon and the breathing screen.
            Box(Modifier.size(300.dp), contentAlignment = Alignment.Center) {
                listOf(300.dp to 0.30f, 240.dp to 0.45f, 180.dp to 0.65f).forEach { (ringSize, alpha) ->
                    Box(Modifier.size(ringSize).ringOutline(alpha))
                }
                Box(
                    Modifier
                        .size(120.dp)
                        .shadow(24.dp, RoundedCornerShape(60.dp), clip = false,
                            ambientColor = Primary, spotColor = Primary)
                        .clip(RoundedCornerShape(60.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF7FB9EE), Lavender)))
                )
            }

            Spacer(Modifier.height(24.dp))

            Box(
                Modifier
                    .size(84.dp)
                    .shadow(20.dp, RoundedCornerShape(26.dp), clip = false,
                        ambientColor = Primary, spotColor = Primary)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF63ACEC), Color(0xFF2A75C2)))),
                contentAlignment = Alignment.Center
            ) {
                Icon(LuminaIcons.Target, null, tint = Color.White, modifier = Modifier.size(44.dp))
            }

            Spacer(Modifier.height(16.dp))
            Text("Lumina", style = MaterialTheme.typography.displayLarge, color = TextPrimary)

            Spacer(Modifier.height(14.dp))
            Text(
                "Plan your life.\nProtect your time.\nFeel better.",
                style = MaterialTheme.typography.headlineLarge,
                color = PrimaryDeep,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))
            Text(
                "Your personal wellness and lifestyle assistant.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LuminaButton("Get Started", onGetStarted)
                LuminaButton("Sign In", onSignIn, style = LuminaButtonStyle.Secondary)
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "By continuing you agree to Lumina's Terms and Privacy Policy.",
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
        }
    }
}

/** Thin ring outline; extracted so the nesting above stays readable. */
private fun Modifier.ringOutline(alpha: Float): Modifier =
    this.border(2.dp, Color.White.copy(alpha = alpha), CircleShape)

@Preview(name = "Welcome", showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun WelcomeScreenPreview() {
    LuminaTheme {
        WelcomeScreen(onGetStarted = {}, onSignIn = {})
    }
}
