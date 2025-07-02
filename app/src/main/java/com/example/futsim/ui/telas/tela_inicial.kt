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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.futsim.ui.componentes.ButtonUniversal

@Composable
fun TelaInicial(navHostController: NavHostController) {
    // Adiciona um modificador de rolagem vertical para permitir que o conteúdo seja rolado
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()) // Permite a rolagem vertical
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Seção 1: Logo e Botão "INICIAR SIMULADOR"
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp), // Espaço abaixo da primeira seção
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.futsim),
                contentDescription = "Logo do FutSim",
                modifier = Modifier.size(600.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            ButtonUniversal(
                text = stringResource(id = R.string.iniciar_simulador),
                onClick = { navHostController.navigate("tela_principal") },
                modifier = Modifier.fillMaxWidth(),
                buttonColor = MaterialTheme.colorScheme.primary
            )
        }

        // Seção 2: Campeonatos Mata-Mata
        InfoSection(
            title = "Campeonatos Mata-Mata",
            description = "Simule torneios eliminatórios, como copas e ligas com fases de playoff. Acompanhe o chaveamento, resultados e o avanço dos times até a grande final!"
        )

        // Seção 3: Campeonatos Pontos Corridos
        InfoSection(
            title = "Campeonatos de Pontos Corridos",
            description = "Crie ligas onde todos os times se enfrentam, acumulando pontos por vitória e empate. Gerencie a tabela de classificação, gols marcados e sofridos, e o saldo de gols para coroar o campeão ao final da temporada."
        )

        // Seção 4: Campeonatos Fase de Grupos
        InfoSection(
            title = "Campeonatos com Fase de Grupos",
            description = "Organize competições com uma fase inicial de grupos, onde os times jogam entre si para avançar para as etapas eliminatórias. Ideal para simular grandes torneios internacionais!"
        )

        Spacer(modifier = Modifier.height(50.dp)) // Espaço extra no final para melhor rolagem
    }
}

@Composable
fun InfoSection(title: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
