package com.example.futsim.ui.telas

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.futsim.R // Importação adicionada para acessar R.string
import com.example.futsim.model.Campeonato
import com.example.futsim.model.TipoCampeonato
import com.example.futsim.navigation.BottomNavItem
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCriarCamp(navHostController: NavHostController) {
    val viewModel = LocalFutSimViewModel.current

    var nome by remember { mutableStateOf("") }
    var tipoCampeonato by remember { mutableStateOf<TipoCampeonato?>(null) }
    var nomeError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.novo_campeonato_titulo), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigate(BottomNavItem.Campeonatos.route) {
                            popUpTo(navHostController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    nomeError = nome.isBlank()
                    if (tipoCampeonato != null && !nomeError) {
                        val campeonato = Campeonato(nome = nome, tipo = tipoCampeonato!!)
                        viewModel.inserirCampeonato(campeonato)

                        navHostController.navigate(BottomNavItem.Campeonatos.route) {
                            popUpTo(navHostController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = nome.isNotBlank() && tipoCampeonato != null
            ) {
                Text(stringResource(R.string.criar_campeonato), fontSize = 18.sp, color = Color.White) 
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = {
                    nome = it
                    nomeError = it.isBlank()
                },
                label = { Text(stringResource(R.string.nome_campeonato)) }, // String substituída
                isError = nomeError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (nomeError) {
                Text(
                    text = stringResource(R.string.nome_campeonato_obrigatorio_erro), // String substituída
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.selecione_formato), // String substituída
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            TipoCampeonatoCard(
                label = stringResource(R.string.tipo_pontos_corridos), // String substituída
                description = stringResource(R.string.desc_pontos_corridos), // String substituída
                icon = Icons.Default.BarChart,
                isSelected = tipoCampeonato == TipoCampeonato.PONTOS_CORRIDOS,
                onClick = { tipoCampeonato = TipoCampeonato.PONTOS_CORRIDOS }
            )
            Spacer(Modifier.height(12.dp))
            TipoCampeonatoCard(
                label = stringResource(R.string.tipo_mata_mata), // String substituída
                description = stringResource(R.string.desc_mata_mata), // String substituída
                icon = Icons.Default.MilitaryTech,
                isSelected = tipoCampeonato == TipoCampeonato.MATA_MATA,
                onClick = { tipoCampeonato = TipoCampeonato.MATA_MATA }
            )
            Spacer(Modifier.height(12.dp))
            TipoCampeonatoCard(
                label = stringResource(R.string.tipo_fase_grupos), // String substituída
                description = stringResource(R.string.desc_fase_grupos), // String substituída
                icon = Icons.Default.Groups,
                isSelected = tipoCampeonato == TipoCampeonato.FASE_GRUPOS,
                onClick = { tipoCampeonato = TipoCampeonato.FASE_GRUPOS }
            )
        }
    }
}

@Composable
fun TipoCampeonatoCard(
    label: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label, // contentDescription pode usar a própria label aqui, já que ela já é uma string localizada.
                modifier = Modifier.size(40.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
