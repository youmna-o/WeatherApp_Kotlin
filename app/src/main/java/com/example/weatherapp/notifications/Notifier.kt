package com.example.weatherapp.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

abstract class Notifier(
    private val notificationManager: NotificationManager,
    private val context :Context
) {
    abstract val notificationChannelId: String
    abstract val notificationChannelName: String
    abstract val notificationId: Int

    fun showNotification(message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createNotificationChannel()
            notificationManager.createNotificationChannel(channel)
        }
        val notification = buildNotification(message)
        notificationManager.notify(
            notificationId,
            notification
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    open fun createNotificationChannel(): NotificationChannel {
        val soundUri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/raw/cinematic")

        return NotificationChannel(
            notificationChannelId,
            notificationChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifications for running reminders"
            setShowBadge(true)
            enableLights(true)
            enableVibration(true)
            setSound(soundUri, AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build())
        }
    }

    abstract fun buildNotification(message:String): Notification

    protected abstract fun getNotificationTitle(message:String): String

    protected abstract fun getNotificationMessage(message:String): String
}