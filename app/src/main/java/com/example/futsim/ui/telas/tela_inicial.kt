package com.example.futsim.ui.telas


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.futsim.ui.componentes.ButtonUniversal

@Composable
fun TelaInicial (navHostController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.tertiary)
            .padding(8.dp),
             verticalArrangement = Arrangement.Center,
             horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ButtonUniversal(
            texto = "INICIAR SIMULADOR",
            onClick = { navHostController.navigate("tela_principal")},
            modifier = Modifier.padding(top = 400.dp).width(380.dp),

            )

    }
}


