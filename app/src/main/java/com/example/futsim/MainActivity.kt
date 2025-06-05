package com.example.futsim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.futsim.data.FutSimDatabase
import com.example.futsim.data.FutSimRepository
import com.example.futsim.ui.theme.FutSimTheme
import com.example.futsim.ui.viewmodel.FutSimViewModel
import com.example.futsim.ui.viewmodel.FutSimViewModelFactory
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.futsim.ui.telas.*
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            FutSimDatabase::class.java,
            "futsim_db"
        ).build()
        val repository = FutSimRepository(
            database.timeDao(),
            database.campeonatoDao(),
            database.partidaDao()
        )
        val factory = FutSimViewModelFactory(repository)

        setContent {
            FutSimTheme {
                val navController = rememberNavController()
                val viewModel: FutSimViewModel = viewModel(factory = factory)

                CompositionLocalProvider(LocalFutSimViewModel provides viewModel) {
                    NavHost(navController = navController, startDestination = "tela_inicial") {
                        composable("tela_inicial") { TelaInicial(navController) }
                        composable("tela_principal") { TelaPrincipal(navController) }
                        composable("tela_CriandoCamp") { TelaTeste(navController) }
                        composable("tela_CampCriados") { TelaCampCriados(navController) }
                        composable("tela_MataMata") { TelaMataMata(navController) }
                        composable("tela_FaseGrupos") { TelaFaseDeGrupos(navController) }
                        composable("tela_PontosCorridos") { TelaPontosCorridos(navController) }
                    }
                }
            }
        }
    }
}