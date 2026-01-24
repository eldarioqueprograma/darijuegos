package com.zdari.dado.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceGameScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "🎲 Juego del Dado",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            JuegoDeDadoMultijugador()
        }
    }
}

@Composable
fun DadoTexto(numero: Int) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color.Cyan, shape = RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = numero.toString(),
            fontSize = 48.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuegoDeDadoMultijugador() {
    var turnoJugador1 by remember { mutableStateOf(true) }
    var dadoJugador1 by remember { mutableStateOf(0) }
    var dadoJugador2 by remember { mutableStateOf(0) }
    var puntajeJugador1 by remember { mutableStateOf(0) }
    var puntajeJugador2 by remember { mutableStateOf(0) }
    var resultadoRonda by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Turno de: ${if (turnoJugador1) "Jugador 1" else "Jugador 2"}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Jugador 1", fontWeight = FontWeight.Medium)
                if (dadoJugador1 > 0) DadoTexto(numero = dadoJugador1)
                Text("Puntos: $puntajeJugador1", fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Jugador 2", fontWeight = FontWeight.Medium)
                if (dadoJugador2 > 0) DadoTexto(numero = dadoJugador2)
                Text("Puntos: $puntajeJugador2", fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        if (resultadoRonda.isNotEmpty()) {
            Card(
                modifier = Modifier.padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC8E6C9))
            ) {
                Text(
                    text = resultadoRonda,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2E7D32)
                )
            }
        }

        Button(
            onClick = {
                val numero = (1..6).random()
                if (turnoJugador1) {
                    dadoJugador1 = numero
                } else {
                    dadoJugador2 = numero

                    resultadoRonda = when {
                        dadoJugador1 > dadoJugador2 -> {
                            puntajeJugador1 += 1
                            "¡Jugador 1 gana la ronda!"
                        }
                        dadoJugador2 > dadoJugador1 -> {
                            puntajeJugador2 += 1
                            "¡Jugador 2 gana la ronda!"
                        }
                        else -> "Empate"
                    }
                }
                turnoJugador1 = !turnoJugador1
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(
                if (turnoJugador1) "Lanzar dado (J1)" else "Lanzar dado (J2)",
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                dadoJugador1 = 0
                dadoJugador2 = 0
                resultadoRonda = ""
                turnoJugador1 = true
                puntajeJugador1 = 0
                puntajeJugador2 = 0
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reiniciar Juego")
        }
    }
}