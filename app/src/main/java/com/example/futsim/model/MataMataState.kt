package com.example.futsim.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Esta classe representa a tabela que guardará o estado do torneio
@Entity(tableName = "mata_mata_state")
data class MataMataState(
    @PrimaryKey val campeonatoId: Int, // A ID do campeonato
    val stateJson: String              // O estado completo do torneio em formato JSON
)