package com.example.futsim.ui.viewmodel

import android.util.Log // Certifique-se de que esta importação está presente
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsim.data.FutSimRepository
import com.example.futsim.model.Campeonato
import com.example.futsim.model.MataMataState
import com.example.futsim.model.Partida
import com.example.futsim.model.Time
import com.example.futsim.ui.telas.MataMataUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
// import kotlinx.coroutines.flow.update // Não está sendo usado diretamente, pode ser removido se não for necessário
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FutSimViewModel(private val repository: FutSimRepository) : ViewModel() {

    private val _campeonatos = MutableStateFlow<List<Campeonato>>(emptyList())
    val campeonatos: StateFlow<List<Campeonato>> = _campeonatos.asStateFlow() // Adicionado .asStateFlow() para consistência

    private val _times = MutableStateFlow<List<Time>>(emptyList())
    val times: StateFlow<List<Time>> = _times.asStateFlow() // Adicionado .asStateFlow()

    private val _partidas = MutableStateFlow<List<Partida>>(emptyList())
    val partidas: StateFlow<List<Partida>> = _partidas.asStateFlow() // Adicionado .asStateFlow()

    // --- LÓGICA NOVA PARA O MATA-MATA ---
    private val _mataMataUiState = MutableStateFlow(MataMataUiState())
    val mataMataUiState: StateFlow<MataMataUiState> = _mataMataUiState.asStateFlow()

    // Guarda a ID do campeonato de mata-mata que está sendo visualizado
    private val _campeonatoIdAtual = MutableStateFlow<Int?>(null) // Mova esta declaração para antes do init se for usada nele

    init {
        Log.d("VIEWMODEL_LIFECYCLE", "FutSimViewModel: INIT") // LOG ADICIONADO
        // Salva o estado do mata-mata no banco de dados 300ms depois de qualquer mudança.
        // Isso evita salvar no banco a cada caractere digitado.
        viewModelScope.launch {
            _mataMataUiState
                .debounce(300)
                .collect { state ->
                    val campeonatoId = _campeonatoIdAtual.value // Certifique-se que _campeonatoIdAtual é inicializado antes
                    if (campeonatoId != null) {
                        val jsonState = Json.encodeToString(state)
                        repository.saveMataMataState(MataMataState(campeonatoId, jsonState))
                        Log.d("VIEWMODEL_LIFECYCLE", "MataMata state saved for ID: $campeonatoId")
                    }
                }
        }
        // É uma boa prática carregar dados iniciais aqui se necessário para todas as instâncias do ViewModel
        // Exemplo: carregarCampeonatos() // Se você sempre quer carregar campeonatos na inicialização
    }

    // LOG ADICIONADO
    override fun onCleared() {
        super.onCleared()
        Log.d("VIEWMODEL_LIFECYCLE", "FutSimViewModel: CLEARED")
    }

    fun carregarEstadoMataMata(campeonatoId: Int) {
        _campeonatoIdAtual.value = campeonatoId
        viewModelScope.launch {
            val savedState = repository.getMataMataState(campeonatoId)
            if (savedState != null) {
                try { // Adicionado try-catch para robustez na desserialização
                    _mataMataUiState.value = Json.decodeFromString<MataMataUiState>(savedState.stateJson)
                    Log.d("VIEWMODEL_LIFECYCLE", "MataMata state loaded for ID: $campeonatoId")
                } catch (e: Exception) {
                    Log.e("VIEWMODEL_LIFECYCLE", "Error decoding MataMataUiState for ID $campeonatoId: ${e.message}")
                    _mataMataUiState.value = MataMataUiState() // Resetar para estado inicial em caso de erro
                }
            } else {
                Log.d("VIEWMODEL_LIFECYCLE", "No saved MataMata state found for ID: $campeonatoId. Initializing.")
                _mataMataUiState.value = MataMataUiState() // Estado inicial se não houver nada salvo
            }
        }
    }

    fun updateMataMataState(newState: MataMataUiState) {
        _mataMataUiState.value = newState
    }
    // --- FIM DA LÓGICA NOVA ---


    //Campeonatos
    fun inserirCampeonato(campeonato: Campeonato) {
        Log.d("VIEWMODEL_DATABASE", "Inserindo campeonato: ${campeonato.nome}")
        viewModelScope.launch {
            repository.inserirCampeonato(campeonato)
            carregarCampeonatos() // Atualiza a lista após inserção
        }
    }

    fun carregarCampeonatos() {
        Log.d("VIEWMODEL_DATABASE", "Carregando todos os campeonatos")
        viewModelScope.launch {
            _campeonatos.value = repository.listarCampeonatos()
            Log.d("VIEWMODEL_DATABASE", "Campeonatos carregados: ${_campeonatos.value.size} encontrados.")
        }
    }

    fun atualizarCampeonato(campeonato: Campeonato) {
        Log.d("VIEWMODEL_DATABASE", "Atualizando campeonato: ${campeonato.nome}")
        viewModelScope.launch {
            repository.atualizarCampeonato(campeonato)
            carregarCampeonatos()
        }
    }

    fun deletarCampeonato(campeonato: Campeonato) {
        Log.d("VIEWMODEL_DATABASE", "Deletando campeonato: ${campeonato.nome}")
        viewModelScope.launch {
            repository.deletarCampeonato(campeonato)
            carregarCampeonatos()
        }
    }

    //Times
    fun inserirTime(time: Time) {
        Log.d("VIEWMODEL_DATABASE", "Inserindo time: ${time.nome} no camp ID: ${time.campeonatoId}")
        viewModelScope.launch {
            repository.inserirTime(time)
            // Considerar se precisa recarregar todos os times ou apenas os do campeonato específico
            carregarTimesPorCampeonato(time.campeonatoId)
        }
    }

    fun carregarTimes() { // Esta função parece não estar sendo usada. Se for, adicione logs.
        Log.d("VIEWMODEL_DATABASE", "Carregando todos os times")
        viewModelScope.launch {
            _times.value = repository.listarTimes()
        }
    }

    fun carregarTimesPorCampeonato(campeonatoId: Int) {
        Log.d("VIEWMODEL_DATABASE", "Carregando times para o campeonato ID: $campeonatoId")
        viewModelScope.launch {
            _times.value = repository.listarTimesPorCampeonato(campeonatoId)
            Log.d("VIEWMODEL_DATABASE", "Times para o camp ID $campeonatoId carregados: ${_times.value.size} encontrados.")
        }
    }

    fun atualizarTime(time: Time) {
        Log.d("VIEWMODEL_DATABASE", "Atualizando time: ${time.nome}")
        viewModelScope.launch {
            repository.atualizarTime(time)
            carregarTimesPorCampeonato(time.campeonatoId)
        }
    }

    fun deletarTime(time: Time) {
        Log.d("VIEWMODEL_DATABASE", "Deletando time: ${time.nome}")
        viewModelScope.launch {
            repository.deletarTime(time)
            carregarTimesPorCampeonato(time.campeonatoId)
        }
    }

    //Partidas
    fun inserirPartida(partida: Partida) {
        Log.d("VIEWMODEL_DATABASE", "Inserindo partida") // Adicionar mais detalhes da partida se útil
        viewModelScope.launch {
            repository.inserirPartida(partida)
        }
    }

    fun carregarPartidasPorCampeonato(campeonatoId: Int) {
        viewModelScope.launch {
            _partidas.value = repository.listarPartidasPorCampeonato(campeonatoId)
        }
    }
}