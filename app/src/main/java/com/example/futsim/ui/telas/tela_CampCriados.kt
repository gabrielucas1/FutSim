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
import androidx.compose.ui.res.stringResource // Importação adicionada
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.futsim.R // Importação adicionada para acessar R.string
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
            title = { Text(stringResource(R.string.editar_nome_campeonato)) },
            text = {
                OutlinedTextField(
                    value = nomeEditado,
                    onValueChange = { nomeEditado = it },
                    label = { Text(stringResource(R.string.nome_campeonato)) }
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
                    Text(stringResource(R.string.salvar))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancelar))
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
                        when (campeonato.tipo) {
                            // Navegação atualizada para passar o ID do campeonato
                            TipoCampeonato.MATA_MATA -> navHostController.navigate("tela_MataMata/${campeonato.id}")
                            TipoCampeonato.PONTOS_CORRIDOS -> navHostController.navigate("tela_PontosCorridos/${campeonato.id}")
                            TipoCampeonato.FASE_GRUPOS -> navHostController.navigate("tela_FaseGrupos")
                            else -> {}
                        }
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
                                TipoCampeonato.MATA_MATA -> stringResource(R.string.tipo_mata_mata)
                                TipoCampeonato.FASE_GRUPOS -> stringResource(R.string.tipo_fase_grupos)
                                TipoCampeonato.PONTOS_CORRIDOS -> stringResource(R.string.tipo_pontos_corridos)
                                TipoCampeonato.NENHUM -> stringResource(R.string.tipo_nao_definido)
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
                            Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.editar), tint = MaterialTheme.colorScheme.secondary)
                        }
                        IconButton(onClick = {
                            viewModel.deletarCampeonato(campeonato)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.excluir), tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
