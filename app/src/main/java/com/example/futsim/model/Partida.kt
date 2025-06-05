package com.example.futsim.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidas")
data class Partida(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val campeonatoId: Int,
    val timeCasaId: Int,
    val timeForaId: Int,
    val golsCasa: Int?,
    val golsFora: Int?,
    val rodada: Int
)