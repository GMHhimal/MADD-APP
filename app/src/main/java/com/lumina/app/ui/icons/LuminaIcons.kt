package com.lumina.app.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * Lumina's icon set.
 *
 * Every glyph is the exact path data used by the Figma components, drawn on a 24x24
 * grid with a 1.8 stroke, round caps and round joins. They are declared as stroked
 * [ImageVector]s so `Icon(..., tint = ...)` recolours them, and they export cleanly
 * to vector drawables if you ever need them outside Compose.
 */
object LuminaIcons {

    // ---- builders -------------------------------------------------------------

    private fun vector(
        name: String,
        stroked: List<String> = emptyList(),
        filled: List<String> = emptyList(),
        strokeWidth: Float = 1.8f
    ): ImageVector {
        val builder = ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        )
        stroked.forEach { d ->
            builder.addPath(
                pathData = addPathNodes(d),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
        }
        filled.forEach { d ->
            builder.addPath(pathData = addPathNodes(d), fill = SolidColor(Color.Black))
        }
        return builder.build()
    }

    /** SVG <circle> has no path equivalent, so build one from two arcs. */
    private fun circle(cx: Float, cy: Float, r: Float): String =
        "M$cx ${cy - r}a$r $r 0 1 0 0 ${2 * r}a$r $r 0 1 0 0 ${-2 * r}z"

    /** SVG <rect rx> as a path. */
    private fun roundRect(x: Float, y: Float, w: Float, h: Float, r: Float): String =
        "M${x + r} ${y}h${w - 2 * r}a$r $r 0 0 1 $r ${r}v${h - 2 * r}a$r $r 0 0 1 ${-r} ${r}" +
            "h${-(w - 2 * r)}a$r $r 0 0 1 ${-r} ${-r}v${-(h - 2 * r)}a$r $r 0 0 1 $r ${-r}z"

    // ---- navigation -----------------------------------------------------------

    val Home by lazy {
        vector("home", listOf("M3.2 10.4 12 3.4l8.8 7v9.1a1.5 1.5 0 0 1-1.5 1.5h-4.4v-6.2H9.1V21H4.7a1.5 1.5 0 0 1-1.5-1.5z"))
    }
    val Plan by lazy {
        vector("plan", listOf(
            "M8 2.5v3.6M16 2.5v3.6M3.6 9.9h16.8",
            roundRect(3.6f, 5.6f, 16.8f, 15.4f, 1.6f),
            "m9.2 14.9 1.9 1.9 3.7-3.8"
        ))
    }
    val Wellness by lazy {
        vector("wellness", listOf("M12 20.6S4.2 15.7 4.2 10.6A4.5 4.5 0 0 1 12 7.6a4.5 4.5 0 0 1 7.8 3c0 5.1-7.8 10-7.8 10z"))
    }
    val Progress by lazy {
        vector("progress", listOf("M4.4 20.4V13M12 20.4V4.6M19.6 20.4v-5.1"), strokeWidth = 1.9f)
    }
    val Profile by lazy {
        vector("profile", listOf(circle(12f, 8.1f, 4.1f), "M4.6 20.6a7.4 7.4 0 0 1 14.8 0"))
    }

    // ---- time, planning -------------------------------------------------------

    val Clock by lazy { vector("clock", listOf(circle(12f, 12f, 8.6f), "M12 7.1V12l3.5 2.1")) }
    val Calendar by lazy {
        vector("calendar", listOf(roundRect(3.6f, 5.4f, 16.8f, 15f, 2.4f), "M8 2.8v4.4M16 2.8v4.4M3.6 10.4h16.8"))
    }
    val Target by lazy {
        vector(
            name = "target",
            stroked = listOf(circle(12f, 12f, 8.6f), circle(12f, 12f, 4.4f)),
            filled = listOf(circle(12f, 12f, 1.3f))
        )
    }
    val Alert by lazy { vector("alert", listOf("M12 4.2 21 19.4H3z", "M12 10v4M12 16.8h.02")) }
    val Repeat by lazy {
        vector("repeat", listOf(
            "M4 8.6h12.4a3.6 3.6 0 0 1 3.6 3.6M20 15.4H7.6A3.6 3.6 0 0 1 4 11.8",
            "m6.8 5.8-2.8 2.8 2.8 2.8M17.2 18.2l2.8-2.8-2.8-2.8"
        ))
    }
    val Swap by lazy { Repeat }

    // ---- wellness -------------------------------------------------------------

