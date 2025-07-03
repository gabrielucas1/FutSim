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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

// --- Enums e Data Classes ---
enum class FaseTorneio(val label: String, val confrontos: Int) {
    OITAVAS("Oitavas de Final", 8),
    QUARTAS("Quartas de Final", 4),
    SEMIFINAIS("Semifinais", 2),
    FINAL("Final", 1);

    companion object {
        fun getNext(current: FaseTorneio): FaseTorneio? {
            return when (current) {
                OITAVAS -> QUARTAS
                QUARTAS -> SEMIFINAIS
                SEMIFINAIS -> FINAL
                FINAL -> null
            }
        }
    }
}

data class TimeMataMata(
    val id: String = UUID.randomUUID().toString(), // Adicionado ID para facilitar identificação
    var nome: String,
    var status: Status = Status.NEUTRO
) {
    enum class Status { NEUTRO, VENCEDOR, ELIMINADO, CAMPEAO }
}

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
                else -> null // Empate ou placar inválido. Ajustar lógica se empates forem permitidos ou tiverem regra específica (ex: pênaltis)
            }
        }
}

// --- ViewModel ---
class MataMataViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _faseAtualConfig = MutableStateFlow(savedStateHandle["faseAtualConfig"] ?: FaseTorneio.OITAVAS)
    val faseAtualConfig: StateFlow<FaseTorneio> = _faseAtualConfig

    private val _timesIniciais = MutableStateFlow(savedStateHandle["timesIniciais"] ?: List(_faseAtualConfig.value.confrontos * 2) { TimeMataMata(nome = "") }) // Adicionado 'nome = ""'
    val timesIniciais: StateFlow<List<TimeMataMata>> = _timesIniciais

    private val _confrontosPorFase = MutableStateFlow(savedStateHandle["confrontosPorFase"] ?: mutableMapOf<FaseTorneio, List<Confronto>>())
    val confrontosPorFase: StateFlow<Map<FaseTorneio, List<Confronto>>> = _confrontosPorFase

    private val _campeao = MutableStateFlow(savedStateHandle["campeao"] as TimeMataMata?)
    val campeao: StateFlow<TimeMataMata?> = _campeao

    init {
        // Se já houver times iniciais salvos e nenhuma fase gerada, tenta gerar as chaves
        if (_timesIniciais.value.any { it.nome.isNotBlank() } && _confrontosPorFase.value.isEmpty()) {
            gerarChavesIniciais()
        }
    }

    fun setFaseInicialConfig(fase: FaseTorneio) {
        _faseAtualConfig.value = fase
        _timesIniciais.value = List(fase.confrontos * 2) { TimeMataMata(nome = "") } // Adicionado 'nome = ""'
        _confrontosPorFase.value = mutableMapOf()
        _campeao.value = null
        saveState()
    }

    fun updateTimeInicial(idx: Int, nome: String) {
        _timesIniciais.value = _timesIniciais.value.toMutableList().also {
            it[idx] = it[idx].copy(nome = nome)
        }
        saveState()
    }

    fun gerarChavesIniciais() {
        if (_timesIniciais.value.any { it.nome.isBlank() }) return // Só gera se todos os times tiverem nome

        val timesEmbaralhados = _timesIniciais.value.shuffled() // Opcional: Embaralhar para chaves mais aleatórias
        val confrontos = timesEmbaralhados.chunked(2).map {
            Confronto(it[0], it[1], fase = _faseAtualConfig.value)
        }
        _confrontosPorFase.update {
            it.toMutableMap().apply {
                this[_faseAtualConfig.value] = confrontos
            }
        }
        saveState()
    }

    fun preencherResultadosConfronto(fase: FaseTorneio, confrontoTimeAId: String, golsA: String, golsB: String) {
        _confrontosPorFase.update { currentMap ->
            val faseConfrontos = currentMap[fase]?.toMutableList() ?: return@update currentMap
            val index = faseConfrontos.indexOfFirst { it.timeA.id == confrontoTimeAId } // Usar o ID do timeA para identificar o confronto
            if (index != -1) {
                val confrontoAtual = faseConfrontos[index]
                var novoConfronto = confrontoAtual.copy(golsA = golsA, golsB = golsB)

                // Atualiza status dos times dentro do confronto
                val ga = novoConfronto.golsA.toIntOrNull() ?: -1
                val gb = novoConfronto.golsB.toIntOrNull() ?: -1

                if (ga >= 0 && gb >= 0) {
                    val updatedTimeA = novoConfronto.timeA.copy(status = TimeMataMata.Status.NEUTRO)
                    val updatedTimeB = novoConfronto.timeB.copy(status = TimeMataMata.Status.NEUTRO)

                    novoConfronto = if (ga > gb) {
                        novoConfronto.copy(
                            timeA = updatedTimeA.copy(status = if (fase == FaseTorneio.FINAL) TimeMataMata.Status.CAMPEAO else TimeMataMata.Status.VENCEDOR),
                            timeB = updatedTimeB.copy(status = TimeMataMata.Status.ELIMINADO)
                        )
                    } else if (gb > ga) {
                        novoConfronto.copy(
                            timeA = updatedTimeA.copy(status = TimeMataMata.Status.ELIMINADO),
                            timeB = updatedTimeB.copy(status = if (fase == FaseTorneio.FINAL) TimeMataMata.Status.CAMPEAO else TimeMataMata.Status.VENCEDOR)
                        )
                    } else { // Empate, ambos neutros ou lógica para pênaltis, etc.
                        novoConfronto.copy(timeA = updatedTimeA, timeB = updatedTimeB)
                    }
                }
                faseConfrontos[index] = novoConfronto

                currentMap.toMutableMap().apply {
                    this[fase] = faseConfrontos
                }
            } else {
                currentMap
            }
        }
        saveState()
    }

    fun avancarFase(faseAtual: FaseTorneio) {
        val confrontosAtuais = _confrontosPorFase.value[faseAtual] ?: return
        val vencedores = confrontosAtuais.mapNotNull { it.vencedor }

        if (vencedores.size * 2 != confrontosAtuais.size * 2) {
            // Nem todos os confrontos foram preenchidos corretamente (ex: empates ou gols vazios)
            // Aqui você pode adicionar um feedback ao usuário, como um Snackbar.
            return
        }

        if (faseAtual == FaseTorneio.FINAL) {
            _campeao.value = vencedores.firstOrNull()?.copy(status = TimeMataMata.Status.CAMPEAO)
            saveState()
            return
        }

        val proximaFase = FaseTorneio.getNext(faseAtual) ?: return
        val novosConfrontos = vencedores.chunked(2).map {
            Confronto(it[0].copy(status = TimeMataMata.Status.NEUTRO), it[1].copy(status = TimeMataMata.Status.NEUTRO), fase = proximaFase)
        }

        _confrontosPorFase.update {
            it.toMutableMap().apply {
                this[proximaFase] = novosConfrontos
            }
        }
        saveState()
    }

    fun resetTabela() {
        _confrontosPorFase.update { currentMap ->
            currentMap.mapValues { (_, confrontos) ->
                confrontos.map {
                    it.copy(
                        golsA = "",
                        golsB = "",
                        timeA = it.timeA.copy(status = TimeMataMata.Status.NEUTRO),
                        timeB = it.timeB.copy(status = TimeMataMata.Status.NEUTRO)
                    )
                }
            }.toMutableMap()
        }
        _campeao.value = null
        saveState()
    }

    fun clearAll() {
        _faseAtualConfig.value = FaseTorneio.OITAVAS
        _timesIniciais.value = List(_faseAtualConfig.value.confrontos * 2) { TimeMataMata(nome = "") } // Adicionado 'nome = ""'
        _confrontosPorFase.value = mutableMapOf()
        _campeao.value = null
        saveState()
    }

    private fun saveState() {
        savedStateHandle["faseAtualConfig"] = _faseAtualConfig.value.name // Salvar o nome do enum
        savedStateHandle["timesIniciais"] = ArrayList(_timesIniciais.value) // Converte para ArrayList para SavedStateHandle

        val serializableMap = _confrontosPorFase.value.mapKeys { it.key.name }
            .mapValues { entry ->
                ArrayList(entry.value.map { confronto ->
                    mapOf(
                        "timeAId" to confronto.timeA.id,
                        "timeANome" to confronto.timeA.nome,
                        "timeAStatus" to confronto.timeA.status.name,
                        "timeBId" to confronto.timeB.id,
                        "timeBNome" to confronto.timeB.nome,
                        "timeBStatus" to confronto.timeB.status.name,
                        "golsA" to confronto.golsA,
                        "golsB" to confronto.golsB,
                        "fase" to confronto.fase.name
                    )
                })
            }
        savedStateHandle["confrontosPorFase"] = serializableMap

        _campeao.value?.let {
            savedStateHandle["campeao"] = mapOf(
                "id" to it.id,
                "nome" to it.nome,
                "status" to it.status.name
            )
        } ?: savedStateHandle.remove<Map<String, String>>("campeao")
    }

    // Factory customizado para o ViewModel com SavedStateHandle
    class Factory(
        private val savedStateHandle: SavedStateHandle
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MataMataViewModel::class.java)) {
                return MataMataViewModel(savedStateHandle) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

// --- Composable Principal ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TelaMataMata(navController: NavHostController) { // navContoller não é usado aqui, pode ser removido se não for navegar
    val viewModel: MataMataViewModel = viewModel()
    val faseAtualConfig by viewModel.faseAtualConfig.collectAsState()
    val timesIniciais by viewModel.timesIniciais.collectAsState()
    val confrontosPorFase by viewModel.confrontosPorFase.collectAsState()
    val campeao by viewModel.campeao.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    var novoTimeNome by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val todasAsFases = remember(confrontosPorFase, faseAtualConfig) {
        val phasesWithConfrontos = confrontosPorFase.keys.sortedBy { it.ordinal }
        if (phasesWithConfrontos.isEmpty()) {
            listOf(faseAtualConfig)
        } else {
            // Incluir todas as fases até a fase atual mais a próxima, se houver
            val maxPhaseOrdinal = phasesWithConfrontos.last().ordinal
            FaseTorneio.entries.filter { it.ordinal <= maxPhaseOrdinal + 1 } // Pegar até a próxima fase para tab
                .sortedBy { it.ordinal }
        }
    }

    val initialPage = remember(faseAtualConfig, confrontosPorFase) {
        val fasesComConfrontos = confrontosPorFase.keys.sortedBy { it.ordinal }
        if (fasesComConfrontos.isNotEmpty()) {
            todasAsFases.indexOf(fasesComConfrontos.last())
        } else {
            todasAsFases.indexOf(faseAtualConfig)
        }.coerceAtLeast(0) // Garante que o index não seja negativo
    }

    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { todasAsFases.size })

    LaunchedEffect(pagerState.currentPage, todasAsFases) {
        // Correção para o erro "@Composable invocations can only happen..."
        // Não há invocações composable diretas aqui.
        // O pagerState.scrollToPage é uma função normal.
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (confrontosPorFase.isEmpty() && timesIniciais.any { it.nome.isBlank() }) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Time")
                }
            }
        },
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
            // Escolha da fase inicial e entrada de times
            if (confrontosPorFase.isEmpty()) {
                ConfiguracaoInicialTorneio(
                    faseAtualConfig = faseAtualConfig,
                    timesIniciais = timesIniciais,
                    onSetFaseInicial = viewModel::setFaseInicialConfig,
                    onUpdateTime = viewModel::updateTimeInicial,
                    onGerarChaves = {
                        viewModel.gerarChavesIniciais()
                        // Feedback ao usuário pode ser adicionado aqui, ou já será visível pela mudança de tela.
                    }
                )
            } else {
                // Exibição das fases do torneio
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 0.dp // Remove padding das bordas
                ) {
                    todasAsFases.forEachIndexed { index, fase ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                // Correção: Uso de LaunchedEffect para rolar a página
                                // Não diretamente aqui, mas o pagerState.animateScrollToPage() é a forma correta
                            },
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

                    if (confrontosDaFase != null && confrontosDaFase.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    fase.label,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            items(confrontosDaFase, key = { it.timeA.id }) { confronto -> // Usar ID do time A como key única para o confronto
                                MataMataConfrontoCard(
                                    confronto = confronto,
                                    onGolsChange = { golsA, golsB ->
                                        viewModel.preencherResultadosConfronto(
                                            fase,
                                            confronto.timeA.id, // Usamos o ID do timeA para identificar o confronto
                                            golsA,
                                            golsB
                                        )
                                    }
                                )
                            }

                            // Botão para avançar fase (se não for a final e todos os confrontos tiverem vencedores)
                            val todosResultadosPreenchidos = confrontosDaFase.all { it.vencedor != null }
                            if (fase != FaseTorneio.FINAL && todosResultadosPreenchidos) {
                                item {
                                    Spacer(Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            viewModel.avancarFase(fase)
                                            // Mudar para a próxima aba automaticamente
                                            val nextIndex = todasAsFases.indexOf(fase) + 1
                                            if (nextIndex < todasAsFases.size) {
                                                // Usar LaunchedEffect ou coroutine para animação de rolagem
                                                // Exemplo: coroutineScope.launch { pagerState.animateScrollToPage(nextIndex) }
                                                // Para simplicidade, vamos apenas atualizar o estado do pagerState
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp)
                                    ) {
                                        Text("Avançar para ${FaseTorneio.getNext(fase)?.label ?: "Final"}")
                                    }
                                }
                            }

                            // Exibição do Campeão
                            if (fase == FaseTorneio.FINAL && campeao != null) {
                                item {
                                    Spacer(Modifier.height(24.dp))
                                    CampeaoCard(campeao!!)
                                }
                            }
                        }
                    } else if (fase == faseAtualConfig && confrontosPorFase.isEmpty()) {
                        // Caso especial: a fase inicial selecionada ainda não tem confrontos gerados
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Configure os times iniciais para começar o torneio!",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        // Fase ainda não alcançada
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Esta fase ainda não foi alcançada.",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // --- Modais ---
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Adicionar Time") },
            text = {
                OutlinedTextField(
                    value = novoTimeNome,
                    onValueChange = { novoTimeNome = it },
                    label = { Text("Nome do Time") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val idx = timesIniciais.indexOfFirst { it.nome.isBlank() }
                        if (idx >= 0) {
                            viewModel.updateTimeInicial(idx, novoTimeNome)
                            // Feedback
                            // CoroutineScope.launch { snackbarHostState.showSnackbar("Time '$novoTimeNome' adicionado!") }
                        } else {
                            // CoroutineScope.launch { snackbarHostState.showSnackbar("Todos os slots de times estão preenchidos!") }
                        }
                        novoTimeNome = ""
                        showAddDialog = false
                    },
                    enabled = novoTimeNome.isNotBlank() && timesIniciais.any { it.nome.isBlank() }
                ) { Text("Adicionar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Resetar Tabela?") },
            text = { Text("Isso irá apagar todos os gols e resultados, mas manterá os times e a fase inicial.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.resetTabela()
                    showResetDialog = false
                    // CoroutineScope.launch { snackbarHostState.showSnackbar("Tabela resetada!") }
                }) { Text("Resetar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Limpar Tudo?") },
            text = { Text("Isso irá apagar todos os times e resultados, reiniciando o torneio. Deseja continuar?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.clearAll()
                    showClearDialog = false
                    // CoroutineScope.launch { snackbarHostState.showSnackbar("Torneio completamente limpo!") }
                }) { Text("Limpar Tudo") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

// --- Composables Reutilizáveis ---

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

        // Dropdown para escolher a fase inicial
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
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true) // Usando o novo menuAnchor
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                FaseTorneio.entries.forEach { fase -> // Usando .entries
                    if (fase != FaseTorneio.FINAL) { // Não permite começar pela final
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

        // Lista de campos para nomes dos times
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
                TimeConfrontoBox(time = confronto.timeA) // Removido onGolsChange
                Text(
                    "X",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TimeConfrontoBox(time = confronto.timeB) // Removido onGolsChange
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
fun TimeConfrontoBox(time: TimeMataMata) { // Removido o parâmetro onGolsChange
    val backgroundColor = when (time.status) {
        TimeMataMata.Status.CAMPEAO -> Color(0xFFFFD700).copy(alpha = 0.2f) // Dourado suave
        TimeMataMata.Status.VENCEDOR -> Color(0xFF4CAF50).copy(alpha = 0.2f) // Verde suave
        TimeMataMata.Status.ELIMINADO -> Color(0xFFD32F2F).copy(alpha = 0.1f) // Vermelho suave
        TimeMataMata.Status.NEUTRO -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when (time.status) {
        TimeMataMata.Status.CAMPEAO -> Color(0xFFDAA520) // Dourado mais forte
        TimeMataMata.Status.VENCEDOR -> Color(0xFF2E7D32) // Verde mais forte
        TimeMataMata.Status.ELIMINADO -> Color(0xFFB71C1C) // Vermelho mais forte
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
                modifier = Modifier.size(24.dp).padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun GolsTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 2 && it.all { char -> char.isDigit() }) { // Limita a 2 dígitos e apenas números
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