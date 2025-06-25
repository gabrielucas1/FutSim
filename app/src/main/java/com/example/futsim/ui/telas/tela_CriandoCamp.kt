package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.futsim.model.Campeonato
import com.example.futsim.model.TipoCampeonato
import com.example.futsim.ui.componentes.ButtonUniversal
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCriarCamp(navHostController: NavHostController) {
    val viewModel = LocalFutSimViewModel.current

    var nome by remember { mutableStateOf("") }
    var tipoCampeonato by remember { mutableStateOf(TipoCampeonato.NENHUM) }
    var nomeError by remember { mutableStateOf(false) }
    var tipoError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Criar Campeonato", fontWeight = FontWeight.Bold, fontSize = 22.sp) }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    nomeError = nome.isBlank()
                    tipoError = tipoCampeonato == TipoCampeonato.NENHUM
                    if (!nomeError && !tipoError) {
                        val campeonato = Campeonato(nome = nome, tipo = tipoCampeonato)
                        viewModel.inserirCampeonato(campeonato)
                        navHostController.navigate("tela_CampCriados")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Criar Campeonato", fontSize = 18.sp, color = Color.White)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            OutlinedTextField(
                value = nome,
                onValueChange = {
                    nome = it
                    nomeError = false
                },
                label = { Text("Nome do Campeonato") },
                isError = nomeError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (nomeError) {
                Text(
                    text = "Informe o nome do campeonato",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(Modifier.height(32.dp))
            Text(
                text = "Tipo de Campeonato",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            val tipos = listOf(
                TipoCampeonato.MATA_MATA to "Mata-Mata",
                TipoCampeonato.FASE_GRUPOS to "Fase de Grupos",
                TipoCampeonato.PONTOS_CORRIDOS to "Pontos Corridos"
            )

            tipos.forEach { (tipo, label) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(40.dp)
                ) {
                    RadioButton(
                        selected = tipoCampeonato == tipo,
                        onClick = {
                            tipoCampeonato = tipo
                            tipoError = false
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = label,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            if (tipoError) {
                Text(
                    text = "Selecione um tipo de campeonato",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}