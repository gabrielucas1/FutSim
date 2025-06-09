package com.example.futsim.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.futsim.model.*

@Database(
    entities = [Time::class, Campeonato::class, Partida::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class FutSimDatabase : RoomDatabase() {
    abstract fun timeDao(): TimeDao
    abstract fun campeonatoDao(): CampeonatoDao
    abstract fun partidaDao(): PartidaDao
}