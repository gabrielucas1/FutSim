package com.example.futsim.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.futsim.model.*

@Database(
    // 1. Adicionada a nova entidade MataMataState
    entities = [Time::class, Campeonato::class, Partida::class, MataMataState::class],
    // 2. Versão do banco incrementada para 3
    version = 3,
    // 3. Adicionado para simplificar o processo de migração durante o desenvolvimento
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FutSimDatabase : RoomDatabase() {
    abstract fun timeDao(): TimeDao
    abstract fun campeonatoDao(): CampeonatoDao
    abstract fun partidaDao(): PartidaDao
    // 4. Adicionado o DAO para a nova tabela
    abstract fun mataMataStateDao(): MataMataStateDao
}