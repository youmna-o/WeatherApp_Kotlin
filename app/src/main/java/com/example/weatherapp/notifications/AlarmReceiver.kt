package com.example.weatherapp.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.weatherapp.data.remote.RetrofitHeloer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var reminderItem:ReminderItem
        val id = intent!!.getIntExtra("REMINDER_ID", -1)
        val time = intent!!.getLongExtra("REMINDER_TIME", 0L)
        val lat = intent!!.getDoubleExtra("LAT",36.08725 )
        val lon = intent!!.getDoubleExtra("LON", 4.45192)
        reminderItem = ReminderItem(time,id,lat,lon)
        when(intent?.action){
            "Cancel" ->{
                val scheduler = NotificationAlarmScheduler(context!!)
                scheduler.cancel(reminderItem)
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(200)
            }

            "Snooze"->{
                CoroutineScope(Dispatchers.IO).launch{
                     delay(50000)
                    sendNotification(context,reminderItem)
                }
            }
            else ->{
                sendNotification(context,reminderItem)
            }

        }

    }
    fun sendNotification(context: Context?,reminderItem: ReminderItem){
        context?.let { ctx ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitHeloer.apiService.getWeatherByCoord(36.08725, 4.45192, "e13a916deb17bb538fdfabaf0d57e6a5", "en",
                        "metric",
                    )
                    val cityName = response.name
                    val discride=response.weather[0].description ?: "Unknown"
                    withContext(Dispatchers.Main) {
                        val notificationManager =
                            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val runnerNotifier = RunnerNotifier(notificationManager, ctx)
                        runnerNotifier.showNotification("${cityName}","Weather in ${cityName} now is  $discride ",reminderItem =reminderItem )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

}
