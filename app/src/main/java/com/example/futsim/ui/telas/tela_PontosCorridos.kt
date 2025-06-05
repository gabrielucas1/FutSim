package com.example.futsim.ui.telas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.futsim.data.CampeonatoRepository
import com.example.futsim.ui.componentes.ButtonUniversal
import com.example.futsim.ui.viewmodel.FutSimViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel

@Composable
fun TelaPontosCorridos(navHostController: NavHostController) {
    val viewModel = LocalFutSimViewModel.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Campeonatos Pontos Corridos", modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(CampeonatoRepository.campeonatosPontosCorridos) { campeonato ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navHostController.navigate("tela_FaseGrupos")
                        }
                )  {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nome: ${campeonato.nome}", fontSize = 22.sp)
                        Text("Tipo: ${campeonato.tipo.name}", fontSize = 18.sp)
                    }
                }
            }
        }

        ButtonUniversal(
            text = "Tela Principal",
            onClick = { navHostController.navigate("tela_Principal") },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}
