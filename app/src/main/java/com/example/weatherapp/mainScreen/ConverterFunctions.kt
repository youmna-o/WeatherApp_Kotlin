package com.example.weatherapp.mainScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun getDayName(dateString: String?): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
    val date = inputFormat.parse(dateString)
    return outputFormat.format(date!!)
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun getHourFromDateTime(dateTime: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val parsedDateTime = LocalDateTime.parse(dateTime, formatter)
    return parsedDateTime.hour.toString()

}

@RequiresApi(Build.VERSION_CODES.O)
fun getDayDate(dateTime: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val parsedDateTime = LocalDateTime.parse(dateTime, inputFormatter)
    val nextDay = parsedDateTime.plus(1, ChronoUnit.DAYS)
    val dayAbbreviation = nextDay.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("d MMMM", Locale.ENGLISH)
    val formattedDate = nextDay.format(outputFormatter)
    return "$dayAbbreviation, $formattedDate"
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherIcon(iconCode: String) {
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    GlideImage(
        model = iconUrl,
        contentDescription = "image",
        Modifier
            .size(40.dp)
            .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()

    )
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MediumIcon(iconCode: String) {
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    GlideImage(
        model = iconUrl,
        contentDescription = "image",
        Modifier
            .size(60.dp)
            .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()

    )
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BigIcon(iconCode: String) {
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    GlideImage(
        model = iconUrl,
        contentDescription = "image",
        Modifier
            .size(100.dp)
            .border(4.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()

    )
}
@SuppressLint("SuspiciousIndentation")
@Composable
fun TempFromKToC(k: Double?):Int{
  var c =  k!!-273.15
    return c.toInt()
}