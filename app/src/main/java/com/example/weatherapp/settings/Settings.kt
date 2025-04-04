package com.example.weatherapp.settings
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.map.MapScreen
import com.example.weatherapp.ui.theme.myBlue
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel


@Composable
fun Settings(viewModel: WeatherDetailsViewModel,navController: NavController){
    val context= LocalContext.current
     val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
    /*val latState by viewModel.lat.collectAsStateWithLifecycle()
    val lonState by viewModel.lon.collectAsStateWithLifecycle()*/
    val locationState by viewModel.locationMethod.collectAsStateWithLifecycle()
    val langState by viewModel.lang.collectAsStateWithLifecycle()
    val selectedTemperature by viewModel.temp.collectAsStateWithLifecycle()
    val selectedWind by viewModel.wind.collectAsStateWithLifecycle()

  //to get it again on ui from shared pref
    val savedLanguage = sharedPreferences.getString("lang","en")?:"en"
    val savedTemperature = sharedPreferences.getString("temp","Celsius")?:"Celsius"
    val savedWind = sharedPreferences.getString("wind","meter/sec") ?:"meter/sec"
    val savedMethod = sharedPreferences.getString("locationMethod","GPS")?:"GPS"


    val locationOptions = listOf("GPS", "Map")
    val lableLocation= stringResource(R.string.location)
    val languageOptions = listOf("en", "ar")
    val lableLanguage= stringResource(R.string.language)
    val windOptions = listOf("meter/sec", "mile/h")
    val lableWind= stringResource(R.string.wind_speed)
    val temperatureOptions = listOf("Celsius", "Kelvin", "Fahrenheit")
    val lableTemperature= stringResource(R.string.temperature)

    Column (modifier = Modifier.fillMaxSize()){
        MenueCard(locationOptions,lableLocation,140,{
            editor.putString("locationMethod",it)
            editor.apply()
            viewModel.updateParameters(locationState,it,selectedTemperature,selectedWind)
        },savedMethod,navController)
        MenueCard(languageOptions,lableLanguage,140,{
            editor.putString("lang",it)
            editor.apply()
            viewModel.setAppLocale(context,it)
            navController.navigate("settings_screen") {
                popUpTo("settings_screen") { inclusive = true }//to rebuild and delete old screen
            }
            viewModel.updateParameters(locationState,it,selectedTemperature,selectedWind)
        },savedLanguage,navController)
        MenueCard(windOptions,lableWind,140,{
            editor.putString("wind",it)
            editor.apply()
            viewModel.updateParameters(locationState,langState,selectedTemperature,it)
        },savedWind,navController)
        MenueCard(temperatureOptions,lableTemperature,180,{
            editor.putString("temp",it)
            editor.apply()
            viewModel.updateParameters(locationState,langState,it,selectedWind)
        },savedTemperature,navController)
    }

}
@Composable
fun RadioButtonSingleSelection(navController: NavController,modifier: Modifier = Modifier,radioOptions:List<String>,lable:String,action: (String) -> Unit,defultOption:String) {
    val context= LocalContext.current
    val selectedOption = remember(defultOption) { mutableStateOf(defultOption) }
    Column(modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            val displayText = getDisplayText(context, text)
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    //to save my last choice in default value
                    selected = (text == selectedOption.value),
                    onClick ={
                        selectedOption.value = text
                        action(text)
                        if(text=="Map"){
                            navController.navigate("map")
                        }
                    },
                )
                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
@Composable
fun MenueCard(radioOptions:List<String>, lable:String, height:Int, action:  (String) -> Unit, defultOption: String,navController: NavController){
    Spacer(modifier = Modifier.height(4.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(16.dp)),
             colors = CardDefaults.cardColors(
            containerColor = myBlue
        ),
        ) {
        Column (){
            Text(lable, fontSize = 28.sp, modifier = Modifier.padding(start = 20.dp))
            RadioButtonSingleSelection(radioOptions=radioOptions, lable = lable, action = action, defultOption = defultOption,  navController = navController)
        }

    }

}
fun getDisplayText(context: Context, key: String): String {
    return when (key) {
        "GPS" -> context.getString(R.string.gps)
        "Map" -> context.getString(R.string.map)
        "en" -> context.getString(R.string.en)
        "ar" -> context.getString(R.string.ar)
        "meter/sec" -> context.getString(R.string.meter_sec)
        "mile/h" -> context.getString(R.string.mile_h)
        "Celsius" -> context.getString(R.string.celsius)
        "Kelvin" -> context.getString(R.string.kelvin)
        "Fahrenheit" -> context.getString(R.string.fahrenheit)
        else -> key
    }
}