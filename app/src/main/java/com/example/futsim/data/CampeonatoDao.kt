package com.example.futsim.data

import androidx.room.*
import com.example.futsim.model.Campeonato

@Dao
interface CampeonatoDao {
    @Insert suspend fun inserir(campeonato: Campeonato): Long
    @Query("SELECT * FROM campeonatos") suspend fun listarTodos(): List<Campeonato>
    @Delete suspend fun deletar(campeonato: Campeonato)
    @Update suspend fun atualizar(campeonato: Campeonato)
}