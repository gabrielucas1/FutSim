package com.example.futsim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.futsim.data.FutSimRepository
import com.example.futsim.model.Campeonato
import com.example.futsim.model.Partida
import com.example.futsim.model.Time
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FutSimViewModel(private val repository: FutSimRepository) : ViewModel() {

    private val _campeonatos = MutableStateFlow<List<Campeonato>>(emptyList())
    val campeonatos: StateFlow<List<Campeonato>> = _campeonatos

    private val _times = MutableStateFlow<List<Time>>(emptyList())
    val times: StateFlow<List<Time>> = _times

    private val _partidas = MutableStateFlow<List<Partida>>(emptyList())
    val partidas: StateFlow<List<Partida>> = _partidas

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