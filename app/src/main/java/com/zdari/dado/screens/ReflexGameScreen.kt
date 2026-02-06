package com.zdari.dado.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import kotlinx.coroutines.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReflexGameScreen(navController: NavController) {
    var gameState by remember { mutableStateOf("waiting") } // waiting, ready, clicked, tooEarly
    var score by remember { mutableStateOf(0) }
    var bestScore by remember { mutableStateOf(0) }
    var startTime by remember { mutableStateOf(0L) }
    var reactionTime by remember { mutableStateOf(0L) }
    var currentDelay by remember { mutableStateOf(2000L) }
    var coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    
    val backgroundColor = when (gameState) {
        "waiting" -> Color(0xFF1976D2)
        "ready" -> Color(0xFF4CAF50)
        "clicked" -> Color(0xFF388E3C)
        "tooEarly" -> Color(0xFFD32F2F)
        else -> Color(0xFF1976D2)
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "⚡ Juego de Reflexos",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6200EE)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                        colors = listOf(backgroundColor, backgroundColor.copy(alpha = 0.7f))
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta de instrucciones
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (gameState) {
                            "waiting" -> "Espera a que el botón cambie a verde"
                            "ready" -> "¡Haz clic ahora!"
                            "clicked" -> "¡Bien hecho!"
                            "tooEarly" -> "¡Demasiado pronto!"
                            else -> ""
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    if (reactionTime > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tiempo: ${reactionTime}ms",
                            fontSize = 18.sp,
                            color = Color(0xFF388E3C)
                        )
                    }
                }
            }
            
            // Botón principal
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(100.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (gameState) {
                        "ready" -> Color(0xFF4CAF50)
                        "clicked" -> Color(0xFF388E3C)
                        "tooEarly" -> Color(0xFFD32F2F)
                        else -> Color(0xFF9E9E9E)
                    }
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (gameState == "ready") 16.dp else 4.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = gameState != "waiting") {
                            when (gameState) {
                                "ready" -> {
                                    reactionTime = System.currentTimeMillis() - startTime
                                    score++
                                    if (score > bestScore) bestScore = score
                                    gameState = "clicked"
                                    job?.cancel()
                                    
                                    // Preparar siguiente ronda
                                    coroutineScope.launch {
                                        delay(1500)
                                        gameState = "waiting"
                                        reactionTime = 0
                                        job = launch {
                                            delay(Random.nextLong(1000, 4000))
                                            startTime = System.currentTimeMillis()
                                            gameState = "ready"
                                        }
                                    }
                                }
                                "waiting" -> {
                                    gameState = "tooEarly"
                                    score = 0
                                    job?.cancel()
                                    
                                    coroutineScope.launch {
                                        delay(2000)
                                        gameState = "waiting"
                                        job = launch {
                                            delay(Random.nextLong(1000, 4000))
                                            startTime = System.currentTimeMillis()
                                            gameState = "ready"
                                        }
                                    }
                                }
                                else -> {}
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (gameState) {
                            "waiting" -> "ESPERA"
                            "ready" -> "¡CLICK!"
                            "clicked" -> "✓"
                            "tooEarly" -> "✗"
                            else -> ""
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
            
            // Puntuaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreCard(
                    title = "Puntuación",
                    value = score.toString(),
                    color = Color(0xFF4CAF50)
                )
                
                ScoreCard(
                    title = "Mejor",
                    value = bestScore.toString(),
                    color = Color(0xFF2196F3)
                )
                
                ScoreCard(
                    title = "Ronda",
                    value = score.toString(),
                    color = Color(0xFFFF9800)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botones de control
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        score = 0
                        reactionTime = 0
                        gameState = "waiting"
                        job?.cancel()
                        coroutineScope.launch {
                            delay(1000)
                            job = launch {
                                delay(Random.nextLong(1000, 4000))
                                startTime = System.currentTimeMillis()
                                gameState = "ready"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Reiniciar", color = Color.White)
                }
                
                Button(
                    onClick = {
                        gameState = "waiting"
                        score = 0
                        reactionTime = 0
                        job?.cancel()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9E9E9E))
                ) {
                    Text("Parar", color = Color.White)
                }
            }
        }
    }
    
    // Iniciar el juego automáticamente
    LaunchedEffect(Unit) {
        job = coroutineScope.launch {
            delay(Random.nextLong(1000, 4000))
            startTime = System.currentTimeMillis()
            gameState = "ready"
        }
    }
}

@Composable
fun ScoreCard(
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier.size(100.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}