package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun TelaPrincipal (navHostController: NavHostController){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =  androidx.compose.ui.Alignment.CenterHorizontally){

        Text("TELA PRINCIPAL ON")
        Button(onClick = {navHostController.navigate("tela_teste")}) {
            Text("TELA TESTE")

        }
    }

}
