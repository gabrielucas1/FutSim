package com.example.futsim.data

import com.example.futsim.model.Campeonato
import com.example.futsim.model.Partida
import com.example.futsim.model.Time

class FutSimRepository(
    private val timeDao: TimeDao,
    private val campeonatoDao: CampeonatoDao,
    private val partidaDao: PartidaDao
) {
    //Times
    suspend fun inserirTime(time: Time) = timeDao.inserir(time)
    suspend fun listarTimes() = timeDao.listarTodos()
    suspend fun deletarTime(time: Time) = timeDao.deletar(time)

    //Campeonatos
    suspend fun inserirCampeonato(campeonato: Campeonato) = campeonatoDao.inserir(campeonato)
    suspend fun listarCampeonatos() = campeonatoDao.listarTodos()
    suspend fun deletarCampeonato(campeonato: Campeonato) = campeonatoDao.deletar(campeonato)
    suspend fun atualizarCampeonato(campeonato: Campeonato) = campeonatoDao.atualizar(campeonato)

    //Partidas
    suspend fun inserirPartida(partida: Partida) = partidaDao.inserir(partida)
    suspend fun listarPartidasPorCampeonato(campeonatoId: Int) = partidaDao.listarPorCampeonato(campeonatoId)
    suspend fun deletarPartida(partida: Partida) = partidaDao.deletar(partida)
    suspend fun atualizarPartida(partida: Partida) = partidaDao.atualizar(partida)
}