package com.example.futsim.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController


import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.futsim.ui.componentes.ButtonUniversal

@Composable
fun TelaTeste(navHostController: NavHostController){
    var nome by remember { mutableStateOf("") }
    var checkMataMata by remember { mutableStateOf(false) }
    var checkFaseGrupos by remember { mutableStateOf(false) }


// Essa coluna engloba toda a estrutura do projeto
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment =  Alignment.CenterHorizontally)
    {

        // onde fica o nome do campeonato
            TextField(
                value = nome,
                onValueChange ={nome = it},
                placeholder = { Text( text = "Insira o nome do campeonato") },
                label = { Text("Nome Campeoanto")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 120.dp),
                )

 // Essa coluna engloba os dois checkboxes
        Column(modifier = Modifier.padding(top = 250.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment =  Alignment.CenterHorizontally) {
            // inicio do posicionamento  dos checkbox de forma horizontal
            Row (modifier = Modifier.padding(end = 90.dp)){
            Checkbox(
                checked = checkMataMata, // caixa aparece seleciona se for true, parte visual do check
                onCheckedChange = {
                    checkMataMata =
                        it // ele que faz mudança da caixa seleciona, parte logica do check
                    if (it) checkFaseGrupos = false
                }, // nessa linha o if recebe o estado atual da caixa seleciona e se ela for true a outra opção não é possivel selecionala
                modifier = Modifier.padding(end = 20.dp, bottom = 20.dp).scale(2f),
            )
            Text(text = "Mata-Mata", fontSize = 40.sp)
        } // fim do primeiro checbox

            // inicio do posicionamento  dos checkbox de forma horizontal
            Row(modifier = Modifier.padding(end = 1.dp, bottom = 30.dp)){

                Checkbox(
                    checked = checkFaseGrupos,
                    onCheckedChange = {
                        checkFaseGrupos = it
                        if (it) checkMataMata = false
                    },
                    modifier = Modifier.padding(end = 20.dp).scale(2f),

                    )
                Text(text = "Fase de Grupos", fontSize = 40.sp)
            } // fim do segundo checkbox


        } // fim da coluna dos checkboxs

        Column {
            if (checkMataMata){
                Text(text = "Mata Mata selecionada")


            }
            else if (checkFaseGrupos){
                Text(text = "Fase de Grupos selecionada")

            }
            else{
                Text(text = "Nenhuma opção selecionada")

            }
        }

        Spacer(modifier = Modifier.weight(1f))

            ButtonUniversal(
                text = "CRIAR",
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .width(135.dp)
                    .height(60.dp),
                textColor = Color.White,
                onClick = {
                if(checkMataMata){
                    navHostController.navigate("tela_MataMata")
                }
                else if(checkFaseGrupos){
                    navHostController.navigate("TelaFaseGrupos")
                }
    }


            )


    }


}
