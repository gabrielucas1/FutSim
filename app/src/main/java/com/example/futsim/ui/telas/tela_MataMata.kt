package com.example.futsim.ui.telas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.*

// --- DATA CLASSES E ENUMS (MARCADOS COMO @Serializable) ---
// Estas classes agora são usadas pelo FutSimViewModel e pela tela
@Serializable
enum class FaseTorneio(val label: String, val confrontos: Int) {
    OITAVAS("Oitavas de Final", 8),
    QUARTAS("Quartas de Final", 4),
    SEMIFINAIS("Semifinais", 2),
    FINAL("Final", 1);

    companion object {
        fun getNext(current: FaseTorneio): FaseTorneio? = when (current) {
            OITAVAS -> QUARTAS
            QUARTAS -> SEMIFINAIS
            SEMIFINAIS -> FINAL
            FINAL -> null
        }
    }
}

@Serializable
data class TimeMataMata(
    val id: String = UUID.randomUUID().toString(),
    var nome: String,
    var status: Status = Status.NEUTRO
) {
    @Serializable
    enum class Status { NEUTRO, VENCEDOR, ELIMINADO, CAMPEAO }
}

@Serializable
data class Confronto(
    val timeA: TimeMataMata,
    val timeB: TimeMataMata,
    var golsA: String = "",
    var golsB: String = "",
    val fase: FaseTorneio
) {
    val vencedor: TimeMataMata?
        get() {
            val ga = golsA.toIntOrNull() ?: -1
            val gb = golsB.toIntOrNull() ?: -1
            return when {
                ga > gb -> timeA
                gb > ga -> timeB
                else -> null
            }
        }
}

@Serializable
data class MataMataUiState(
    val faseAtualConfig: FaseTorneio = FaseTorneio.OITAVAS,
    val timesIniciais: List<TimeMataMata> = List(FaseTorneio.OITAVAS.confrontos * 2) { TimeMataMata(nome = "") },
    val confrontosPorFase: Map<FaseTorneio, List<Confronto>> = emptyMap(),
    val campeao: TimeMataMata? = null
)

