package com.example.futsim.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Settings // Exemplo de ícone
import androidx.compose.material.icons.filled.SportsSoccer

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object TelaInicial : BottomNavItem("tela_inicial", "Início", Icons.Filled.Home)
    object TelaPrincipal : BottomNavItem("tela_principal", "Adicionar", Icons.Filled.Add)
    object Campeonatos : BottomNavItem("tela_CampCriados", "Campeonatos", Icons.Filled.SportsSoccer) // ADICIONADO

}

val bottomNavItemsList = listOf(
    BottomNavItem.TelaInicial,
    BottomNavItem.TelaPrincipal,
    BottomNavItem.Campeonatos
)