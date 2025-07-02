package com.example.futsim
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.futsim.data.FutSimDatabase
import com.example.futsim.data.FutSimRepository
import com.example.futsim.navigation.BottomNavItem
import com.example.futsim.navigation.bottomNavItemsList
import com.example.futsim.ui.telas.*
import com.example.futsim.ui.theme.FutSimTheme
import com.example.futsim.ui.viewmodel.FutSimViewModel
import com.example.futsim.ui.viewmodel.FutSimViewModelFactory
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
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
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(navController)
                        }
                    ) { innerPadding ->
                        AppNavHost(navController, Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItemsList.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.TelaInicial.route,
        modifier = modifier
    ) {
        // Chamadas corrigidas para as telas corretas
        composable(BottomNavItem.TelaInicial.route) { TelaInicial(navController) }
        composable(BottomNavItem.TelaPrincipal.route) { TelaPrincipal(navController) }
        composable(BottomNavItem.Campeonatos.route) { TelaCampCriados(navController) }
        composable("tela_CriandoCamp") { TelaCriarCamp(navController) }
        composable("tela_MataMata") { TelaMataMata(navController) }
        composable("tela_FaseGrupos") { TelaFaseDeGrupos(navController) }
        composable("tela_PontosCorridos/{campeonatoId}") { backStackEntry ->
            val campeonatoId =
                backStackEntry.arguments?.getString("campeonatoId")?.toIntOrNull()
                    ?: run {
                        Log.e("MainActivity", "ID do campeonato inválido ou nulo")
                        return@composable
                    }
            TelaPontosCorridos(navController, campeonatoId)
        }
    }
}