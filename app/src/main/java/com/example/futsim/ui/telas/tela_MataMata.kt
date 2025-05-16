package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.futsim.ui.componentes.ButtonUniversal

@Composable
fun TelaMataMata(navHostController: NavHostController){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text("Tela MATA MATA")




        ButtonUniversal(
            text = "tela principal",
            onClick = {navHostController.navigate("tela_Principal")},
            modifier = Modifier.padding(top = 10.dp, bottom = 50.dp),

        )

    }
}
