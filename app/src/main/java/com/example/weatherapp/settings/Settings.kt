package com.example.weatherapp.settings

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Settings(){
    val locationOptions = listOf("GPS", "Map")
    val lableLocation="Location"
    val languageOptions = listOf("English", "Arabic")
    val lableLanguage="Language"
    val windOptions = listOf("meter/sec", "mile/h")
    val lableWind="Wind Speed"
    val temperatureOptions= listOf("Celsius","kelvin", "Fahrenheit")
    val lableTemperature="Temperature"
    Column (modifier = Modifier.fillMaxSize()){
        MenueCard(locationOptions,lableLocation,140)
        MenueCard(languageOptions,lableLanguage,140)
        MenueCard(windOptions,lableWind,140)
        MenueCard(temperatureOptions,lableTemperature,180)
    }

}
@Composable
fun RadioButtonSingleSelection(modifier: Modifier = Modifier,radioOptions:List<String>,lable:String) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Column(modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
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
fun MenueCard(radioOptions:List<String>,lable:String,height:Int){
    Spacer(modifier = Modifier.height(4.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(16.dp)),
             colors = CardDefaults.cardColors(
            containerColor = Color.Blue.copy(alpha = 0.3f)
        ),
        ) {
        Column (){
            Text(lable, fontSize = 28.sp, modifier = Modifier.padding(start = 20.dp))
            RadioButtonSingleSelection(radioOptions=radioOptions, lable = lable)
        }
    }
}