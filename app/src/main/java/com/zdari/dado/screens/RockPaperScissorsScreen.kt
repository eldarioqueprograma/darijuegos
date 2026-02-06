package com.zdari.dado.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RockPaperScissorsScreen(navController: NavController) {
    var playerChoice by remember { mutableStateOf<String?>(null) }
    var computerChoice by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf("") }
    var playerScore by remember { mutableStateOf(0) }
    var computerScore by remember { mutableStateOf(0) }
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
                        playerScore = 0
                        computerScore = 0
                        playerChoice = null
                        computerChoice = null
                        result = ""
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reiniciar",
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
                    
                    Text(
                        text = "VS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                    
                    ScoreDisplay("CPU", computerScore, Color(0xFFF44336))
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
                    if (playerChoice != null && computerChoice != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ChoiceDisplay(getChoiceIcon(playerChoice!!), "Tú", isAnimating)
                            Text(
                                text = "VS",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2)
                            )
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
                        Text(
                            text = "¡Elige tu jugada!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1976D2)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Opciones de juego
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChoiceButton("✊", "Piedra", Color(0xFF8D6E63)) { playGame("piedra", { playerChoice = it }, { computerChoice = it }, { result = it }, { playerScore = it }, { computerScore = it }, { isAnimating = it }) }
                ChoiceButton("✋", "Papel", Color(0xFF42A5F5)) { playGame("papel", { playerChoice = it }, { computerChoice = it }, { result = it }, { playerScore = it }, { computerScore = it }, { isAnimating = it }) }
                ChoiceButton("✌️", "Tijera", Color(0xFFEC407A)) { playGame("tijera", { playerChoice = it }, { computerChoice = it }, { result = it }, { playerScore = it }, { computerScore = it }, { isAnimating = it }) }
            }
        }
    }
}

@Composable
fun ScoreDisplay(name: String, score: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
        Text(
            text = score.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun ChoiceDisplay(icon: String, label: String, isAnimating: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .size(80.dp)
                .scale(if (isAnimating) 1.1f else 1f),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 40.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF424242)
        )
    }
}

@Composable
fun ChoiceButton(icon: String, label: String, color: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(100.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 40.sp,
                color = Color.White
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
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

fun playGame(
    playerChoice: String,
    setPlayerChoice: (String) -> Unit,
    setComputerChoice: (String) -> Unit,
    setResult: (String) -> Unit,
    setPlayerScore: (Int) -> Unit,
    setComputerScore: (Int) -> Unit,
    setIsAnimating: (Boolean) -> Unit
) {
    setIsAnimating(true)
    
    // Simular delay para animación
    kotlinx.coroutines.runBlocking {
        kotlinx.coroutines.delay(500)
    }
    
    val choices = listOf("piedra", "papel", "tijera")
    val computerChoice = choices.random()
    
    setPlayerChoice(playerChoice)
    setComputerChoice(computerChoice)
    
    val result = when {
        playerChoice == computerChoice -> "¡Empate!"
        (playerChoice == "piedra" && computerChoice == "tijera") ||
        (playerChoice == "papel" && computerChoice == "piedra") ||
        (playerChoice == "tijera" && computerChoice == "papel") -> {
            "¡Ganaste! 🎉"
        }
        else -> "¡Perdiste! 😔"
    }
    
    setResult(result)
    
    // Actualizar puntuación
    when {
        result.contains("Ganaste") -> setPlayerScore(playerScore + 1)
        result.contains("Perdiste") -> setComputerScore(computerScore + 1)
    }
    
    setIsAnimating(false)
}