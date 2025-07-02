package com.example.futsim.ui.telas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.futsim.model.Campeonato
import com.example.futsim.model.TipoCampeonato
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel

@Composable
fun TelaCampCriados(navHostController: NavHostController) {
    val viewModel = LocalFutSimViewModel.current
    val campeonatos by viewModel.campeonatos.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var nomeEditado by remember { mutableStateOf("") }
    var campeonatoSelecionado by remember { mutableStateOf<Campeonato?>(null) }

    LaunchedEffect(Unit) { viewModel.carregarCampeonatos() }

    if (showDialog && campeonatoSelecionado != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Editar nome do campeonato") },
            text = {
                OutlinedTextField(
                    value = nomeEditado,
                    onValueChange = { nomeEditado = it },
                    label = { Text("Nome do campeonato") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    campeonatoSelecionado?.let {
                        val atualizado = it.copy(nome = nomeEditado)
                        viewModel.atualizarCampeonato(atualizado)
                    }
                    showDialog = false
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(campeonatos) { campeonato ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        navHostController.navigate("tela_PontosCorridos/${campeonato.id}")
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = campeonato.nome,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (campeonato.tipo) {
                                TipoCampeonato.MATA_MATA -> "Mata-Mata"
                                TipoCampeonato.FASE_GRUPOS -> "Fase de Grupos"
                                TipoCampeonato.PONTOS_CORRIDOS -> "Pontos Corridos"
                                TipoCampeonato.NENHUM -> "Tipo não definido"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Row {
                        IconButton(onClick = {
                            campeonatoSelecionado = campeonato
                            nomeEditado = campeonato.nome
                            showDialog = true
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.secondary)
                        }
                        IconButton(onClick = {
                            viewModel.deletarCampeonato(campeonato)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}