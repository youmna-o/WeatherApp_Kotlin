package com.example.weatherapp.notifications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun NotificationScreen(notificationAlarmScheduler: NotificationAlarmScheduler){
    ReminderScreen(notificationAlarmScheduler)
    Box (modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "NotificationScreen ",
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
