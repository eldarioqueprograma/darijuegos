package com.zdari.dado.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RockPaperScissorsScreen(navController: NavController) {
    var playerChoice by remember { mutableStateOf(Choice.NONE) }
    var computerChoice by remember { mutableStateOf(Choice.NONE) }
    var result by remember { mutableStateOf("") }
    var playerScore by remember { mutableStateOf(0) }
    var computerScore by remember { mutableStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "✊ Piedra, Papel o Tijera ✌️",
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
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Marcador
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
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
                        "VS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                    ScoreDisplay("CPU", computerScore, Color(0xFFF44336))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Elecciones mostradas
            if (playerChoice != Choice.NONE && computerChoice != Choice.NONE) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChoiceDisplay(playerChoice, "Tú", isAnimating)
                    Text(
                        "VS",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    ChoiceDisplay(computerChoice, "CPU", isAnimating)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resultado
            if (result.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            result.contains("¡Ganaste!") -> Color(0xFF4CAF50)
                            result.contains("¡Perdiste!") -> Color(0xFFF44336)
                            else -> Color(0xFFFF9800)
                        }.copy(alpha = 0.9f)
                    )
                ) {
                    Text(
                        text = result,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Opciones del jugador
            Text(
                "Elige tu jugada:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ChoiceButton(Choice.ROCK, isAnimating) { choice ->
                    playGame(choice, { playerChoice = it }, { computerChoice = it }, { result = it }, { playerScore = it }, { computerScore = it }, { isAnimating = it }, playerScore, computerScore)
                }
                ChoiceButton(Choice.PAPER, isAnimating) { choice ->
                    playGame(choice, { playerChoice = it }, { computerChoice = it }, { result = it }, { playerScore = it }, { computerScore = it }, { isAnimating = it }, playerScore, computerScore)
                }
                ChoiceButton(Choice.SCISSORS, isAnimating) { choice ->
                    playGame(choice, { playerChoice = it }, { computerChoice = it }, { result = it }, { playerScore = it }, { computerScore = it }, { isAnimating = it }, playerScore, computerScore)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón reiniciar
            Button(
                onClick = {
                    playerChoice = Choice.NONE
                    computerChoice = Choice.NONE
                    result = ""
                    playerScore = 0
                    computerScore = 0
                    isAnimating = false
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    "Reiniciar Partida",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ScoreDisplay(label: String, score: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Text(
            score.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun ChoiceDisplay(choice: Choice, label: String, isAnimating: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    choice.emoji,
                    fontSize = if (isAnimating) 40.sp else 50.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
fun ChoiceButton(choice: Choice, isAnimating: Boolean, onClick: (Choice) -> Unit) {
    ElevatedButton(
        onClick = { if (!isAnimating) onClick(choice) },
        modifier = Modifier.size(80.dp),
        shape = CircleShape,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF1976D2)
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        )
    ) {
        Text(
            choice.emoji,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

enum class Choice(val emoji: String) {
    ROCK("✊"),
    PAPER("✋"),
    SCISSORS("✌️"),
    NONE("❓")
}

fun getWinner(player: Choice, computer: Choice): String {
    return when {
        player == computer -> "¡Empate!"
        (player == Choice.ROCK && computer == Choice.SCISSORS) ||
        (player == Choice.PAPER && computer == Choice.ROCK) ||
        (player == Choice.SCISSORS && computer == Choice.PAPER) -> "¡Ganaste!"
        else -> "¡Perdiste!"
    }
}

fun playGame(
    playerChoice: Choice,
    setPlayerChoice: (Choice) -> Unit,
    setComputerChoice: (Choice) -> Unit,
    setResult: (String) -> Unit,
    setPlayerScore: (Int) -> Unit,
    setComputerScore: (Int) -> Unit,
    setIsAnimating: (Boolean) -> Unit,
    currentPlayerScore: Int,
    currentComputerScore: Int
) {
    setIsAnimating(true)
    
    // Animación de "pensamiento"
    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
        val computerChoice = Choice.values().filter { it != Choice.NONE }.random()
        setPlayerChoice(playerChoice)
        setComputerChoice(computerChoice)
        
        val winner = getWinner(playerChoice, computerChoice)
        setResult(winner)
        
        // Actualizar puntuación
        when {
            winner.contains("¡Ganaste!") -> setPlayerScore(currentPlayerScore + 1)
            winner.contains("¡Perdiste!") -> setComputerScore(currentComputerScore + 1)
        }
        
        setIsAnimating(false)
    }, 1000) // 1 segundo de animación
}