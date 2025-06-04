package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.futsim.ui.componentes.ButtonUniversal
import com.example.futsim.LocalFutSimViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import com.example.futsim.R

@Composable
fun TelaPrincipal(navHostController: NavHostController) {
    val viewModel = LocalFutSimViewModel.current
    val campeonatos by viewModel.campeonatos.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        ButtonUniversal(
            text = stringResource(R.string.criar_campeonato),
            onClick = { navHostController.navigate("tela_CriandoCamp") },
            modifier = Modifier.padding(top = 10.dp, bottom = 50.dp).width(380.dp).height(45.dp),
            fontSize = 30.sp,
        )

        ButtonUniversal(
            text = stringResource(R.string.campeonatos_criados),
            onClick = { navHostController.navigate("tela_CampCriados") },
            modifier = Modifier.padding(top = 2.dp, bottom = 80.dp).width(380.dp).height(45.dp),
            fontSize = 28.sp,
        )

        ButtonUniversal(
            text = stringResource(R.string.tela_inicial),
            onClick = { navHostController.navigate("tela_inicial") },
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 50.dp)
                .width(135.dp)
                .height(60.dp)
        )
    }
}