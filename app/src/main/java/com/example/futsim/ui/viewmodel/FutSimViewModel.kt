package com.example.futsim.ui.viewmodel

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FutSimViewModel(private val repository: FutSimRepository) : ViewModel() {

    private val _campeonatos = MutableStateFlow<List<Campeonato>>(emptyList())
    val campeonatos: StateFlow<List<Campeonato>> = _campeonatos

    private val _times = MutableStateFlow<List<Time>>(emptyList())
    val times: StateFlow<List<Time>> = _times

    private val _partidas = MutableStateFlow<List<Partida>>(emptyList())
    val partidas: StateFlow<List<Partida>> = _partidas

    // --- LÓGICA NOVA PARA O MATA-MATA ---
    private val _mataMataUiState = MutableStateFlow(MataMataUiState())
    val mataMataUiState: StateFlow<MataMataUiState> = _mataMataUiState.asStateFlow()

    init {
        // Salva o estado do mata-mata no banco de dados 300ms depois de qualquer mudança.
        // Isso evita salvar no banco a cada caractere digitado.
        viewModelScope.launch {
            _mataMataUiState
                .debounce(300)
                .collect { state ->
                    val campeonatoId = _campeonatoIdAtual.value
                    if (campeonatoId != null) {
                        val jsonState = Json.encodeToString(state)
                        repository.saveMataMataState(MataMataState(campeonatoId, jsonState))
                    }
                }
        }
    }

    // Guarda a ID do campeonato de mata-mata que está sendo visualizado
    private val _campeonatoIdAtual = MutableStateFlow<Int?>(null)

    fun carregarEstadoMataMata(campeonatoId: Int) {
        _campeonatoIdAtual.value = campeonatoId
        viewModelScope.launch {
            val savedState = repository.getMataMataState(campeonatoId)
            if (savedState != null) {
                _mataMataUiState.value = Json.decodeFromString<MataMataUiState>(savedState.stateJson)
            } else {
                _mataMataUiState.value = MataMataUiState() // Estado inicial se não houver nada salvo
            }
        }
    }

    // As funções de manipulação do estado agora estão aqui
    fun updateMataMataState(newState: MataMataUiState) {
        _mataMataUiState.value = newState
    }
    // --- FIM DA LÓGICA NOVA ---


    //Campeonatos
    fun inserirCampeonato(campeonato: Campeonato) {
        viewModelScope.launch {
            repository.inserirCampeonato(campeonato)
            carregarCampeonatos()
        }
    }

    fun carregarCampeonatos() {
        viewModelScope.launch {
            _campeonatos.value = repository.listarCampeonatos()
        }
    }

    fun atualizarCampeonato(campeonato: Campeonato) {
        viewModelScope.launch {
            repository.atualizarCampeonato(campeonato)
            carregarCampeonatos()
        }
    }

    fun deletarCampeonato(campeonato: Campeonato) {
        viewModelScope.launch {
            repository.deletarCampeonato(campeonato)
            carregarCampeonatos()
        }
    }

    //Times
    fun inserirTime(time: Time) {
        viewModelScope.launch {
            repository.inserirTime(time)
            carregarTimesPorCampeonato(time.campeonatoId)
        }
    }

    fun carregarTimes() {
        viewModelScope.launch {
            _times.value = repository.listarTimes()
        }
    }

    fun carregarTimesPorCampeonato(campeonatoId: Int) {
        viewModelScope.launch {
            _times.value = repository.listarTimesPorCampeonato(campeonatoId)
        }
    }

    fun atualizarTime(time: Time) {
        viewModelScope.launch {
            repository.atualizarTime(time)
            carregarTimesPorCampeonato(time.campeonatoId)
        }
    }

    fun deletarTime(time: Time) {
        viewModelScope.launch {
            repository.deletarTime(time)
            carregarTimesPorCampeonato(time.campeonatoId)
        }
    }

    //Partidas
    fun inserirPartida(partida: Partida) {
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