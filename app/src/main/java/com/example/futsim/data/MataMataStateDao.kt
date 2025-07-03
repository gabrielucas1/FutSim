package com.example.futsim.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.futsim.model.MataMataState

@Dao
interface MataMataStateDao {
    // Salva ou atualiza o estado de um campeonato
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveState(state: MataMataState)

    // Pega o estado de um campeonato específico
    @Query("SELECT * FROM mata_mata_state WHERE campeonatoId = :campeonatoId")
    suspend fun getState(campeonatoId: Int): MataMataState?
}