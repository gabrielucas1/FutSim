package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsim.data.CampeonatoRepository
import com.example.futsim.model.Campeonato
import com.example.futsim.ui.componentes.ButtonUniversal
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel
import com.example.futsim.model.TipoCampeonato
import androidx.compose.runtime.collectAsState

@Composable
fun TelaCriarCamp(navHostController: NavHostController) {
    val viewModel = LocalFutSimViewModel.current
    val campeonatos by viewModel.campeonatos.collectAsState()

    var nome by remember { mutableStateOf("") }
    var tipoCampeonato by remember { mutableStateOf(TipoCampeonato.NENHUM) }

    // Essa coluna engloba toda a estrutura do projeto
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // onde fica o nome do campeonato
        TextField(
            value = nome,
            onValueChange = { nome = it },
            placeholder = { Text(text = "Insira o nome do campeonato") },
            label = { Text("Nome Campeonato") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 120.dp),
        )

        // Essa coluna engloba os checkboxes e fica centralizada com os itens um abaixo do outro
        Column(
            modifier = Modifier
                .padding(top = 250.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Checkbox Mata-Mata
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Checkbox(
                    checked = tipoCampeonato == TipoCampeonato.MATA_MATA,
                    onCheckedChange = {
                        tipoCampeonato = if (it) TipoCampeonato.MATA_MATA else TipoCampeonato.NENHUM
                    },
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .scale(2f),
                )
                Text(text = "Mata-Mata", fontSize = 40.sp)
            }

            // Checkbox Fase de Grupos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                Checkbox(
                    checked = tipoCampeonato == TipoCampeonato.FASE_GRUPOS,
                    onCheckedChange = {
                        tipoCampeonato = if (it) TipoCampeonato.FASE_GRUPOS else TipoCampeonato.NENHUM
                    },
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .scale(2f),
                )
                Text(text = "Fase de Grupos", fontSize = 40.sp)
            }

            // Checkbox Pontos Corridos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(
                    checked = tipoCampeonato == TipoCampeonato.PONTOS_CORRIDOS,
                    onCheckedChange = {
                        tipoCampeonato = if (it) TipoCampeonato.PONTOS_CORRIDOS else TipoCampeonato.NENHUM
                    },
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .scale(2f),
                )
                Text(text = "Pontos-Corridos", fontSize = 40.sp)
            }
        }

        Column {
            when (tipoCampeonato) {
                TipoCampeonato.MATA_MATA -> Text(text = "Mata Mata selecionada")
                TipoCampeonato.FASE_GRUPOS -> Text(text = "Fase de Grupos selecionada")
                TipoCampeonato.PONTOS_CORRIDOS -> Text(text = "Pontos corridos selecionado")
                TipoCampeonato.NENHUM -> Text("")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        ButtonUniversal(
            text = "CRIAR",
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 50.dp)
                .width(135.dp)
                .height(60.dp),
            textColor = Color.White,
            onClick = {
                if (nome.isNotBlank() && tipoCampeonato != TipoCampeonato.NENHUM) {
                    val campeonato = Campeonato(nome = nome, tipo = tipoCampeonato)
                    viewModel.inserirCampeonato(campeonato)
                    navHostController.navigate("tela_CampCriados")
                }
            }
        )
    }
}
