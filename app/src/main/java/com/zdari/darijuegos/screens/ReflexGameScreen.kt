package com.zdari.darijuegos.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    var gameState by remember { mutableStateOf("waiting") }
    var score by remember { mutableIntStateOf(0) }
    var bestScore by remember { mutableIntStateOf(0) }
    var startTime by remember { mutableLongStateOf(0L) }
    var reactionTime by remember { mutableLongStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()
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
                title = { Text("⚡ Reflejos", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF6200EE)),
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("<-", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(backgroundColor, backgroundColor.copy(alpha = 0.7f)))).padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (gameState) {
                            "waiting" -> "Espera al VERDE..."
                            "ready" -> "¡YA! ¡CLICK!"
                            "clicked" -> "¡Muy bien!"
                            "tooEarly" -> "¡Muy pronto!"
                            else -> ""
                        },
                        fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black
                    )
                    if (reactionTime > 0) {
                        Text(text = "${reactionTime}ms", fontSize = 20.sp, color = Color(0xFF388E3C))
                    }
                }
            }
            
            Box(
                modifier = Modifier.size(200.dp).background(if (gameState == "ready") Color.Green else Color.Gray, RoundedCornerShape(100.dp))
                    .clickable {
                        if (gameState == "ready") {
                            reactionTime = System.currentTimeMillis() - startTime
                            score++
                            if (score > bestScore) bestScore = score
                            gameState = "clicked"
                            job?.cancel()
                            coroutineScope.launch {
                                delay(1500)
                                gameState = "waiting"
                                job = launch { delay(Random.nextLong(1000, 4000)); startTime = System.currentTimeMillis(); gameState = "ready" }
                            }
                        } else if (gameState == "waiting") {
                            gameState = "tooEarly"; score = 0; job?.cancel()
                            coroutineScope.launch { delay(2000); gameState = "waiting" }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(if (gameState == "ready") "¡AHORA!" else "...", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ScoreCardReflex("Puntos", score.toString(), Color(0xFF4CAF50))
                ScoreCardReflex("Mejor", bestScore.toString(), Color(0xFF2196F3))
            }
        }
    }

    LaunchedEffect(Unit) {
        job = coroutineScope.launch { delay(Random.nextLong(1000, 4000)); startTime = System.currentTimeMillis(); gameState = "ready" }
    }
}

@Composable
fun ScoreCardReflex(title: String, value: String, color: Color) {
    Card(modifier = Modifier.size(100.dp), colors = CardDefaults.cardColors(containerColor = color)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 12.sp, color = Color.White)
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
