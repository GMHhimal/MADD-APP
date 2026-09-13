package com.lumina.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LuminaColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimarySurface,
    onPrimaryContainer = PrimaryDeep,

    secondary = Peach,
    onSecondary = Color.White,
    secondaryContainer = PeachSurface,
    onSecondaryContainer = PeachDeep,

    tertiary = Lavender,
    onTertiary = Color.White,
    tertiaryContainer = LavenderSurface,
    onTertiaryContainer = LavenderDeep,

    background = Canvas,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceSunken,
    onSurfaceVariant = TextSecondary,

    outline = BorderStrong,
    outlineVariant = BorderSubtle,

    error = Danger,
    onError = Color.White,
    errorContainer = DangerSurface,
    onErrorContainer = Danger,

    scrim = Color(0xFF0B1524)
)

@Composable
fun LuminaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LuminaColorScheme,
        typography = LuminaTypography,
        shapes = LuminaShapes,
        content = content
    )
}
