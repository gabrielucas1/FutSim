package com.example.futsim.ui.componentes


import android.graphics.fonts.Font
import androidx.annotation.Size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ButtonDefaults


@Composable
fun ButtonUniversal(
    text: String,
    onClick:() -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 20.sp,
    textColor: Color = Color.White,
    buttonColor: Color = Color.Black

){
    Button(onClick = onClick, modifier = modifier,shape = RectangleShape,colors = ButtonDefaults.buttonColors(containerColor = buttonColor)

    ){
        Text(text = text, fontSize = fontSize, color = textColor)
    }

}