package com.example.futsim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.futsim.ui.telas.TelaPontosCorridos




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FutSimTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "tela_inicial"){
                    composable("tela_inicial") {TelaInicial(navController)}
                    composable("tela_principal") {TelaPrincipal(navController)}
                    composable("tela_teste") {TelaTeste(navController)}
                    composable("tela_campCriados") {TelaCampCriados(navController)}
                    composable("tela_mataMata") {TelaMataMata(navController) }
                    composable("tela_pontosCorridos") { TelaPontosCorridos(navController)  }
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