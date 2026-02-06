package com.zdari.dado.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessNumberScreen(navController: NavController) {
    var targetNumber by remember { mutableStateOf((1..100).random()) }
    var userGuess by remember { mutableStateOf("") }
    var attempts by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf("") }
    var gameWon by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf("") }
    var showHint by remember { mutableStateOf(false) }
    var hintPenalty by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "🔢 Adivina el Número",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF7B1FA2)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        targetNumber = (1..100).random()
                        userGuess = ""
                        attempts = 0
                        message = ""
                        gameWon = false
                        hint = ""
                        showHint = false
                        hintPenalty = 0
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Nuevo Juego",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF9C27B0), Color(0xFFE1BEE7))
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Información del juego
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Adivina el número entre 1 y 100",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF7B1FA2)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoCard("Intentos", attempts.toString(), Color(0xFF7B1FA2))
                        InfoCard("Penalización", hintPenalty.toString(), Color(0xFFF44336))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Área de resultados
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (gameWon) {
                        Text(
                            text = "🎉 ¡Felicidades! 🎉",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Adivinaste en $attempts intentos",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                        if (hintPenalty > 0) {
                            Text(
                                text = "(Con penalización de $hintPenalty puntos)",
                                fontSize = 14.sp,
                                color = Color(0xFFF44336)
                            )
                        }
                    } else if (message.isNotEmpty()) {
                        Text(
                            text = message,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = when {
                                message.contains("muy bajo") -> Color(0xFF2196F3)
                                message.contains("muy alto") -> Color(0xFFFF5722)
                                else -> Color(0xFF7B1FA2)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Barra de progreso visual
                        LinearProgressIndicator(
                            progress = when {
                                message.contains("muy bajo") -> 0.3f
                                message.contains("muy alto") -> 0.7f
                                else -> 0.5f
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(8.dp),
                            color = when {
                                message.contains("muy bajo") -> Color(0xFF2196F3)
                                message.contains("muy alto") -> Color(0xFFFF5722)
                                else -> Color(0xFF7B1FA2)
                            }
                        )
                    } else {
                        Text(
                            text = "¡Empieza a adivinar!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF7B1FA2)
                        )
                    }
                    
                    if (showHint && hint.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                        ) {
                            Text(
                                text = "💡 Pista: $hint",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 14.sp,
                                color = Color(0xFFF57C00)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Input del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = userGuess,
                        onValueChange = { userGuess = it },
                        label = { Text("Tu adivinanza") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !gameWon,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF7B1FA2),
                            unfocusedBorderColor = Color(0xFF9C27B0)
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                val guess = userGuess.toIntOrNull()
                                if (guess != null && guess in 1..100) {
                                    attempts++
                                    when {
                                        guess == targetNumber -> {
                                            message = "¡Correcto!"
                                            gameWon = true
                                        }
                                        guess < targetNumber -> {
                                            message = "Muy bajo 📈"
                                            hint = "Piensa en un número más grande"
                                        }
                                        else -> {
                                            message = "Muy alto 📉"
                                            hint = "Piensa en un número más pequeño"
                                        }
                                    }
                                    userGuess = ""
                                } else {
                                    message = "Por favor ingresa un número entre 1 y 100"
                                }
                            },
                            enabled = userGuess.isNotEmpty() && !gameWon,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Adivinar")
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        OutlinedButton(
                            onClick = {
                                showHint = true
                                hintPenalty += 2
                                // Generar pista más específica
                                val range = when {
                                    targetNumber <= 25 -> "entre 1 y 25"
                                    targetNumber <= 50 -> "entre 26 y 50"
                                    targetNumber <= 75 -> "entre 51 y 75"
                                    else -> "entre 76 y 100"
                                }
                                hint = "El número está $range"
                            },
                            enabled = !showHint && !gameWon,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Pista (-2 pts)")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botón de nuevo juego
            if (gameWon) {
                Button(
                    onClick = {
                        targetNumber = (1..100).random()
                        userGuess = ""
                        attempts = 0
                        message = ""
                        gameWon = false
                        hint = ""
                        showHint = false
                        hintPenalty = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Nuevo Juego")
                }
            }
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = color
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}