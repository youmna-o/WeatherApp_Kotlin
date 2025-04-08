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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.myOrange
import com.example.weatherapp.ui.theme.myPurple
import com.example.weatherapp.utils.NetworkUtils.isNetworkAvailable
import java.util.Date

@Composable
fun ReminderScreen(notificationAlarmScheduler: NotificationAlarmScheduler) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val calendar = remember { Calendar.getInstance() }

    val reminderList = remember { mutableStateListOf<ReminderItem>() }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            val reminderItem = ReminderItem(
                time = calendar.timeInMillis,
                id = (calendar.timeInMillis % Int.MAX_VALUE).toInt(),
                lat = 36.08725,
                lon = 4.45192,
            )
            reminderList.add(reminderItem)
            notificationAlarmScheduler.schedule(reminderItem)
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
        datePicker.minDate = System.currentTimeMillis()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(

            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable() {
                    if (isNetworkAvailable(context)) {
                        datePickerDialog.show()
                    } else {
                        showDialog = true
                    }
                }
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.3f)
            )){
            Text(text = stringResource(R.string.pick_to_choose_your_alert_time), maxLines = 1, fontSize = 24.sp,textAlign= TextAlign.Center, modifier = Modifier.padding(start = 8.dp, top = 8.dp))

        }


        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(reminderList) { item ->
                ReminderListItem(item) {
                    reminderList.remove(item)
                    notificationAlarmScheduler.cancel(item)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            containerColor = myPurple,
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.warning)) },
            text = {
                Text(stringResource(R.string.you_can_t_set_notification_with_future_information_about_weather_without_with_out_internet))
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myOrange,
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}
@Composable
fun ReminderListItem(item: ReminderItem, onDelete: () -> Unit) {
    val formattedTime = SimpleDateFormat("dd MMM yyyy - hh:mm a", Locale.getDefault())
        .format(Date(item.time))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.3f)
        ),

        ){
        Row {
            Text(text = formattedTime, fontSize = 16.sp, modifier = Modifier.padding(all = 16.dp))
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Reminder",
                    tint = myPurple
                )
            }
        }
    }

}