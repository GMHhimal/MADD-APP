package com.lumina.app.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.lumina.app.data.model.NotificationType
import java.util.Calendar

/**
 * Schedules the recurring reminders derived from the user's routine.
 *
 * Uses inexact alarms deliberately: a nudge to drink water does not justify asking the
 * user for the exact-alarm permission, and Android batches inexact alarms to save battery.
 */
object ReminderScheduler {

    private fun alarmManager(context: Context) =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun pendingIntent(
        context: Context,
        requestCode: Int,
        type: NotificationType,
        title: String,
        body: String,
        route: String,
        primary: String,
        secondary: String
    ): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ReminderReceiver.ACTION_FIRE
            putExtra(ReminderReceiver.EXTRA_TYPE, type.name)
            putExtra(ReminderReceiver.EXTRA_TITLE, title)
            putExtra(ReminderReceiver.EXTRA_BODY, body)
            putExtra(ReminderReceiver.EXTRA_ROUTE, route)
            putExtra(ReminderReceiver.EXTRA_PRIMARY, primary)
            putExtra(ReminderReceiver.EXTRA_SECONDARY, secondary)
        }
        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /** Next occurrence of [minutesFromMidnight], today if it hasn't passed yet. */
    private fun nextTrigger(minutesFromMidnight: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, minutesFromMidnight / 60)
            set(Calendar.MINUTE, minutesFromMidnight % 60)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (target.timeInMillis <= now.timeInMillis) target.add(Calendar.DAY_OF_YEAR, 1)
        return target.timeInMillis
    }

    fun scheduleDaily(
        context: Context,
        requestCode: Int,
        minutesFromMidnight: Int,
        type: NotificationType,
        title: String,
        body: String,
        route: String,
        primary: String,
        secondary: String
    ) {
        alarmManager(context).setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            nextTrigger(minutesFromMidnight),
            AlarmManager.INTERVAL_DAY,
            pendingIntent(context, requestCode, type, title, body, route, primary, secondary)
        )
    }

    fun cancel(context: Context, requestCode: Int, type: NotificationType) {
        alarmManager(context).cancel(
            pendingIntent(context, requestCode, type, "", "", "", "", "")
        )
    }

    /** Sets up the routine reminders from the user's onboarding answers. */
    fun scheduleRoutine(
        context: Context,
        breakfastMinutes: Int,
        lunchMinutes: Int,
        dinnerMinutes: Int,
        exerciseMinutes: Int,
        sleepMinutes: Int
    ) {
        scheduleDaily(context, 1, breakfastMinutes, NotificationType.MEAL,
            "Breakfast time", "You planned breakfast for this time.", "habits", "Mark eaten", "Later")
        scheduleDaily(context, 2, lunchMinutes, NotificationType.MEAL,
            "Lunch time", "You planned lunch for this time.", "habits", "Mark eaten", "Later")
        scheduleDaily(context, 3, dinnerMinutes, NotificationType.MEAL,
            "Dinner time", "Your evening meal break starts now.", "habits", "Mark eaten", "Later")
        scheduleDaily(context, 4, (exerciseMinutes - 30).coerceAtLeast(0), NotificationType.GYM,
            "Gym in 30 minutes", "Push day. Bag packed?", "habits", "I'm going", "Reschedule")
        scheduleDaily(context, 5, (sleepMinutes - 30).coerceAtLeast(0), NotificationType.WELLNESS,
            "Time to wind down", "Screens off soon so you can sleep on schedule.", "mood", "Check in", "Later")
    }
}