    val Wave by lazy {
        vector("wave", listOf("M3.2 9.4c2.4-3 5.6-3 8 0s5.6 3 8 0M3.2 15.4c2.4-3 5.6-3 8 0s5.6 3 8 0"))
    }
    val Water by lazy { vector("water", listOf("M12 3.4s6 6.2 6 10.3a6 6 0 0 1-12 0C6 9.6 12 3.4 12 3.4z")) }
    val Sprout by lazy {
        vector("sprout", listOf("M12 21v-7.8M12 13.2C12 9.4 9.1 6.5 5.4 6.5c0 3.8 2.9 6.7 6.6 6.7zM12 13.2c0-3.8 2.9-6.7 6.6-6.7 0 3.8-2.9 6.7-6.6 6.7z"))
    }
    val Leaf by lazy {
        vector("leaf", listOf("M4.6 19.4C3.4 13 7.4 5.4 19.8 4.6c.8 11.6-6.4 16.2-13.2 15.2z", "M8.4 15.8c2.4-3.6 5.4-6 8.8-7.4"))
    }
    val Dumbbell by lazy { vector("dumbbell", listOf("M5.4 9.3v5.4M8.6 7.4v9.2M15.4 7.4v9.2M18.6 9.3v5.4M8.6 12h6.8")) }
    val Meal by lazy { vector("meal", listOf("M3.9 11.4h16.2a8.1 8.1 0 0 1-16.2 0zM6.2 19.4h11.6M12 11.4V6.6M9.9 4.6h4.2")) }
    val Coffee by lazy {
        vector("coffee", listOf(
            "M4.4 8.2h12v6.2a4.4 4.4 0 0 1-4.4 4.4H8.8a4.4 4.4 0 0 1-4.4-4.4z",
            "M16.4 9.6h1.4a2.6 2.6 0 0 1 0 5.2h-1.4M6.6 3.2v2.4M10.4 3.2v2.4M14.2 3.2v2.4"
        ))
    }
    val Moon by lazy { vector("moon", listOf("M20 14.6A8.6 8.6 0 0 1 9.4 4a8.6 8.6 0 1 0 10.6 10.6z")) }
    val Sun by lazy {
        vector("sun", listOf(circle(12f, 12f, 4.2f), "M12 2.8v2.4M12 18.8v2.4M4.5 4.5l1.7 1.7M17.8 17.8l1.7 1.7M2.8 12h2.4M18.8 12h2.4M4.5 19.5l1.7-1.7M17.8 6.2l1.7-1.7"))
    }
    val Walk by lazy {
        vector("walk", listOf(circle(13.6f, 4.9f, 1.9f), "m12.6 9.3-2.7 3.9 2.6 1.7.9 5.2M12.6 9.3l3.2 1.5.8 3.5M10.2 20.5 8.4 17.2"))
    }
    val Stretch by lazy {
        vector("stretch", listOf(circle(12f, 5f, 2f), "M12 8.4v6M12 14.4 8.4 20.6M12 14.4l3.6 6.2M5.4 10.2 12 8.8l6.6 1.4"))
    }
    val Balance by lazy {
        vector("balance", listOf("M12 4.4v15.2M6 8.2h12M5.2 20.2h13.6M8.4 8.2 5.2 14.9h6.4zM15.6 8.2l-3.2 6.7h6.4z"))
    }
    val Battery by lazy {
        vector("battery", listOf(roundRect(2.6f, 7.4f, 15.6f, 9.2f, 2.6f), "M21.4 10.4v3.2"), listOf("M5.6 10.4h4.4v3.2H5.6z"))
    }

    // ---- interests ------------------------------------------------------------

