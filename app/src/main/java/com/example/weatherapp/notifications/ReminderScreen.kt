package com.example.weatherapp.notifications

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ReminderScreen(notificationAlarmScheduler: NotificationAlarmScheduler) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val reminderItem = ReminderItem(
                    time = Calendar.getInstance().apply {
                        add(Calendar.SECOND, 10)
                    }.timeInMillis,
                    id = 10,
                )
                Log.d("ReminderTime", "Reminder set for: ${reminderItem.time}")
                notificationAlarmScheduler.schedule(reminderItem)
            }
        ) {
            Text(text = "Set Reminder (After 1 Min)")
        }
    }
}