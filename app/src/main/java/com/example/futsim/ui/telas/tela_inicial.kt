package com.example.futsim.ui.telas

import com.example.futsim.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.futsim.ui.componentes.ButtonUniversal

@Composable
fun TelaInicial(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // --- LOGO ADICIONADA AQUI ---
        Image(
            painter = painterResource(id = R.drawable.futsim),
            contentDescription = "Logo do FutSim",
            modifier = Modifier.size(600.dp) // <-- Você pode ajustar o tamanho aqui
        )

        // Espaço entre a logo e o botão
        Spacer(modifier = Modifier.height(30.dp))

        // Botão que já existia
        ButtonUniversal(
            text = stringResource(id = R.string.iniciar_simulador),
            onClick = { navHostController.navigate("tela_principal") },
            modifier = Modifier.fillMaxWidth(),
            buttonColor = MaterialTheme.colorScheme.primary
        )
    }
}