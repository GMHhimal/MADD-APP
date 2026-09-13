package com.lumina.app.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.lumina.app.data.model.NotificationType

/**
 * Fires scheduled reminders and handles the "dismiss / snooze" action buttons.
 */
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_DISMISS -> {
                val id = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
                if (id != -1) NotificationManagerCompat.from(context).cancel(id)
            }

            ACTION_FIRE -> {
                val typeName = intent.getStringExtra(EXTRA_TYPE) ?: return
                val type = runCatching { NotificationType.valueOf(typeName) }.getOrNull() ?: return
                LuminaNotifications.show(
                    context = context,
                    type = type,
                    title = intent.getStringExtra(EXTRA_TITLE).orEmpty(),
                    body = intent.getStringExtra(EXTRA_BODY).orEmpty(),
                    route = intent.getStringExtra(EXTRA_ROUTE).orEmpty(),
                    primaryAction = intent.getStringExtra(EXTRA_PRIMARY) ?: "Open",
                    secondaryAction = intent.getStringExtra(EXTRA_SECONDARY) ?: "Dismiss"
                )
            }
        }
    }

    companion object {
        const val ACTION_FIRE = "com.lumina.app.FIRE_REMINDER"
        const val ACTION_DISMISS = "com.lumina.app.DISMISS_REMINDER"

        const val EXTRA_TYPE = "type"
        const val EXTRA_TITLE = "title"
        const val EXTRA_BODY = "body"
        const val EXTRA_ROUTE = "route"
        const val EXTRA_PRIMARY = "primary"
        const val EXTRA_SECONDARY = "secondary"
        const val EXTRA_NOTIFICATION_ID = "notificationId"

        fun dismissIntent(context: Context, notificationId: Int): PendingIntent {
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                action = ACTION_DISMISS
                putExtra(EXTRA_NOTIFICATION_ID, notificationId)
            }
            return PendingIntent.getBroadcast(
                context, notificationId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
