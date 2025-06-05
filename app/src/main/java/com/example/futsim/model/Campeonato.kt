package com.example.futsim.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.futsim.model.TipoCampeonato

@Entity(tableName = "campeonatos")
data class Campeonato(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val tipo: TipoCampeonato
)