package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.futsim.ui.componentes.ButtonUniversal

@Composable
fun TelaPrincipal (navHostController: NavHostController){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =  androidx.compose.ui.Alignment.CenterHorizontally){

        ButtonUniversal( text = "CRIAR CAMPEONATO",
            onClick = {navHostController.navigate("tela_CriandoCamp")},
            modifier = Modifier.padding(top = 10.dp, bottom = 50.dp).width(380.dp).height(45.dp),
            fontSize = 30.sp,

            )

        ButtonUniversal( text = "CAMPEONATOS CRIADOS",
            onClick = {navHostController.navigate("tela_CampCriados")},
            modifier = Modifier.padding(top = 2.dp, bottom = 80.dp).width(380.dp).height(45.dp),
            fontSize = 28.sp,

            )


        ButtonUniversal(
            text = "Tela Inicial",
                onClick = {navHostController.navigate("tela_Inicial")},
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 50.dp)
                .width(135.dp)
                .height(60.dp)
        )





    }

}
