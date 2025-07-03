package com.example.futsim.ui.telas

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.futsim.model.Campeonato
import com.example.futsim.model.TipoCampeonato
import com.example.futsim.ui.viewmodel.LocalFutSimViewModel
import kotlinx.coroutines.launch

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
                title = { Text("Novo Campeonato", fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    nomeError = nome.isBlank()
                    if (tipoCampeonato != null && !nomeError) {
                        val campeonato = Campeonato(nome = nome, tipo = tipoCampeonato!!)
                        viewModel.inserirCampeonato(campeonato)

                        // ✅ NAVEGAÇÃO AJUSTADA AQUI
                        // Navega para a lista de campeonatos criados
                        navHostController.navigate("tela_CampCriados") {
                            // Limpa a tela de criação da pilha de navegação,
                            // para que o usuário não volte para ela ao pressionar "Voltar".
                            popUpTo("tela_CriandoCamp") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = nome.isNotBlank() && tipoCampeonato != null,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Criar Campeonato", fontSize = 18.sp, color = Color.White)
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
                label = { Text("Nome do Campeonato") },
                isError = nomeError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (nomeError) {
                Text(
                    text = "O nome do campeonato é obrigatório",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Selecione o formato",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            TipoCampeonatoCard(
                label = "Pontos Corridos",
                description = "Liga onde todos jogam contra todos.",
                icon = Icons.Default.BarChart,
                isSelected = tipoCampeonato == TipoCampeonato.PONTOS_CORRIDOS,
                onClick = { tipoCampeonato = TipoCampeonato.PONTOS_CORRIDOS }
            )
            Spacer(Modifier.height(12.dp))
            TipoCampeonatoCard(
                label = "Mata-Mata",
                description = "Torneio eliminatório até a final.",
                icon = Icons.Default.MilitaryTech,
                isSelected = tipoCampeonato == TipoCampeonato.MATA_MATA,
                onClick = { tipoCampeonato = TipoCampeonato.MATA_MATA }
            )
            Spacer(Modifier.height(12.dp))
            TipoCampeonatoCard(
                label = "Fase de Grupos",
                description = "Grupos com classificação para o mata-mata.",
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
                contentDescription = label,
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