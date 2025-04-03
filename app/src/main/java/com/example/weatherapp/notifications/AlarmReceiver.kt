package com.example.weatherapp.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.weatherapp.data.remote.RetrofitHeloer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitHeloer.apiService.getWeatherByCoord(31.0797867, 31.590905, "e13a916deb17bb538fdfabaf0d57e6a5", "en",
                   "metric",
                    )
                        val cityName = response.weather[0].main ?: "Unknown"
                        withContext(Dispatchers.Main) {
                            val notificationManager =
                                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            val runnerNotifier = RunnerNotifier(notificationManager, ctx)
                            runnerNotifier.showNotification("Weather in $cityName")
                        }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
