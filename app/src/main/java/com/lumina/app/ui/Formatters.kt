package com.lumina.app.ui

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/** Minutes-from-midnight to "6:30 AM". */
fun formatMinutes(minutes: Int): String {
    val hour24 = (minutes / 60) % 24
    val minute = minutes % 60
    val period = if (hour24 < 12) "AM" else "PM"
    val hour = if (hour24 % 12 == 0) 12 else hour24 % 12
    return String.format(Locale.getDefault(), "%d:%02d %s", hour, minute, period)
}

/** "4:00 PM – 4:45 PM" */
fun formatRange(startMinutes: Int, endMinutes: Int): String =
    "${formatMinutes(startMinutes)} – ${formatMinutes(endMinutes)}"

/** "45 min" / "1h 20m" */
fun formatDuration(minutes: Int): String = when {
    minutes < 60 -> "$minutes min"
    minutes % 60 == 0 -> "${minutes / 60}h"
    else -> "${minutes / 60}h ${minutes % 60}m"
}

fun formatDate(millis: Long, pattern: String = "d MMMM yyyy"): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(Date(millis))

fun todayLabel(): String = formatDate(System.currentTimeMillis(), "EEEE, d MMMM")

fun nowMinutes(): Int = Calendar.getInstance().let {
    it.get(Calendar.HOUR_OF_DAY) * 60 + it.get(Calendar.MINUTE)
}

fun daysUntil(millis: Long): Int {
    val diff = millis - System.currentTimeMillis()
    return TimeUnit.MILLISECONDS.toDays(diff).toInt().coerceAtLeast(0)
}

/** "Due tomorrow · 11:59 PM" — the phrasing used on deadline cards. */
fun formatDueLabel(millis: Long): String {
    val days = daysUntil(millis)
    val time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(millis))
    return when (days) {
        0 -> "Due today · $time"
        1 -> "Due tomorrow · $time"
        else -> "Due ${formatDate(millis, "d MMMM")} · $time"
    }
}

fun greetingForNow(): String = when (nowMinutes() / 60) {
    in 0..11 -> "Good morning"
    in 12..17 -> "Good afternoon"
    else -> "Good evening"
}

/** True when [millis] falls on today's calendar date. */
fun isToday(millis: Long): Boolean {
    val then = Calendar.getInstance().apply { timeInMillis = millis }
    val now = Calendar.getInstance()
    return then.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
        then.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
}
