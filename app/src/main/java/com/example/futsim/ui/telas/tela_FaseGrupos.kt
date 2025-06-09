package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.futsim.model.TimeTabela
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.example.futsim.ui.viewmodel.FutSimViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HeaderTabelaFaseGrupos() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("#", modifier = Modifier.weight(0.5f))
        Text("Time", modifier = Modifier.weight(2f))
        Text("Pts", modifier = Modifier.weight(1f))
        Text("PJ", modifier = Modifier.weight(1f))
        Text("V", modifier = Modifier.weight(1f))
        Text("E", modifier = Modifier.weight(1f))
        Text("D", modifier = Modifier.weight(1f))
        Text("GP", modifier = Modifier.weight(1f))
        Text("GC", modifier = Modifier.weight(1f))
        Text("SG", modifier = Modifier.weight(1f))
    }
}

@Composable
fun TelaFaseDeGrupos(
    navController: NavHostController,
    viewModel: FutSimViewModel = viewModel()
) {
    val times = remember {
        mutableStateListOf(
            TimeTabela(1, "Palmeiras", 38, 19, 12, 2, 5, 30, 15),
            TimeTabela(2, "Flamengo", 36, 19, 11, 3, 5, 28, 18),
            TimeTabela(3, "Atlético-MG", 33, 19, 10, 3, 6, 25, 20),
            TimeTabela(4, "São Paulo", 30, 19, 9, 3, 7, 22, 21)
        )
    }

    var nome by remember { mutableStateOf("") }
    var vitorias by remember { mutableStateOf("") }
    var empates by remember { mutableStateOf("") }
    var derrotas by remember { mutableStateOf("") }
    var golsPro by remember { mutableStateOf("") }
    var golsContra by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Adicionar Novo Time", style = MaterialTheme.typography.titleLarge)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome do Time") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(value = vitorias, onValueChange = { vitorias = it }, label = { Text("Vitórias") },modifier = Modifier.weight(1f))
                    OutlinedTextField(value = empates, onValueChange = { empates = it }, label = { Text("Empates") },modifier = Modifier.weight(1f))
                    OutlinedTextField(value = derrotas, onValueChange = { derrotas = it }, label = { Text("Derrotas") },modifier = Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(value = golsPro, onValueChange = { golsPro = it }, label = { Text("Gols Pró") },modifier = Modifier.weight(1f))
                    OutlinedTextField(value = golsContra, onValueChange = { golsContra = it }, label = { Text("Gols Contra") },modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                val coroutineScope = rememberCoroutineScope()

                Button(onClick = {
                    val camposValidos = nome.isNotBlank()
                            && vitorias.toIntOrNull() != null
                            && empates.toIntOrNull() != null
                            && derrotas.toIntOrNull() != null
                            && golsPro.toIntOrNull() != null
                            && golsContra.toIntOrNull() != null

                    if (camposValidos) {
                        val novoTime = TimeTabela(
                            posicao = times.size + 1,
                            nome = nome,
                            pontos = (vitorias.toIntOrNull() ?: 0) * 3 + (empates.toIntOrNull() ?: 0),
                            jogos = (vitorias.toIntOrNull() ?: 0) + (empates.toIntOrNull() ?: 0) + (derrotas.toIntOrNull() ?: 0),
                            vitorias = vitorias.toIntOrNull() ?: 0,
                            empates = empates.toIntOrNull() ?: 0,
                            derrotas = derrotas.toIntOrNull() ?: 0,
                            golsPro = golsPro.toIntOrNull() ?: 0,
                            golsContra = golsContra.toIntOrNull() ?: 0
                        )
                        times.add(novoTime)

                        nome = ""
                        vitorias = ""
                        empates = ""
                        derrotas = ""
                        golsPro = ""
                        golsContra = ""

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Time adicionado com sucesso!")
                        }
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Preencha todos os campos corretamente.")
                        }
                    }
                })
                {
                    Text("Salvar Time")
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                HeaderTabelaFaseGrupos()
            }

            val timesOrdenados = times.sortedWith(
                compareByDescending<TimeTabela> { it.pontos }
                    .thenByDescending { it.saldoGols }
                    .thenByDescending { it.golsPro }
            ).mapIndexed { index, time -> time.copy(posicao = index + 1) }

            items(timesOrdenados) { time ->
                LinhaTabela(time)
                Divider()
            }
        }
    }
}

@Composable
fun LinhaTabela(time: TimeTabela) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${time.posicao}", modifier = Modifier.weight(0.5f))
        Text(time.nome, modifier = Modifier.weight(2f))
        Text("${time.pontos}", modifier = Modifier.weight(1f))
        Text("${time.jogos}", modifier = Modifier.weight(1f))
        Text("${time.vitorias}", modifier = Modifier.weight(1f))
        Text("${time.empates}", modifier = Modifier.weight(1f))
        Text("${time.derrotas}", modifier = Modifier.weight(1f))
        Text("${time.golsPro}", modifier = Modifier.weight(1f))
        Text("${time.golsContra}", modifier = Modifier.weight(1f))
        Text("${time.saldoGols}", modifier = Modifier.weight(1f))
    }
}