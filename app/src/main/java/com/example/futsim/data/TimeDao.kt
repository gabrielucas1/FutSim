package com.example.futsim.data

import androidx.room.*
import com.example.futsim.model.Time

@Dao
interface TimeDao {
    @Insert suspend fun inserir(time: Time): Long
    @Query("SELECT * FROM times") suspend fun listarTodos(): List<Time>
    @Query("SELECT * FROM times WHERE campeonatoId = :campeonatoId")
    suspend fun listarPorCampeonato(campeonatoId: Int): List<Time>
    @Update suspend fun atualizar(time: Time)
    @Delete suspend fun deletar(time: Time)
}