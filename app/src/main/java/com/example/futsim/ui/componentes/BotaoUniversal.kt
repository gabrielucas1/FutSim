package com.example.futsim.ui.componentes


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape


@Composable
fun ButtonUniversal(
    texto: String,
    onClick:() -> Unit,
    modifier: Modifier = Modifier,
){
    Button(onClick = onClick, modifier = modifier,shape = RectangleShape,
    ){
        Text(texto)
    }

}