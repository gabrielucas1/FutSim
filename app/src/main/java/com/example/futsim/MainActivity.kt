package com.example.futsim

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.futsim.ui.theme.FutSimTheme
import androidx.navigation.compose.rememberNavController
import com.example.futsim.ui.telas.TelaInicial
import com.example.futsim.ui.telas.TelaPrincipal
import com.example.futsim.ui.telas.TelaTeste
import com.example.futsim.ui.telas.TelaMataMata
import com.example.futsim.ui.telas.TelaCampCriados
import com.example.futsim.ui.telas.TelaFaseDeGrupos
import com.example.futsim.ui.telas.TelaPontosCorridos


class MainActivity : ComponentActivity() {
    @SuppressLint("ComposableDestinationInComposeScope")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FutSimTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "tela_inicial") {
                    composable("tela_Inicial") { TelaInicial(navController) }
                    composable("tela_Principal") { TelaPrincipal(navController) }
                    composable("tela_CriandoCamp") { TelaTeste(navController) }
                    composable("tela_CampCriados") { TelaCampCriados(navController) }
                    composable("tela_MataMata") { TelaMataMata(navController) }
                    composable("tela_FaseGrupos") { TelaFaseDeGrupos(navController) }
                    composable("tela_PontosCorridos") {
                        TelaPontosCorridos(navController)
                        //composable("tela_FaseGrupos/{nomeCampeonato}") { backStackEntry ->
                        // val nomeCampeonato = backStackEntry.arguments?.getString("nomeCampeonato") ?: ""
                        // TelaFaseDeGrupos(navController, nomeCampeonato)
                        //}

                        //}
                    }

                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FutSimTheme {
        }
    }
}
