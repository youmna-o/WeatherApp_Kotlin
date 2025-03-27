package com.example.weatherapp
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.example.weatherapp.data.model.ForecastData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.repo.Repo
import com.example.weatherapp.mainActivity.ShowNavBar
import com.example.weatherapp.ui.theme.WeatherAppTheme
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
     WeatherAppTheme {
         val currentContext = LocalContext.current
       /*  Box(
             modifier = Modifier
                 .fillMaxSize()
         ) {
             //Image(painter = painterResource(R.drawable.background), contentDescription = "nn",Modifier.fillMaxWidth())
             val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash2  ))
             val progress by animateLottieCompositionAsState(
                 composition,
                 iterations = LottieConstants.IterateForever
             )

             LottieAnimation(
                 composition = composition,
                 progress = { progress },
                 modifier = Modifier.fillMaxSize(),
                 contentScale = ContentScale.FillBounds
             )

         }*/
         ShowNavBar(this, application = application)

     }

     }


    }

}
