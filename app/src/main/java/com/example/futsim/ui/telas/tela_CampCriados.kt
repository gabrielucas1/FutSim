package com.example.futsim.ui.telas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.futsim.LocalFutSimViewModel
import com.example.futsim.model.Campeonato

@Composable
fun TelaCampCriados(navHostController: NavHostController) {
    val viewModel = LocalFutSimViewModel.current
    val campeonatos by viewModel.campeonatos.collectAsState()

    LaunchedEffect(Unit) { viewModel.carregarCampeonatos() }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(campeonatos) { campeonato ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { 
                        navHostController.navigate("tela_campeonato/${campeonato.id}")
                    }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = campeonato.nome, style = MaterialTheme.typography.titleMedium)
                    Row {
                        IconButton(onClick = { /* abre a caixa de edit */ }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { viewModel.deletarCampeonato(campeonato) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir")
                        }
                    }
                }
            }
        }
    }
}