package com.zdari.darijuegos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zdari.darijuegos.navigation.AppScript
import com.zdari.darijuegos.utils.UpdateManager

@Composable
fun MainMenuScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val updateManager = remember { UpdateManager(context) }

    // Sistema de actualización automática
    LaunchedEffect(Unit) {
        val downloadUrl = updateManager.checkForUpdates("1.0")
        if (downloadUrl != null) {
            updateManager.downloadAndInstall(downloadUrl)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF6200EE), Color(0xFF3700B3))
                )
            )
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "🎮 Mis Juegos 🎮",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        GameCard(
            title = "🎲 Juego del Dado",
            description = "Lanza los dados y compite contra un amigo.",
            buttonText = "Jugar Dados",
            color = Color(0xFF6200EE),
            onClick = { navController.navigate(AppScript.DiceGame.route) }
        )
        
        GameCard(
            title = "🧠 Juego de Memoria",
            description = "Encuentra todas las parejas de emojis.",
            buttonText = "Entrenar Mente",
            color = Color(0xff4b7200),
            onClick = { navController.navigate(AppScript.MemoryGame.route) }
        )
        
        GameCard(
            title = "🔢 Adivina el Número",
            description = "Intenta adivinar el número secreto del 1 al 100.",
            buttonText = "Adivinar",
            color = Color(0xFF7B1FA2),
            onClick = { navController.navigate(AppScript.GuessNumberGame.route) }
        )

        GameCard(
            title = "✊✋✌ Piedra, Papel o Tijeras",
            description = "Gánale a tu teléfono en piedra, papel o tijeras.",
            buttonText = "¡A jugar!",
            color = Color(0xffFFAF9E),
            onClick = { navController.navigate(AppScript.RockPaperScissorsGame.route) }
        )

        GameCard(
            title = "🧠 Tres en Raya",
            description = "Juega al Tres en Raya contra la IA.",
            buttonText = "¿Listo?",
            color = Color(0xff9920F5),
            onClick = { navController.navigate(AppScript.TicTacToeGame.route) }
        )

        GameCard(
            title = "🧠 Reflejos",
            description = "Veamos qué tan rápido eres.",
            buttonText = "¡Allá vamos!",
            color = Color(0xffF520BC),
            onClick = { navController.navigate(AppScript.ReflexGame.route) }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun GameCard(
    title: String,
    description: String,
    buttonText: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = color)
            ) {
                Text(buttonText, color = Color.White)
            }
        }
    }
}
