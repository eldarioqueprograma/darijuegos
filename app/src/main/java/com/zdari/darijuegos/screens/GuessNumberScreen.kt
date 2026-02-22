package com.zdari.darijuegos.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessNumberScreen(navController: NavController) {
    var targetNumber by remember { mutableIntStateOf((1..100).random()) }
    var userGuess by remember { mutableStateOf("") }
    var attempts by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var gameWon by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf("") }
    var showHint by remember { mutableStateOf(false) }
    var hintPenalty by remember { mutableIntStateOf(0) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("🔢 Adivina el Número", fontWeight = FontWeight.Bold, color = Color.White)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF7B1FA2)
                ),
                navigationIcon = {
                    // Usamos un botón de texto simple en lugar de Iconos para evitar errores de librerías
                    TextButton(onClick = { navController.navigateUp() }) {
                        Text("<-", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        targetNumber = (1..100).random()
                        userGuess = ""; attempts = 0; message = ""; gameWon = false; hint = ""; showHint = false; hintPenalty = 0
                    }) {
                        Text("🔄", fontSize = 20.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(Color(0xFF9C27B0), Color(0xFFE1BEE7))))
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
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoCard2("Intentos", attempts.toString(), Color(0xFF7B1FA2))
                    InfoCard2("Penalización", hintPenalty.toString(), Color(0xFFF44336))
                }
            }
            
            // Pantalla de mensajes
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (gameWon) "🎉 ¡ADIVINASTE! 🎉" else if (message.isEmpty()) "¡Introduce un número!" else message,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (gameWon) Color(0xFF4CAF50) else Color(0xFF7B1FA2),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    if (showHint) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "💡 Pista: $hint", color = Color(0xFFF57C00), fontWeight = FontWeight.Medium)
                    }
                }
            }
            
            // Input y Botones
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = userGuess,
                    onValueChange = { if (it.length <= 3) userGuess = it },
                    label = { Text("Número (1-100)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !gameWon,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF7B1FA2))
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = {
                        val guess = userGuess.toIntOrNull()
                        if (guess != null && guess in 1..100) {
                            attempts++
                            if (guess == targetNumber) {
                                message = "¡Correcto! Era el $targetNumber"
                                gameWon = true
                            } else {
                                message = if (guess < targetNumber) "Muy bajo 📈" else "Muy alto 📉"
                                hint = if (guess < targetNumber) "Más grande que $guess" else "Más pequeño que $guess"
                            }
                            userGuess = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = userGuess.isNotEmpty() && !gameWon,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2))
                ) {
                    Text("PROBAR SUERTE")
                }

                if (!gameWon && !showHint) {
                    TextButton(
                        onClick = { showHint = true; hintPenalty += 5 },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Ver pista (-5 pts)", color = Color.White)
                    }
                }

                if (gameWon) {
                    Button(
                        onClick = { 
                            targetNumber = (1..100).random(); userGuess = ""; attempts = 0; message = ""; gameWon = false; showHint = false; hintPenalty = 0 
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("NUEVO JUEGO")
                    }
                }
            }
        }
    }
}


