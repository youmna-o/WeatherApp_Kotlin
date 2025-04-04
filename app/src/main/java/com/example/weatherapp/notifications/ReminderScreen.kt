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
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.weatherapp.ui.theme.myPurple

@Composable
fun ReminderScreen(notificationAlarmScheduler: NotificationAlarmScheduler) {
    val context = LocalContext.current

    val calendar = remember { Calendar.getInstance() }
    val selectedTime = remember { mutableStateOf("") }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            selectedTime.value = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calendar.time)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            timePickerDialog.show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        //to enable previous days
        datePicker.minDate = System.currentTimeMillis()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { datePickerDialog.show() },
            colors =  ButtonDefaults.buttonColors(
            containerColor = myPurple,
            contentColor = Color.White
        )) {
            Text(text = "Pick to choose your alert time")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTime.value.isNotEmpty()) {
            Text(text = " ${selectedTime.value}", fontSize = 18.sp)

            Button(
                onClick = {
                    val reminderItem = ReminderItem(
                        time = calendar.timeInMillis,
                        id = 200,
                        lat = 36.08725,
                        lon = 4.45192,

                        )
                    Log.d("ReminderTime", "Reminder set for: ${reminderItem.time}")
                    notificationAlarmScheduler.schedule(reminderItem)
                },colors =  ButtonDefaults.buttonColors(
                    containerColor = myPurple,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Set your alert")
            }
        }
    }
}
