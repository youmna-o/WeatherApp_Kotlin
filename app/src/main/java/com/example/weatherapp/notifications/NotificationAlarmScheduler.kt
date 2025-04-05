package com.example.weatherapp.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


class NotificationAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun createPendingIntent(reminderItem: ReminderItem): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)

        return PendingIntent.getBroadcast(
            context,
            reminderItem.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    override fun schedule(reminderItem: ReminderItem) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            reminderItem.time,
            createPendingIntent(reminderItem)
        )
    }

    override fun cancel(reminderItem: ReminderItem) {
        alarmManager.cancel(
            createPendingIntent(reminderItem)
        )
    }
}