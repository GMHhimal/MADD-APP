package com.lumina.app.data.model

/** How the user rated their day. Order matters: it drives the mood scale and the trend chart. */
enum class Mood(val label: String, val score: Int) {
    GREAT("Great", 5),
    GOOD("Good", 4),
    OKAY("Okay", 3),
    LOW("Low", 2),
    STRESSED("Stressed", 1)
}

enum class Priority(val label: String) { HIGH("High"), MEDIUM("Medium"), LOW("Low") }

/** Drives the visual treatment of a timeline row. */
enum class EventKind { FIXED, FLEXIBLE, OPTIONAL, FREE }

enum class NotificationType(val channelId: String, val channelName: String) {
    HABIT("lumina_habit", "Habit reminders"),
    GYM("lumina_gym", "Gym reminders"),
    MEAL("lumina_meal", "Meal reminders"),
    DEADLINE("lumina_deadline", "Deadline alerts"),
    FREE_TIME("lumina_freetime", "Free-time suggestions"),
    WELLNESS("lumina_wellness", "Wellness check-ins"),
    HEALTH("lumina_health", "Health reminders")
}
