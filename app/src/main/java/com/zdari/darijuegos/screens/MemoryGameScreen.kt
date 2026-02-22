package com.zdari.darijuegos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameScreen(navController: NavController) {
    var cards by remember { mutableStateOf(generateCards()) }
    var flippedCards by remember { mutableStateOf<List<Int>>(emptyList()) }
    var matchedPairs by remember { mutableStateOf(0) }
    var attempts by remember { mutableStateOf(0) }
    var gameWon by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var showHint by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf("") }
    var hintPenalty by remember { mutableStateOf(0) }
    
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "🧠 Juego de Memoria",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF388E3C)
                ),
                navigationIcon = {
                    TextButton(onClick = { navController.navigateUp() }) {
                        Text("<-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        cards = generateCards()
                        flippedCards = emptyList()
                        matchedPairs = 0
                        attempts = 0
                        gameWon = false
                        isProcessing = false
                        score = 0
                        showHint = false
                        hint = ""
                        hintPenalty = 0
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
                        colors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Panel de información
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
                    InfoCard2("Parejas", "$matchedPairs/8", Color(0xFF388E3C))
                    InfoCard2("Intentos", attempts.toString(), Color(0xFF1976D2))
                    InfoCard2("Puntos", score.toString(), Color(0xFFFF9800))
                    InfoCard2("Penal", hintPenalty.toString(), Color(0xFFF44336))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mensaje de estado
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        gameWon -> Color(0xFFE8F5E8)
                        showHint -> Color(0xFFFFF3E0)
                        else -> Color.White.copy(alpha = 0.9f)
                    }
                ),
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
                            text = "Puntuación final: ${score - hintPenalty} puntos",
                            fontSize = 14.sp,
                            color = Color(0xFF388E3C)
                        )
                    } else {
                        Text(
                            text = when {
                                isProcessing -> "Procesando..."
                                flippedCards.size == 1 -> "¡Busca la pareja!"
                                else -> "¡Encuentra todas las parejas!"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF388E3C)
                        )
                    }
                    
                    if (showHint && !gameWon) {
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
            
            // Tablero de juego
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cards.size) { index ->
                        MemoryCard(
                            card = cards[index],
                            isFlipped = flippedCards.contains(index) || cards[index].isMatched,
                            canFlip = !isProcessing && flippedCards.size < 2 && !cards[index].isMatched,
                            onClick = {
                                if (!isProcessing && flippedCards.size < 2 && !cards[index].isMatched) {
                                    flippedCards = flippedCards + index
                                    
                                    if (flippedCards.size == 2) {
                                        attempts++
                                        isProcessing = true
                                        
                                        scope.launch {
                                            delay(1000)
                                            
                                            val firstCard = cards[flippedCards[0]]
                                            val secondCard = cards[flippedCards[1]]
                                            
                                            if (firstCard.content == secondCard.content) {
                                                cards = cards.mapIndexed { i, card ->
                                                    if (i == flippedCards[0] || i == flippedCards[1]) {
                                                        card.copy(isMatched = true)
                                                    } else card
                                                }
                                                matchedPairs++
                                                score += 10
                                                
                                                if (matchedPairs == 8) {
                                                    gameWon = true
                                                }
                                            }
                                            
                                            flippedCards = emptyList()
                                            isProcessing = false
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Controles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val unmatchedCards = cards.filter { !it.isMatched }.distinctBy { it.content }
                        if (unmatchedCards.isNotEmpty()) {
                            showHint = true
                            hintPenalty += 5
                            hint = "Faltan ${unmatchedCards.size} parejas"
                        }
                    },
                    enabled = !showHint && !gameWon,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Text("Pista (-5 pts)")
                }
                
                if (gameWon) {
                    Button(
                        onClick = {
                            cards = generateCards()
                            flippedCards = emptyList()
                            matchedPairs = 0
                            attempts = 0
                            gameWon = false
                            isProcessing = false
                            score = 0
                            showHint = false
                            hint = ""
                            hintPenalty = 0
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Nuevo Juego")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard2(title: String, value: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 11.sp, color = color)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun MemoryCard(
    card: MemoryCardData,
    isFlipped: Boolean,
    canFlip: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(80.dp)
            .scale(if (isFlipped && !card.isMatched) 1.1f else 1f),
        enabled = canFlip || isFlipped,
        colors = CardDefaults.cardColors(
            containerColor = if (isFlipped) Color(0xFFE3F2FD) else Color(0xFF388E3C)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFlipped) 8.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isFlipped) {
                Text(
                    text = card.content,
                    fontSize = 32.sp
                )
            } else {
                Text(
                    text = "?",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

data class MemoryCardData(
    val content: String,
    val isMatched: Boolean = false
)

fun generateCards(): List<MemoryCardData> {
    val emojis = listOf("🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼")
    return (emojis + emojis).shuffled().map { MemoryCardData(it) }
}
