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
import androidx.compose.ui.res.stringResource // Importação adicionada
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
                contentDescription = stringResource(R.string.app_name), // Usando stringResource
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

        InfoSection(
            title = stringResource(R.string.info_section_title_1), // String substituída
            description = stringResource(R.string.info_section_desc_1) // String substituída
        )
        // Imagem adicionada para a apresentção
        Image(
            painter = painterResource(id = R.drawable.chuteirabolatrofeu),
            contentDescription = stringResource(R.string.content_desc_apresentacao), // String substituída
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 16.dp),
            alignment = Alignment.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Seção 2: Campeonatos Mata-Mata
        InfoSection(
            title = stringResource(R.string.info_section_title_mata_mata), // String substituída
            description = stringResource(R.string.info_section_desc_mata_mata) // String substituída
        )
        // Imagem adicionada para a seção de mata mata
        Image(
            painter = painterResource(id = R.drawable.imgmatamata),
            contentDescription = stringResource(R.string.content_desc_mata_mata), // String substituída
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 16.dp),
            alignment = Alignment.Center
        )

        // Seção 3: Campeonatos Pontos Corridos
        InfoSection(
            title = stringResource(R.string.info_section_title_pontos_corridos), // String substituída
            description = stringResource(R.string.info_section_desc_pontos_corridos) // String substituída
        )
        // Imagem adicionada para a seção de Pontos Corridos
        Image(
            painter = painterResource(id = R.drawable.pontoscorridos),
            contentDescription = stringResource(R.string.content_desc_pontos_corridos), // String substituída
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 16.dp),
            alignment = Alignment.Center
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Rodapé (Footer)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.footer_text), // String substituída
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
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
