package com.example.weatherapp.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.example.weatherapp.MainActivity

class RunnerNotifier(
    private val notificationManager: NotificationManager,
    private val context: Context
) : Notifier(notificationManager,context) {

    override val notificationChannelId: String = "runner_channel_id"
    override val notificationChannelName: String = "Running Notification"
    override val notificationId: Int = 200

    override fun buildNotification(message:String): Notification {
        val fullScreenIntent = Intent(context, MainActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val soundUri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/cinematic")


        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(getNotificationTitle(message))
            .setContentText(getNotificationMessage(message))
            .setSmallIcon(android.R.drawable.btn_star)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()
    }


    override fun getNotificationTitle(message: String): String {
        return message
    }

    override fun getNotificationMessage(message: String): String {
        return "You are ready to go for a run?"
    }
}