package com.lumina.app.ui.theme

import androidx.compose.ui.unit.dp

/** Spacing tokens from the "Lumina / Layout" collection. */
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp
    val xxxl = 32.dp
}

object Dimens {
    /** Every screen keeps a constant 24dp gutter, matching the prototype. */
    val screenGutter = 24.dp

    /** WCAG 2.1 AA minimum interactive size. Enforced on every tappable element. */
    val minTouchTarget = 44.dp

    val buttonHeight = 56.dp
    val chipHeight = 46.dp
    val bottomBarHeight = 84.dp
}
