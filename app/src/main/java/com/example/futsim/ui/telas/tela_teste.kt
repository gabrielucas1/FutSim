package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@Composable
fun TelaTeste(navHostController: NavHostController){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =  androidx.compose.ui.Alignment.CenterHorizontally){

        Text("TELA TESTEEE ON")
        Button(onClick = {navHostController.navigate("tela_inicial")}) {
            Text("TELA PRINCIPAL")

        }
    }

}
