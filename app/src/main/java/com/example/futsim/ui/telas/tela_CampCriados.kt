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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel
import com.example.futsim.model.Campeonato
import com.example.futsim.model.TipoCampeonato

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
                        viewModel.carregarCampeonatos()
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
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(campeonatos) { campeonato ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { 
                        //navHostController.navigate("tela_campeonato/${campeonato.id}")
                    }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = campeonato.nome,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = when (campeonato.tipo) {
                                TipoCampeonato.MATA_MATA -> "Mata-Mata"
                                TipoCampeonato.FASE_GRUPOS -> "Fase de Grupos"
                                TipoCampeonato.PONTOS_CORRIDOS -> "Pontos Corridos"
                                TipoCampeonato.NENHUM -> "Tipo não definido"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row {
                        IconButton(onClick = {
                            campeonatoSelecionado = campeonato
                            nomeEditado = campeonato.nome
                            showDialog = true
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { 
                            viewModel.deletarCampeonato(campeonato)
                            viewModel.carregarCampeonatos()
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Excluir")
                        }
                    }
                }
            }
        }
    }
}