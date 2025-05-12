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

                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FutSimTheme {
        Greeting("Android")
    }
}