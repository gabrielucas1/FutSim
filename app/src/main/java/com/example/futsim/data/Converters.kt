package com.example.futsim.data

import androidx.room.TypeConverter
import com.example.futsim.model.TipoCampeonato

class Converters {
    @TypeConverter
    fun fromTipoCampeonato(value: TipoCampeonato): String = value.name

    @TypeConverter
    fun toTipoCampeonato(value: String): TipoCampeonato = TipoCampeonato.valueOf(value)
}