// --- COMPOSABLE PRINCIPAL ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TelaMataMata(navController: NavHostController, campeonatoId: Int) {
    // Usa o ViewModel central compartilhado
    val viewModel = LocalFutSimViewModel.current
    val uiState by viewModel.mataMataUiState.collectAsState()

    // Carrega o estado do campeonato específico ao entrar na tela
    LaunchedEffect(campeonatoId) {
        viewModel.carregarEstadoMataMata(campeonatoId)
    }

    // Funções de ajuda para modificar o estado no ViewModel
    val setState = { newState: MataMataUiState -> viewModel.updateMataMataState(newState) }

    val faseAtualConfig = uiState.faseAtualConfig
    val timesIniciais = uiState.timesIniciais
    val confrontosPorFase = uiState.confrontosPorFase
    val campeao = uiState.campeao

    var showResetDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val todasAsFases = remember(confrontosPorFase) {
        confrontosPorFase.keys.sortedBy { it.ordinal }
    }
    val pagerState = rememberPagerState(pageCount = { todasAsFases.size })

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mata-Mata", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                actions = {
                    IconButton(onClick = { showResetDialog = true }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Resetar Tabela")
                    }
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Limpar Tudo")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (confrontosPorFase.isEmpty()) {
                ConfiguracaoInicialTorneio(
                    faseAtualConfig = faseAtualConfig,
                    timesIniciais = timesIniciais,
                    onSetFaseInicial = { fase ->
                        setState(uiState.copy(
                            faseAtualConfig = fase,
                            timesIniciais = List(fase.confrontos * 2) { TimeMataMata(nome = "") },
                            confrontosPorFase = emptyMap(),
                            campeao = null
                        ))
                    },
                    onUpdateTime = { idx, nome ->
                        val novosTimes = timesIniciais.toMutableList().also {
                            it[idx] = it[idx].copy(nome = nome)
                        }
                        setState(uiState.copy(timesIniciais = novosTimes))
                    },
                    onGerarChaves = {
                        if (timesIniciais.all { it.nome.isNotBlank() }) {
                            val timesEmbaralhados = timesIniciais.shuffled()
                            val confrontos = timesEmbaralhados.chunked(2).map {
                                Confronto(it[0], it[1], fase = faseAtualConfig)
                            }
                            setState(uiState.copy(confrontosPorFase = mapOf(faseAtualConfig to confrontos)))
                        }
                    }
                )
            } else {
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 0.dp
                ) {
                    todasAsFases.forEachIndexed { index, fase ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(fase.label) }
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val fase = todasAsFases[page]
                    val confrontosDaFase = confrontosPorFase[fase]

                    if (!confrontosDaFase.isNullOrEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(confrontosDaFase, key = { it.timeA.id }) { confronto ->
                                MataMataConfrontoCard(
                                    confronto = confronto,
                                    onGolsChange = { golsA, golsB ->
                                        val novosConfrontosPorFase = confrontosPorFase.toMutableMap()
                                        val faseConfrontos = novosConfrontosPorFase[fase]?.toMutableList() ?: return@MataMataConfrontoCard
                                        val index = faseConfrontos.indexOfFirst { it.timeA.id == confronto.timeA.id }

                                        if (index != -1) {
                                            var novoConfronto = faseConfrontos[index].copy(golsA = golsA, golsB = golsB)
                                            val ga = novoConfronto.golsA.toIntOrNull() ?: -1
                                            val gb = novoConfronto.golsB.toIntOrNull() ?: -1

                                            if (ga >= 0 && gb >= 0) {
                                                val statusA = if (ga > gb) TimeMataMata.Status.VENCEDOR else TimeMataMata.Status.ELIMINADO
                                                val statusB = if (gb > ga) TimeMataMata.Status.VENCEDOR else TimeMataMata.Status.ELIMINADO
                                                novoConfronto = novoConfronto.copy(
                                                    timeA = novoConfronto.timeA.copy(status = statusA),
                                                    timeB = novoConfronto.timeB.copy(status = statusB)
                                                )
                                            }
                                            faseConfrontos[index] = novoConfronto
                                            novosConfrontosPorFase[fase] = faseConfrontos
                                            setState(uiState.copy(confrontosPorFase = novosConfrontosPorFase))
                                        }
                                    }
                                )
                            }

                            val todosResultadosPreenchidos = confrontosDaFase.all { it.vencedor != null }
                            if (fase != FaseTorneio.FINAL && todosResultadosPreenchidos) {
                                item {
                                    Spacer(Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            val vencedores = confrontosDaFase.mapNotNull { it.vencedor }
                                            val proximaFase = FaseTorneio.getNext(fase)
                                            if (proximaFase != null && vencedores.size == confrontosDaFase.size) {
                                                val novosConfrontos = vencedores.chunked(2).map {
                                                    Confronto(it[0].copy(status = TimeMataMata.Status.NEUTRO), it[1].copy(status = TimeMataMata.Status.NEUTRO), fase = proximaFase)
                                                }
                                                val novosConfrontosPorFase = confrontosPorFase.toMutableMap()
                                                novosConfrontosPorFase[proximaFase] = novosConfrontos
                                                setState(uiState.copy(confrontosPorFase = novosConfrontosPorFase))

                                                val nextIndex = todasAsFases.indexOf(fase) + 1
                                                if(nextIndex < pagerState.pageCount) {
                                                    coroutineScope.launch { pagerState.animateScrollToPage(nextIndex) }
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                                    ) {
                                        Text("Avançar para ${FaseTorneio.getNext(fase)?.label ?: ""}")
                                    }
                                }
                            }

                            if (fase == FaseTorneio.FINAL && campeao == null) {
                                val confrontoFinal = confrontosDaFase.first()
                                if (confrontoFinal.vencedor != null) {
                                    setState(uiState.copy(campeao = confrontoFinal.vencedor?.copy(status = TimeMataMata.Status.CAMPEAO)))
                                }
                            }

                            if (campeao != null) {
                                item {
                                    Spacer(Modifier.height(24.dp))
                                    CampeaoCard(campeao)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Resetar Torneio?") },
            text = { Text("Isso irá apagar os confrontos e resultados, voltando para a tela de configuração inicial dos times.") },
            confirmButton = {
                Button(onClick = {
                    val faseInicial = uiState.faseAtualConfig
                    val timesParaManter = uiState.confrontosPorFase[faseInicial]?.flatMap { listOf(it.timeA, it.timeB) }
                        ?.map { it.copy(status = TimeMataMata.Status.NEUTRO) } ?: uiState.timesIniciais
                    setState(MataMataUiState(faseAtualConfig = faseInicial, timesIniciais = timesParaManter))
                    showResetDialog = false
                }) { Text("Resetar") }
            },
            dismissButton = { OutlinedButton(onClick = { showResetDialog = false }) { Text("Cancelar") } }
        )
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Limpar Tudo?") },
            text = { Text("Isso irá apagar todos os dados deste torneio. Esta ação não pode ser desfeita.") },
            confirmButton = {
                Button(onClick = {
                    setState(MataMataUiState())
                    showClearDialog = false
                }) { Text("Limpar Tudo") }
            },
            dismissButton = { OutlinedButton(onClick = { showClearDialog = false }) { Text("Cancelar") } }
        )
    }
}


// --- COMPOSABLES REUTILIZÁVEIS (Nenhuma alteração necessária) ---
// Copie e cole os que você já tinha.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracaoInicialTorneio(
    faseAtualConfig: FaseTorneio,
    timesIniciais: List<TimeMataMata>,
    onSetFaseInicial: (FaseTorneio) -> Unit,
    onUpdateTime: (Int, String) -> Unit,
    onGerarChaves: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Configurar Torneio",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = faseAtualConfig.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fase Inicial") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                FaseTorneio.entries.forEach { fase ->
                    if (fase != FaseTorneio.FINAL) {
                        DropdownMenuItem(
                            text = { Text(fase.label) },
                            onClick = {
                                onSetFaseInicial(fase)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(timesIniciais.size) { idx ->
                OutlinedTextField(
                    value = timesIniciais[idx].nome,
                    onValueChange = { onUpdateTime(idx, it) },
                    label = { Text("Time ${idx + 1}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onGerarChaves,
            enabled = timesIniciais.all { it.nome.isNotBlank() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Mata-Mata")
        }
    }
}

@Composable
fun MataMataConfrontoCard(
    confronto: Confronto,
    onGolsChange: (golsA: String, golsB: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = confronto.fase.label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TimeConfrontoBox(time = confronto.timeA)
                Text(
                    "X",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TimeConfrontoBox(time = confronto.timeB)
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GolsTextField(
                    value = confronto.golsA,
                    onValueChange = { onGolsChange(it, confronto.golsB) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                GolsTextField(
                    value = confronto.golsB,
                    onValueChange = { onGolsChange(confronto.golsA, it) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TimeConfrontoBox(time: TimeMataMata) {
    val backgroundColor = when (time.status) {
        TimeMataMata.Status.CAMPEAO -> Color(0xFFFFD700).copy(alpha = 0.2f)
        TimeMataMata.Status.VENCEDOR -> Color(0xFF4CAF50).copy(alpha = 0.2f)
        TimeMataMata.Status.ELIMINADO -> Color(0xFFD32F2F).copy(alpha = 0.1f)
        TimeMataMata.Status.NEUTRO -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when (time.status) {
        TimeMataMata.Status.CAMPEAO -> Color(0xFFDAA520)
        TimeMataMata.Status.VENCEDOR -> Color(0xFF2E7D32)
        TimeMataMata.Status.ELIMINADO -> Color(0xFFB71C1C)
        TimeMataMata.Status.NEUTRO -> MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = Modifier
            .widthIn(min = 100.dp, max = 150.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = time.nome,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = contentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedVisibility(
            visible = time.status == TimeMataMata.Status.VENCEDOR || time.status == TimeMataMata.Status.CAMPEAO,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(),
            exit = fadeOut(animationSpec = tween(500)) + slideOutVertically()
        ) {
            Icon(
                imageVector = if (time.status == TimeMataMata.Status.CAMPEAO) Icons.Default.EmojiEvents else Icons.Default.Star,
                contentDescription = if (time.status == TimeMataMata.Status.CAMPEAO) "Campeão" else "Vencedor",
                tint = contentColor,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun GolsTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                onValueChange(it)
            }
        },
        label = { Text("Gols") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun CampeaoCard(campeao: TimeMataMata) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD700).copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Troféu de Campeão",
                tint = Color(0xFFDAA520),
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "O GRANDE CAMPEÃO É:",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDAA520)
            )
            Text(
                campeao.nome.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFDAA520),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}