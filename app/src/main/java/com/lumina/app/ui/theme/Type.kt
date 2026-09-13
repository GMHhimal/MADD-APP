package com.lumina.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * The Figma file uses Inter. To match it exactly, drop the Inter .ttf files into
 * `res/font` (inter_regular.ttf, inter_medium.ttf, inter_semibold.ttf, inter_bold.ttf)
 * and replace the line below with:
 *
 * val LuminaFontFamily = FontFamily(
 *     Font(R.font.inter_regular, FontWeight.Normal),
 *     Font(R.font.inter_medium, FontWeight.Medium),
 *     Font(R.font.inter_semibold, FontWeight.SemiBold),
 *     Font(R.font.inter_bold, FontWeight.Bold)
 * )
 *
 * Until then the app uses the platform sans (Roboto), which shares Inter's metrics
 * closely enough that no layout changes are needed.
 */
val LuminaFontFamily: FontFamily = FontFamily.SansSerif

private fun style(
    weight: FontWeight,
    size: Int,
    lineHeight: Int,
    tracking: Double = 0.0
) = TextStyle(
    fontFamily = LuminaFontFamily,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = tracking.sp
)

val LuminaTypography = Typography(
    displayLarge = style(FontWeight.Bold, 30, 38, -0.4),      // Lumina/Display/Large
    headlineLarge = style(FontWeight.Bold, 24, 30, -0.3),     // Lumina/Heading/H1
    headlineMedium = style(FontWeight.SemiBold, 20, 26, -0.2),// Lumina/Heading/H2
    titleLarge = style(FontWeight.SemiBold, 18, 24, -0.1),    // Lumina/Title/Large
    titleMedium = style(FontWeight.SemiBold, 16, 22),         // Lumina/Title/Medium
    bodyLarge = style(FontWeight.Normal, 16, 24),             // Lumina/Body/Large
    bodyMedium = style(FontWeight.Normal, 14, 21),            // Lumina/Body/Medium
    labelLarge = style(FontWeight.Medium, 14, 18),            // Lumina/Label/Large
    labelMedium = style(FontWeight.Medium, 13, 17),           // Lumina/Label/Medium
    labelSmall = style(FontWeight.Medium, 12, 16, 0.1)        // Lumina/Caption
)

/** Tracked-out tile label, e.g. "MOOD" / "FOCUS TIME". */
val OverlineStyle = style(FontWeight.SemiBold, 11, 14, 0.7)

/** Large numerals: breathing countdown, focus timer. */
val NumericDisplay = style(FontWeight.Bold, 34, 40, -0.5)
