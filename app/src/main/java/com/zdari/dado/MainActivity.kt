package com.zdari.dado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zdari.dado.ui.theme.DadoTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DadoTheme {
                JuegoDeDadoMultijugador()
            }
        }
    }
}

@Composable
fun DadoTexto(numero: Int) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color.Cyan, shape = RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = numero.toString(),
            fontSize = 48.sp
        )
    }
}
@Composable
fun example(){
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Absolute.SpaceEvenly){
        Column(horizontalAlignment= Alignment.CenterHorizontally){
            DadoTexto(3)
        }
    }
}

@Composable
fun JuegoDeDadoMultijugador() {

    var turnoJugador1 by remember { mutableStateOf(true) }
    var dadoJugador1 by remember { mutableStateOf(0) }
    var dadoJugador2 by remember { mutableStateOf(0) }
    var puntajeJugador1 by remember { mutableStateOf(0) }
    var puntajeJugador2 by remember { mutableStateOf(0) }
    var resultadoRonda by remember { mutableStateOf("") }
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
    ) { Text("Turno de: ${if (turnoJugador1) "Jugador 1" else "Jugador 2"}", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Jugador 1")
            if (dadoJugador1 > 0) DadoTexto(numero = dadoJugador1)
            Text("Puntos: $puntajeJugador1")
        }
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Jugador 2")
            if (dadoJugador2 > 0) DadoTexto(numero = dadoJugador2)
            Text("Puntos: $puntajeJugador2")
        } }
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { val numero = (1..6).random()
            if (turnoJugador1) {
                dadoJugador1 = numero
            } else {
                dadoJugador2 = numero

// Comparar resultados
                resultadoRonda = when {
                    dadoJugador1 > dadoJugador2 -> {
                        puntajeJugador1 += 1
                        "¡Jugador 1 gana la ronda!"
                    }

                    dadoJugador2 > dadoJugador1 -> {
                        puntajeJugador2 += 1
                        "¡Jugador 2 gana la ronda!"
                    }

                    else -> "Empate"
                }
            }
            turnoJugador1 = !turnoJugador1
        }) { Text(if (turnoJugador1) "Lanzar dado (J1)" else "Lanzar dado (J2)")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { dadoJugador1 = 0
            dadoJugador2 = 0
            resultadoRonda = ""
            turnoJugador1 = true
            puntajeJugador1 = 0
            puntajeJugador2 = 0

        }) { Text("Reiniciar Juego")


        } }
}







