package com.zdari.darijuegos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.draw.scale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RockPaperScissorsScreen(navController: NavController) {
    var playerChoice by remember { mutableStateOf<String?>(null) }
    var computerChoice by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf("") }
    var playerScore by remember { mutableIntStateOf(0) }
    var computerScore by remember { mutableIntStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "✊✋✌️ Piedra, Papel o Tijera",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1976D2)
                ),
                navigationIcon = {
                    TextButton(onClick = { navController.navigateUp() }) {
                        Text("<-", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        playerScore = 0
                        computerScore = 0
                        playerChoice = null
                        computerChoice = null
                        result = ""
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF42A5F5), Color(0xFF90CAF9))
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Marcador
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScoreDisplay("Tú", playerScore, Color(0xFF4CAF50))
                    Text("VS", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                    ScoreDisplay("Robot", computerScore, Color(0xFFF44336))
                }
            }
            
            // Área de resultados
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (playerChoice != null && computerChoice != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ChoiceDisplay(getChoiceIcon(playerChoice!!), "Tú", isAnimating)
                            Text("VS", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            ChoiceDisplay(getChoiceIcon(computerChoice!!), "CPU", isAnimating)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = result,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                result.contains("Ganaste") -> Color(0xFF4CAF50)
                                result.contains("Perdiste") -> Color(0xFFF44336)
                                else -> Color(0xFFFF9800)
                            }
                        )
                    } else {
                        Text("¡Elige tu jugada!", fontSize = 20.sp, color = Color(0xFF1976D2))
                    }
                }
            }
            
            // Opciones de juego
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChoiceButton("✊", "Piedra", Color(0xFF8D6E63)) { 
                    val (res, pChoice, cChoice) = resolveGame("piedra")
                    playerChoice = pChoice
                    computerChoice = cChoice
                    result = res
                    if (res.contains("Ganaste")) playerScore++
                    if (res.contains("Perdiste")) computerScore++
                }
                ChoiceButton("✋", "Papel", Color(0xFF42A5F5)) { 
                    val (res, pChoice, cChoice) = resolveGame("papel")
                    playerChoice = pChoice
                    computerChoice = cChoice
                    result = res
                    if (res.contains("Ganaste")) playerScore++
                    if (res.contains("Perdiste")) computerScore++
                }
                ChoiceButton("✌️", "Tijera", Color(0xFFEC407A)) { 
                    val (res, pChoice, cChoice) = resolveGame("tijera")
                    playerChoice = pChoice
                    computerChoice = cChoice
                    result = res
                    if (res.contains("Ganaste")) playerScore++
                    if (res.contains("Perdiste")) computerScore++
                }
            }
        }
    }
}

@Composable
fun ScoreDisplay(name: String, score: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = name, fontSize = 18.sp, color = color)
        Text(text = score.toString(), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun ChoiceDisplay(icon: String, label: String, isAnimating: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = icon, fontSize = 40.sp)
            }
        }
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun ChoiceButton(icon: String, label: String, color: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(90.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = icon, fontSize = 32.sp, color = Color.White)
            Text(text = label, fontSize = 10.sp, color = Color.White)
        }
    }
}

fun getChoiceIcon(choice: String): String {
    return when (choice) {
        "piedra" -> "✊"
        "papel" -> "✋"
        "tijera" -> "✌️"
        else -> "❓"
    }
}

fun resolveGame(playerChoice: String): Triple<String, String, String> {
    val choices = listOf("piedra", "papel", "tijera")
    val computerChoice = choices.random()
    
    val result = when {
        playerChoice == computerChoice -> "¡Empate!"
        (playerChoice == "piedra" && computerChoice == "tijera") ||
        (playerChoice == "papel" && computerChoice == "piedra") ||
        (playerChoice == "tijera" && computerChoice == "papel") -> "¡Ganaste! 🎉"
        else -> "¡Perdiste! 😔"
    }
    return Triple(result, playerChoice, computerChoice)
}
