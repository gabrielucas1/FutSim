package com.example.futsim.data

import androidx.room.*
import com.example.futsim.model.Partida

@Dao
interface PartidaDao {
    @Insert suspend fun inserir(partida: Partida): Long
    @Query("SELECT * FROM partidas WHERE campeonatoId = :campeonatoId")
    suspend fun listarPorCampeonato(campeonatoId: Int): List<Partida>
    @Delete suspend fun deletar(partida: Partida)
    @Update suspend fun atualizar(partida: Partida)
}