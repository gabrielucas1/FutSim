package com.example.futsim.data

import com.example.futsim.model.Campeonato
import com.example.futsim.model.MataMataState
import com.example.futsim.model.Partida
import com.example.futsim.model.Time

class FutSimRepository(
    private val timeDao: TimeDao,
    private val campeonatoDao: CampeonatoDao,
    private val partidaDao: PartidaDao,
    private val mataMataStateDao: MataMataStateDao // Adicione este parâmetro
) {
    //Times
    suspend fun inserirTime(time: Time) = timeDao.inserir(time)
    suspend fun listarTimes() = timeDao.listarTodos()
    suspend fun listarTimesPorCampeonato(campeonatoId: Int) = timeDao.listarPorCampeonato(campeonatoId)
    suspend fun deletarTime(time: Time) = timeDao.deletar(time)
    suspend fun atualizarTime(time: Time) = timeDao.atualizar(time)

    //Campeonatos
    suspend fun inserirCampeonato(campeonato: Campeonato) = campeonatoDao.inserir(campeonato)
    suspend fun listarCampeonatos() = campeonatoDao.listarTodos()
    suspend fun atualizarCampeonato(campeonato: Campeonato) = campeonatoDao.atualizar(campeonato)
    suspend fun deletarCampeonato(campeonato: Campeonato) = campeonatoDao.deletar(campeonato)

    //Partidas
    suspend fun inserirPartida(partida: Partida) = partidaDao.inserir(partida)
    suspend fun listarPartidasPorCampeonato(campeonatoId: Int) = partidaDao.listarPorCampeonato(campeonatoId)
    suspend fun atualizarPartida(partida: Partida) = partidaDao.atualizar(partida)
    suspend fun deletarPartida(partida: Partida) = partidaDao.deletar(partida)

    // MÉTODOS NOVOS PARA PERSISTÊNCIA DO MATA-MATA
    suspend fun getMataMataState(campeonatoId: Int): MataMataState? = mataMataStateDao.getState(campeonatoId)
    suspend fun saveMataMataState(state: MataMataState) = mataMataStateDao.saveState(state)
}