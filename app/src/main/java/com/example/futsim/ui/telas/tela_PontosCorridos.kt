package com.example.futsim.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle // Import corrigido
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.futsim.model.Time
import com.example.futsim.model.TimeTabela
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel
import kotlinx.coroutines.launch

// ... (o resto do arquivo continua igual até o Scaffold)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPontosCorridos(navHostController: NavHostController, campeonatoId: Int) {
    val viewModel = LocalFutSimViewModel.current
    val times by viewModel.times.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingTime by remember { mutableStateOf<Time?>(null) }

    var nome by remember { mutableStateOf("") }
    var vitorias by remember { mutableStateOf("") }
    var empates by remember { mutableStateOf("") }
    var derrotas by remember { mutableStateOf("") }
    var golsPro by remember { mutableStateOf("") }
    var golsContra by remember { mutableStateOf("") }

    LaunchedEffect(campeonatoId) {
        viewModel.carregarTimesPorCampeonato(campeonatoId)
    }

    fun limparCampos() {
        nome = ""
        vitorias = ""
        empates = ""
        derrotas = ""
        golsPro = ""
        golsContra = ""
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Adicionar Time") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, singleLine = true)
                    OutlinedTextField(value = vitorias, onValueChange = { vitorias = it }, label = { Text("Vitórias") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = empates, onValueChange = { empates = it }, label = { Text("Empates") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = derrotas, onValueChange = { derrotas = it }, label = { Text("Derrotas") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = golsPro, onValueChange = { golsPro = it }, label = { Text("Gols Pró") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = golsContra, onValueChange = { golsContra = it }, label = { Text("Gols Contra") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    val camposValidos = nome.isNotBlank() && vitorias.toIntOrNull() != null && empates.toIntOrNull() != null && derrotas.toIntOrNull() != null && golsPro.toIntOrNull() != null && golsContra.toIntOrNull() != null
                    if (camposValidos) {
                        val time = Time(
                            nome = nome,
                            campeonatoId = campeonatoId,
                            vitorias = vitorias.toInt(),
                            empates = empates.toInt(),
                            derrotas = derrotas.toInt(),
                            golsPro = golsPro.toInt(),
                            golsContra = golsContra.toInt()
                        )
                        viewModel.inserirTime(time)
                        limparCampos()
                        showAddDialog = false
                        coroutineScope.launch { snackbarHostState.showSnackbar("Time adicionado!") }
                    } else {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Preencha todos os campos corretamente.") }
                    }
                }) { Text("Salvar") }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    limparCampos()
                    showAddDialog = false
                }) { Text("Cancelar") }
            }
        )
    }

    if (showEditDialog && editingTime != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Time") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, singleLine = true)
                    OutlinedTextField(value = vitorias, onValueChange = { vitorias = it }, label = { Text("Vitórias") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = empates, onValueChange = { empates = it }, label = { Text("Empates") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = derrotas, onValueChange = { derrotas = it }, label = { Text("Derrotas") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = golsPro, onValueChange = { golsPro = it }, label = { Text("Gols Pró") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(value = golsContra, onValueChange = { golsContra = it }, label = { Text("Gols Contra") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    val camposValidos = nome.isNotBlank() && vitorias.toIntOrNull() != null && empates.toIntOrNull() != null && derrotas.toIntOrNull() != null && golsPro.toIntOrNull() != null && golsContra.toIntOrNull() != null
                    if (camposValidos && editingTime != null) {
                        val timeAtualizado = editingTime!!.copy(
                            nome = nome,
                            vitorias = vitorias.toInt(),
                            empates = empates.toInt(),
                            derrotas = derrotas.toInt(),
                            golsPro = golsPro.toInt(),
                            golsContra = golsContra.toInt()
                        )
                        viewModel.atualizarTime(timeAtualizado)
                        limparCampos()
                        showEditDialog = false
                        coroutineScope.launch { snackbarHostState.showSnackbar("Time atualizado!") }
                    } else {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Preencha todos os campos corretamente.") }
                    }
                }) { Text("Salvar") }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    limparCampos()
                    showEditDialog = false
                }) { Text("Cancelar") }
            },
            icon = {
                IconButton(onClick = {
                    editingTime?.let { viewModel.deletarTime(it) }
                    limparCampos()
                    showEditDialog = false
                    coroutineScope.launch { snackbarHostState.showSnackbar("Time excluído!") }
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Time")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                "Tabela de Pontos Corridos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
            )
            Divider()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { HeaderTabelaPontosCorridos() }
                val timesOrdenados = times
                    .map {
                        TimeTabela(
                            posicao = 0,
                            nome = it.nome,
                            pontos = it.vitorias * 3 + it.empates,
                            jogos = it.vitorias + it.empates + it.derrotas,
                            vitorias = it.vitorias,
                            empates = it.empates,
                            derrotas = it.derrotas,
                            golsPro = it.golsPro,
                            golsContra = it.golsContra
                        )
                    }
                    .sortedWith(
                        compareByDescending<TimeTabela> { it.pontos }
                            .thenByDescending { it.saldoGols }
                            .thenByDescending { it.golsPro }
                    )
                    .mapIndexed { index, time -> time.copy(posicao = index + 1) }

                items(timesOrdenados) { timeTabela: TimeTabela ->
                    val time = times.find { it.nome == timeTabela.nome }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp, horizontal = 4.dp)
                            .background(
                                if (editingTime?.id == time?.id) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                else Color.Transparent
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinhaTabelaPontosCorridos(timeTabela, Modifier.weight(1f))
                        if (time != null) {
                            IconButton(
                                onClick = {
                                    nome = time.nome
                                    vitorias = time.vitorias.toString()
                                    empates = time.empates.toString()
                                    derrotas = time.derrotas.toString()
                                    golsPro = time.golsPro.toString()
                                    golsContra = time.golsContra.toString()
                                    editingTime = time
                                    showEditDialog = true
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(start = 4.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Editar/Excluir",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    Divider()
                }
            }
        }
    }
}


@Composable
fun HeaderTabelaPontosCorridos() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val headerStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text("#", modifier = Modifier.weight(0.5f), style = headerStyle)
        Text("Time", modifier = Modifier.weight(2f), style = headerStyle)
        Text("Pts", modifier = Modifier.weight(1f), style = headerStyle)
        Text("PJ", modifier = Modifier.weight(1f), style = headerStyle)
        Text("V", modifier = Modifier.weight(1f), style = headerStyle)
        Text("E", modifier = Modifier.weight(1f), style = headerStyle)
        Text("D", modifier = Modifier.weight(1f), style = headerStyle)
        Text("GP", modifier = Modifier.weight(1f), style = headerStyle)
        Text("GC", modifier = Modifier.weight(1f), style = headerStyle)
        Text("SG", modifier = Modifier.weight(1f), style = headerStyle)
    }
}

@Composable
fun LinhaTabelaPontosCorridos(time: TimeTabela, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min), // Garante que a linha tenha uma altura consistente
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val cellModifier = Modifier.align(Alignment.CenterVertically)
        Text("${time.posicao}", modifier = cellModifier.weight(0.5f), fontSize = 14.sp)
        Box(
            modifier = cellModifier
                .weight(2f)
                .horizontalScroll(rememberScrollState())
        ) {
            Text(
                text = time.nome,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
        }
        Text("${time.pontos}", modifier = cellModifier.weight(1f), fontSize = 14.sp)
        Text("${time.jogos}", modifier = cellModifier.weight(1f), fontSize = 14.sp)
        Text("${time.vitorias}", modifier = cellModifier.weight(1f), fontSize = 14.sp)
        Text("${time.empates}", modifier = cellModifier.weight(1f), fontSize = 14.sp)
        Text("${time.derrotas}", modifier = cellModifier.weight(1f), fontSize = 14.sp)
        Text("${time.golsPro}", modifier = cellModifier.weight(1f), fontSize = 14.sp)
        Text("${time.golsContra}", modifier = cellModifier.weight(1f), fontSize = 14.sp)
        Text("${time.saldoGols}", modifier = cellModifier.weight(1f), fontSize = 14.sp)
    }
}