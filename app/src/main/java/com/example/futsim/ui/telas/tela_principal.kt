package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth // Import corrigido
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.futsim.R
import com.example.futsim.ui.componentes.ButtonUniversal

@Composable
fun TelaPrincipal(navHostController: NavHostController) { // Nome da função corrigido
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonUniversal(
            text = stringResource(R.string.criar_campeonato),
            onClick = { navHostController.navigate("tela_CriandoCamp") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ButtonUniversal(
            text = stringResource(R.string.campeonatos_criados),
            onClick = { navHostController.navigate("tela_CampCriados") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}