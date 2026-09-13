package com.lumina.app.data.model

/** A gap in the day, in minutes from midnight. */
data class FreeSlot(val startMinutes: Int, val endMinutes: Int) {
    val durationMinutes: Int get() = endMinutes - startMinutes
}