    val Play by lazy { vector("play", listOf(circle(12f, 12f, 8.6f), "m10.3 8.6 5.4 3.4-5.4 3.4z")) }
    val Tv by lazy { vector("tv", listOf(roundRect(3.4f, 7.6f, 17.2f, 12f, 2.2f), "M8.4 4.2 12 7.6l3.6-3.4")) }
    val Game by lazy {
        vector("game", listOf(
            "M8.6 7.6h6.8a5 5 0 0 1 0 10H8.6a5 5 0 0 1 0-10z",
            "M7.4 10.8v3.4M5.7 12.5h3.4M15.6 13.4h.02M17.4 11.2h.02"
        ))
    }
    val Music by lazy {
        vector("music", listOf("M9 17.4V6.2l10-2v11.2", circle(6.6f, 17.6f, 2.6f), circle(16.6f, 15.4f, 2.6f)))
    }
    val Book by lazy {
        vector("book", listOf("M4.2 5.6a2.2 2.2 0 0 1 2.2-2.2h13.4v14.2H6.4a2.2 2.2 0 0 0-2.2 2.2z", "M4.2 5.6v14.2"))
    }
    val Pulse by lazy { vector("pulse", listOf("M2.8 12h4.1l2.5-7 4 14 2.5-7h5.3")) }
    val Cap by lazy {
        vector("cap", listOf("M12 4.6 2.6 9.2 12 13.8l9.4-4.6z", "M6.6 11.4v4.9c0 1.6 2.4 2.9 5.4 2.9s5.4-1.3 5.4-2.9v-4.9"))
    }
    val Mic by lazy {
        vector("mic", listOf("M12 3.4a2.8 2.8 0 0 1 2.8 2.8v5.2a2.8 2.8 0 1 1-5.6 0V6.2A2.8 2.8 0 0 1 12 3.4z", "M5.6 11.2a6.4 6.4 0 0 0 12.8 0M12 17.6v3"))
    }
    val Users by lazy {
        vector("users", listOf(circle(9.2f, 8f, 3.3f), "M3.4 19.4a5.8 5.8 0 0 1 11.6 0M16.4 5a3.3 3.3 0 0 1 0 6.2M17.8 14.2a5.8 5.8 0 0 1 3.4 5.2"))
    }
    val Sparkle by lazy { vector("sparkle", listOf("M12 3.6 13.9 9.2 19.5 11 13.9 12.9 12 18.5 10.1 12.9 4.5 11l5.6-1.8z")) }
    val Camera by lazy { vector("camera", listOf("M3.6 8.6h3.2l1.6-2.6h7.2l1.6 2.6h3.2v10.4H3.6z", circle(12f, 13.4f, 3.4f))) }
    val Pen by lazy { vector("pen", listOf("m4 20 1.1-4.1L16.6 4.4a2 2 0 0 1 2.9 2.9L8 18.9z", "m14.6 6.4 3 3")) }
    val Code by lazy { vector("code", listOf("M8.6 8.2 4.6 12l4 3.8M15.4 8.2l4 3.8-4 3.8M13.4 5.6l-2.8 12.8")) }
    val Dice by lazy {
        vector(
            name = "dice",
            stroked = listOf(roundRect(3.6f, 3.6f, 16.8f, 16.8f, 4f)),
            filled = listOf(
                circle(8.6f, 8.6f, 1.3f),
                circle(15.4f, 15.4f, 1.3f),
                circle(12f, 12f, 1.3f)
            )
        )
    }

    // ---- health ---------------------------------------------------------------

    val Blood by lazy {
        vector("blood", listOf("M12 3.4s6 6.2 6 10.3a6 6 0 0 1-12 0C6 9.6 12 3.4 12 3.4z", "M9.6 13.6h4.8M12 11.2v4.8"))
    }
    val HeartPulse by lazy {
        vector("heartPulse", listOf(
            "M2.8 12h3.6l1.8-3.4 2.6 6.6 2.2-4.4 1.4 1.2h6.8",
            "M20.2 8.4a4 4 0 0 0-6.8-2.2L12 7.6l-1.4-1.4A4 4 0 0 0 4 8.4"
        ))
    }
    val Heart by lazy { vector("heart", listOf("M12 20.4S4.2 15.5 4.2 10.4A4.5 4.5 0 0 1 12 7.4a4.5 4.5 0 0 1 7.8 3c0 5.1-7.8 10-7.8 10z")) }
    val Tooth by lazy {
        vector("tooth", listOf("M12 3.6c-2 0-2.6 1-4.4 1-1.5 0-3.5.6-3.5 4.3 0 3 1 4.4 1.6 6.6.5 2 .4 5.4 2.2 5.4 1.7 0 1.7-3.6 2.7-5.4.5-.9 1-.9 1.4-.9s.9 0 1.4.9c1 1.8 1 5.4 2.7 5.4 1.8 0 1.7-3.4 2.2-5.4.6-2.2 1.6-3.6 1.6-6.6 0-3.7-2-4.3-3.5-4.3-1.8 0-2.4-1-4.4-1z"))
    }
    val Eye by lazy {
        vector("eye", listOf("M2.6 12S6.4 5.6 12 5.6 21.4 12 21.4 12 17.6 18.4 12 18.4 2.6 12 2.6 12z", circle(12f, 12f, 3.2f)))
    }
    val Shield by lazy {
        vector("shield", listOf("M12 3.2 4.6 6.2v5.4c0 4.4 3.1 7.9 7.4 9.2 4.3-1.3 7.4-4.8 7.4-9.2V6.2z", "M12 9.6v3.4M12 16.2h.02"))
    }

    // ---- ui -------------------------------------------------------------------

