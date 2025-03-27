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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.myBlue
import com.example.weatherapp.weatherScreen.WeatherDetailsViewModel


@Composable
fun Settings(viewModel: WeatherDetailsViewModel){
    val context= LocalContext.current
     val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

    val locationState by viewModel.locationMethod.collectAsStateWithLifecycle()
    val langState by viewModel.lang.collectAsStateWithLifecycle()
    val selectedTemperature by viewModel.temp.collectAsStateWithLifecycle()
    val selectedWind by viewModel.wind.collectAsStateWithLifecycle()
    val unitState by viewModel.unit.collectAsStateWithLifecycle()

    val savedLanguage = sharedPreferences.getString(stringResource(R.string.lang), stringResource(R.string.en))?:stringResource(R.string.en)
    val savedTemperature = sharedPreferences.getString(
        stringResource(R.string.temp),
        stringResource(R.string.celsius)
    ) ?: stringResource(R.string.celsius)
    val savedWind = sharedPreferences.getString("wind", stringResource(R.string.meter_sec),) ?:stringResource(R.string.meter_sec)


    val locationOptions = listOf(stringResource(R.string.gps), stringResource(R.string.map))
    val lableLocation= stringResource(R.string.location)
    val languageOptions = listOf(stringResource(R.string.en), stringResource(R.string.ar))
    val lableLanguage= stringResource(R.string.language)
    val windOptions = listOf(stringResource(R.string.meter_sec), stringResource(R.string.mile_h))
    val lableWind= stringResource(R.string.wind_speed)
    val temperatureOptions= listOf(
        stringResource(R.string.celsius),
        stringResource(R.string.kelvin), stringResource(R.string.fahrenheit))
    val lableTemperature= stringResource(R.string.temperature)

    Column (modifier = Modifier.fillMaxSize()){
        MenueCard(locationOptions,lableLocation,140,{
            editor.putString("locationMethod",it)
            editor.apply()
            viewModel.updateParameters(locationState,it,selectedTemperature,selectedWind)
        },locationState)
        MenueCard(languageOptions,lableLanguage,140,{
            editor.putString("lang",it)
            editor.apply()
            viewModel.updateParameters(locationState,it,selectedTemperature,selectedWind)
        },savedLanguage)
        MenueCard(windOptions,lableWind,140,{
            editor.putString("wind",it)
            editor.apply()
            viewModel.updateParameters(locationState,langState,selectedTemperature,it)
        },savedWind)
        MenueCard(temperatureOptions,lableTemperature,180,{
            editor.putString("temp",it)
            editor.apply()
            viewModel.updateParameters(locationState,langState,it,selectedWind)
        },savedTemperature)
       //println(sharedPreferences.getStringSet(selectedTemperature,""))
    }

}
@Composable
fun RadioButtonSingleSelection(modifier: Modifier = Modifier,radioOptions:List<String>,lable:String,action: (String) -> Unit,defultOption:String) {
    val selectedOption = remember(defultOption) { mutableStateOf(defultOption) }
   // val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Column(modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .selectable(
                        selected = (text == selectedOption.value),
                        onClick = {
                            selectedOption.value = text
                            action(text)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    //to save my last choice in default value
                    selected = (text == selectedOption.value),
                    onClick = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
@Composable
fun MenueCard(radioOptions:List<String>,lable:String,height:Int,action: (String) -> Unit ,defultOption: String){
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
            RadioButtonSingleSelection(radioOptions=radioOptions, lable = lable, action = action, defultOption = defultOption)
        }
    }
}