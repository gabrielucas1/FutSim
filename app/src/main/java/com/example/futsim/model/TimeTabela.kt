package com.example.futsim.model

data class TimeTabela(
    val posicao: Int,
    val nome: String,
    val pontos: Int,
    val jogos: Int,
    val vitorias: Int,
    val empates: Int,
    val derrotas: Int,
    val golsPro: Int,
    val golsContra: Int
) {
    val saldoGols: Int
        get() = golsPro - golsContra
}