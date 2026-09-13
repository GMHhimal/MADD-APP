package com.lumina.app.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.lumina.app.MainActivity
import com.lumina.app.R
import com.lumina.app.data.model.NotificationType

/**
 * Lumina's smart notification system.
 *
 * Seven channels, one per alert type, so the user can silence gym nudges without losing
 * deadline alerts. Every notification carries two actions — nothing is a dead end.
 */
object LuminaNotifications {

    const val EXTRA_ROUTE = "lumina_route"

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        NotificationType.entries.forEach { type ->
            val importance = when (type) {
                NotificationType.DEADLINE, NotificationType.HEALTH -> NotificationManager.IMPORTANCE_HIGH
                else -> NotificationManager.IMPORTANCE_DEFAULT
            }
            val channel = NotificationChannel(type.channelId, type.channelName, importance).apply {
                description = descriptionFor(type)
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)
        }
    }

    private fun descriptionFor(type: NotificationType) = when (type) {
        NotificationType.HABIT -> "Gentle nudges for the habits you are tracking."
        NotificationType.GYM -> "Reminders shortly before a planned workout."
        NotificationType.MEAL -> "Reminders for your protected meal breaks."
        NotificationType.DEADLINE -> "Alerts when a deadline is close and progress is behind."
        NotificationType.FREE_TIME -> "Suggestions when a gap opens up in your day."
        NotificationType.WELLNESS -> "Mood check-ins and break suggestions."
        NotificationType.HEALTH -> "Reminders for checkups you have added yourself."
    }

    fun hasPermission(context: Context): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

    private fun contentIntent(context: Context, route: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_ROUTE, route)
        }
        return PendingIntent.getActivity(
            context, route.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /**
     * @param route where tapping the notification should land in the app
     * @param primaryAction / [secondaryAction] the two inline buttons
     */
    fun show(
        context: Context,
        type: NotificationType,
        title: String,
        body: String,
        route: String,
        primaryAction: String,
        secondaryAction: String,
        notificationId: Int = type.ordinal + 100
    ) {
        if (!hasPermission(context)) return

        val builder = NotificationCompat.Builder(context, type.channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(0xFF3E8EDE.toInt())
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(
                if (type == NotificationType.DEADLINE) NotificationCompat.PRIORITY_HIGH
                else NotificationCompat.PRIORITY_DEFAULT
            )
            .setAutoCancel(true)
            .setContentIntent(contentIntent(context, route))
            .addAction(0, primaryAction, contentIntent(context, route))
            .addAction(
                0, secondaryAction,
                ReminderReceiver.dismissIntent(context, notificationId)
            )

        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (_: SecurityException) {
            // Permission revoked between the check above and posting. Nothing to do.
        }
    }

    // ---- the seven prototype notifications ------------------------------------

    fun habitReminder(context: Context, habitTitle: String, detail: String) = show(
        context, NotificationType.HABIT,
        title = "Time to $habitTitle".lowercase().replaceFirstChar { it.uppercase() },
        body = detail,
        route = "habits", primaryAction = "Mark done", secondaryAction = "Snooze 15 min"
    )

    fun gymReminder(context: Context, minutesUntil: Int) = show(
        context, NotificationType.GYM,
        title = "Gym in $minutesUntil minutes",
        body = "Push day · Mon / Wed / Fri. Bag packed?",
        route = "habits", primaryAction = "I'm going", secondaryAction = "Reschedule"
    )

    fun mealReminder(context: Context, meal: String, time: String) = show(
        context, NotificationType.MEAL,
        title = "$meal time",
        body = "You planned $meal for $time.",
        route = "habits", primaryAction = "Mark eaten", secondaryAction = "Later"
    )

    fun deadlineAlert(context: Context, title: String, progress: Int) = show(
        context, NotificationType.DEADLINE,
        title = "$title due tomorrow",
        body = "You're $progress% done. 25 focused minutes today keeps you on track.",
        route = "plan", primaryAction = "Start focus", secondaryAction = "View task"
    )

    fun freeTimeSuggestion(context: Context, minutes: Int, at: String) = show(
        context, NotificationType.FREE_TIME,
        title = "$minutes minutes free at $at",
        body = "Continue where you left off, or take a short walk?",
        route = "freetime", primaryAction = "See options", secondaryAction = "Not now"
    )

    fun wellnessCheckIn(context: Context) = show(
        context, NotificationType.WELLNESS,
        title = "How are you feeling today?",
        body = "A quick check-in helps Lumina plan a better evening.",
        route = "mood", primaryAction = "Check in", secondaryAction = "Later"
    )

    fun healthReminder(context: Context, title: String, daysAway: Int) = show(
        context, NotificationType.HEALTH,
        title = "$title in $daysAway days",
        body = "Tap to see the last date and reschedule if you need to.",
        route = "health", primaryAction = "View details", secondaryAction = "Remind me later"
    )
}
