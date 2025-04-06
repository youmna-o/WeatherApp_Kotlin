package com.example.weatherapp.notifications
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.example.weatherapp.mainActivity.MainActivity

class RunnerNotifier(
    private val notificationManager: NotificationManager,
    private val context: Context,
) : Notifier(notificationManager,context) {

    override val notificationChannelId: String = "runner_channel_id"
    override val notificationChannelName: String = "Running Notification"
    override val notificationId: Int = 200

    override fun buildNotification(title:String,describtion:String,reminderItem: ReminderItem): Notification {
        val fullScreenIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("DESTINATION", "weather_screen")
            putExtra("LAT", reminderItem.lat)
            putExtra("LON", reminderItem.lon)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val cancelIntent = Intent(context, AlarmReceiver::class.java).apply {
            action="Cancel"
            putExtra("REMINDER_ID", reminderItem.id)
            putExtra("REMINDER_TIME", reminderItem.time)

        }

        val cancelPendingIntent = PendingIntent.getBroadcast(
            context, reminderItem.id, cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val snoozeIntent = Intent(context, AlarmReceiver::class.java).apply {
            action="Snooze"
            putExtra("REMINDER_ID", reminderItem.id)
            putExtra("REMINDER_TIME", reminderItem.time)
        }

        val snoozePendingIntent = PendingIntent.getBroadcast(
            context, reminderItem.id, snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/cinematic")
        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(getNotificationTitle(title))
            .setContentText(getNotificationMessage(describtion))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSound(soundUri)
            .setAutoCancel(true)
            .addAction(android.R.drawable.ic_menu_info_details,"Snooze",snoozePendingIntent)
            .addAction(android.R.drawable.ic_menu_info_details,"Cancel",cancelPendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()
    }


    override fun getNotificationTitle(message: String): String {
        return message
    }

    override fun getNotificationMessage(message: String): String {
        return message
    }
}