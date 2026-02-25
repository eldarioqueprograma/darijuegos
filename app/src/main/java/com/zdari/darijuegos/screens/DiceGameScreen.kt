package com.zdari.darijuegos.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zdari.darijuegos.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiceGameScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("🎲 Juego del Dado", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    TextButton(onClick = { navController.navigateUp() }) {
                        Text("<-", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
fun JuegoDeDadoMultijugador() {
    var turnoJugador1 by remember { mutableStateOf(true) }
    var dadoJugador1 by remember { mutableIntStateOf(0) }
    var dadoJugador2 by remember { mutableIntStateOf(0) }
    var puntajeJugador1 by remember { mutableIntStateOf(0) }
    var puntajeJugador2 by remember { mutableIntStateOf(0) }
    var resultadoRonda by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Turno de: ${if (turnoJugador1) "Jugador 1" else "Jugador 2"}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Jugador 1")
                DadoAnimacion(numero = dadoJugador1)
                Text("Puntos: $puntajeJugador1", fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Jugador 2")
                DadoAnimacion(numero = dadoJugador2)
                Text("Puntos: $puntajeJugador2", fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        if (resultadoRonda.isNotEmpty()) {
            Text(text = resultadoRonda, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
        }

        Button(
            onClick = {
                val numero = (1..6).random()
                if (turnoJugador1) {
                    dadoJugador1 = numero
                    resultadoRonda = ""
                } else {
                    dadoJugador2 = numero
                    resultadoRonda = when {
                        dadoJugador1 > dadoJugador2 -> { puntajeJugador1++; "¡Gana J1!" }
                        dadoJugador2 > dadoJugador1 -> { puntajeJugador2++; "¡Gana J2!" }
                        else -> "Empate"
                    }
                }
                turnoJugador1 = !turnoJugador1
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text(if (turnoJugador1) "Lanzar J1" else "Lanzar J2")
        }
    }
}

@Composable
fun DadoAnimacion(numero: Int) {
    if (numero == 0) {
        Box(modifier = Modifier.size(100.dp).background(Color.LightGray, RoundedCornerShape(16.dp)))
        return
    }

    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(numero) {
        scope.launch {
            rotation.snapTo(0f)
            rotation.animateTo(720f, animationSpec = tween(500, easing = LinearEasing))
        }
        scope.launch {
            scale.animateTo(1.3f, tween(250))
            scale.animateTo(1f, tween(250))
        }
    }

    val imagen = when (numero) {
        1 -> painterResource(R.drawable.uno)
        2 -> painterResource(R.drawable.dos)
        3 -> painterResource(R.drawable.tres)
        4 -> painterResource(R.drawable.cuatro)
        5 -> painterResource(R.drawable.cinco)
        6 -> painterResource(R.drawable.seis)
        else -> painterResource(R.drawable.uno)
    }

    Image(
        painter = imagen,
        contentDescription = "Dado $numero",
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer {
                rotationZ = rotation.value
                scaleX = scale.value
                scaleY = scale.value
            }
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    )
}