    val Plus by lazy { vector("plus", listOf("M12 5.6v12.8M5.6 12h12.8"), strokeWidth = 2f) }
    val Close by lazy { vector("close", listOf("M6.5 6.5 17.5 17.5M17.5 6.5 6.5 17.5"), strokeWidth = 2.1f) }
    val Check by lazy { vector("check", listOf("m6.5 12.4 3.6 3.6 7.4-7.5"), strokeWidth = 2.4f) }
    val CheckCircle by lazy { vector("checkCircle", listOf(circle(12f, 12f, 8.7f), "m8.2 12.2 2.6 2.6 5-5.2")) }
    val ChevronRight by lazy { vector("chevronRight", listOf("m9.5 5.5 6.5 6.5-6.5 6.5"), strokeWidth = 2f) }
    val ChevronLeft by lazy { vector("chevronLeft", listOf("M14.5 5.5 8 12l6.5 6.5"), strokeWidth = 2.1f) }
    val ArrowRight by lazy { vector("arrowRight", listOf("M4.5 12h14M13.5 6.5 19 12l-5.5 5.5"), strokeWidth = 1.9f) }
    val Bell by lazy {
        vector("bell", listOf("M18 8.6a6 6 0 1 0-12 0c0 6-2.4 7.6-2.4 7.6h16.8S18 14.6 18 8.6zM13.7 19.6a2 2 0 0 1-3.4 0"))
    }
    val BellOff by lazy {
        vector("bellOff", listOf("M18 8.6a6 6 0 0 0-9.4-4.9M5.2 9.9c-.1 5-2.8 6.3-2.8 6.3h13.2M13.7 19.6a2 2 0 0 1-3.4 0M3.4 3.4l17.2 17.2"))
    }
    val Bulb by lazy {
        vector("bulb", listOf("M12 3.4a5.8 5.8 0 0 0-3.4 10.5c.7.5 1.1 1.3 1.1 2.1h4.6c0-.8.4-1.6 1.1-2.1A5.8 5.8 0 0 0 12 3.4zM9.9 19.2h4.2M10.6 21.2h2.8"))
    }
    val Gear by lazy {
        vector("gear", listOf(circle(12f, 12f, 3.2f), "M19.4 12a7.4 7.4 0 0 0-.1-1.2l2-1.5-1.9-3.3-2.3 1a7.4 7.4 0 0 0-2.1-1.2l-.3-2.4h-3.8l-.3 2.4a7.4 7.4 0 0 0-2.1 1.2l-2.3-1L4.3 9.3l2 1.5a7.4 7.4 0 0 0 0 2.4l-2 1.5 1.9 3.3 2.3-1a7.4 7.4 0 0 0 2.1 1.2l.3 2.4h3.8l.3-2.4a7.4 7.4 0 0 0 2.1-1.2l2.3 1 1.9-3.3-2-1.5c.1-.4.1-.8.1-1.2z"))
    }
    val Note by lazy { vector("note", listOf("M5.4 3.6h13.2v16.8H5.4z", "M8.6 8.4h6.8M8.6 12h6.8M8.6 15.6h4")) }
    val Logout by lazy {
        vector("logout", listOf("M9.4 20.4H5.6a2 2 0 0 1-2-2V5.6a2 2 0 0 1 2-2h3.8M15.6 16.4 20 12l-4.4-4.4M20 12H9.4"))
    }
    val Pause by lazy { vector("pause", listOf("M9 5.5v13M15 5.5v13"), strokeWidth = 2.4f) }
    val Flame by lazy {
        vector("flame", listOf("M12 2.8c.6 3.2 2.4 4.2 3.9 5.8a6.9 6.9 0 0 1 2 4.9 7.9 7.9 0 1 1-15.8 0c0-2 .9-3.4 2.2-2.6.9.6 1 1.9.8 3 1.9-1.6 3.1-4.4 3.1-7.2 0-1.6.9-3 3.8-3.9z"), strokeWidth = 1.7f)
    }
    val Smile by lazy {
        vector("smile", listOf(circle(12f, 12f, 8.7f), "M8.4 14.2a4.4 4.4 0 0 0 7.2 0"), listOf(circle(9.3f, 9.6f, 1.1f), circle(14.7f, 9.6f, 1.1f)))
    }
    val Info by lazy { vector("info", listOf(circle(12f, 12f, 8.6f), "M12 11.2v5M12 7.9h.02")) }

    /** Resolves the icon used by a habit or reminder from its stored key. */
    fun byKey(key: String): ImageVector = when (key.lowercase()) {
        "water" -> Water
        "meal" -> Meal
        "gym", "dumbbell" -> Dumbbell
        "meditation", "wave" -> Wave
        "walk" -> Walk
        "sleep", "moon" -> Moon
        "stretch" -> Stretch
        "coffee" -> Coffee
        "blood" -> Blood
        "cholesterol", "heartpulse" -> HeartPulse
        "dental", "tooth" -> Tooth
        "eye" -> Eye
        "play" -> Play
        "music" -> Music
        "target" -> Target
        "book" -> Book
        "sprout" -> Sprout
        else -> Sparkle
    }
}
