package com.zdari.darijuegos.screens

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
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeScreen(navController: NavController) {
    var board by remember { mutableStateOf(Array(3) { arrayOfNulls<String>(3) }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var isGameOver by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf<String?>(null) }
    var scoreX by remember { mutableIntStateOf(0) }
    var scoreO by remember { mutableIntStateOf(0) }
    var isVsComputer by remember { mutableStateOf(false) }
    var computerDifficulty by remember { mutableStateOf("Medio") }
    var showSettings by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    var hintText by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text("⭕ Tres en Raya", fontWeight = FontWeight.Bold, color = Color.White)
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
                    Row {
                        TextButton(onClick = { showSettings = true }) {
                            Text(if (isVsComputer) "🤖" else "👥", fontSize = 20.sp)
                        }
                        TextButton(onClick = { 
                            board = Array(3) { arrayOfNulls<String>(3) }
                            currentPlayer = "X"
                            isGameOver = false
                            winner = null
                            showHint = false
                        }) {
                            Text("🔄", fontSize = 20.sp)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(Color(0xFF2196F3), Color(0xFF64B5F6))))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Marcador
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ScoreItem("X", scoreX, Color(0xFF1976D2), currentPlayer == "X" && !isGameOver)
                    ScoreItem("O", scoreO, Color(0xFFD32F2F), currentPlayer == "O" && !isGameOver)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Modo", fontSize = 10.sp, color = Color.Gray)
                        Text(if (isVsComputer) "CPU ($computerDifficulty)" else "2 Jug.", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Estado
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when {
                            isGameOver && winner != null -> "🎉 ¡$winner Gana! 🎉"
                            isGameOver && winner == null -> "🤝 ¡Empate! 🤝"
                            isVsComputer && currentPlayer == "O" -> "💭 CPU pensando..."
                            else -> "Turno de $currentPlayer"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isGameOver && winner != null) Color(0xFF4CAF50) else Color(0xFF1976D2)
                    )
                    
                    if (showHint && !isGameOver) {
                        Text("💡 $hintText", color = Color(0xFFF57C00), modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Tablero
            Box(modifier = Modifier.size(300.dp).background(Color.White, RoundedCornerShape(16.dp)).padding(8.dp)) {
                Column {
                    for (i in 0..2) {
                        Row(modifier = Modifier.weight(1f)) {
                            for (j in 0..2) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(4.dp)
                                        .background(
                                            if (isWinningCell(board, i, j, winner)) Color(0xFFC8E6C9) 
                                            else Color(0xFFE3F2FD), 
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable(enabled = !isGameOver && board[i][j] == null && (!isVsComputer || currentPlayer == "X")) {
                                            board[i][j] = currentPlayer
                                            checkWin(board) { winWinner, over ->
                                                winner = winWinner
                                                isGameOver = over
                                                if (over && winWinner == "X") scoreX++
                                                if (over && winWinner == "O") scoreO++
                                                
                                                if (!over) {
                                                    currentPlayer = "O"
                                                    if (isVsComputer) {
                                                        scope.launch {
                                                            delay(600)
                                                            val move = getCPUMove(board, computerDifficulty)
                                                            if (move != null) {
                                                                board[move.first][move.second] = "O"
                                                                checkWin(board) { w2, o2 ->
                                                                    winner = w2
                                                                    isGameOver = o2
                                                                    if (o2 && w2 == "O") scoreO++
                                                                    currentPlayer = "X"
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        currentPlayer = "O"
                                                    }
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = board[i][j] ?: "",
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (board[i][j] == "X") Color(0xFF1976D2) else Color(0xFFD32F2F)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))

            if (!isGameOver) {
                Button(onClick = { 
                    hintText = generateHintText(board, currentPlayer)
                    showHint = true 
                }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))) {
                    Text("💡 Pista")
                }
            } else {
                Button(onClick = {
                    board = Array(3) { arrayOfNulls<String>(3) }
                    currentPlayer = "X"
                    isGameOver = false
                    winner = null
                    showHint = false
                }) {
                    Text("NUEVA PARTIDA")
                }
            }
        }
    }

    if (showSettings) {
        AlertDialog(
            onDismissRequest = { showSettings = false },
            title = { Text("Configuración") },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Contra CPU:")
                        Checkbox(checked = isVsComputer, onCheckedChange = { isVsComputer = it })
                    }
                    if (isVsComputer) {
                        Text("Dificultad:")
                        Row {
                            listOf("Fácil", "Medio", "Difícil").forEach {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { computerDifficulty = it }) {
                                    RadioButton(selected = computerDifficulty == it, onClick = { computerDifficulty = it })
                                    Text(it, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showSettings = false }) { Text("OK") } }
        )
    }
}

@Composable
fun ScoreItem(label: String, score: Int, color: Color, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.scale(if (isActive) 1.1f else 1f)) {
        Text(label, color = color, fontWeight = FontWeight.Bold)
        Text(score.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

fun checkWin(board: Array<Array<String?>>, onResult: (String?, Boolean) -> Unit) {
    val winLines = listOf(
        listOf(0 to 0, 0 to 1, 0 to 2), listOf(1 to 0, 1 to 1, 1 to 2), listOf(2 to 0, 2 to 1, 2 to 2),
        listOf(0 to 0, 1 to 0, 2 to 0), listOf(0 to 1, 1 to 1, 2 to 1), listOf(0 to 2, 1 to 2, 2 to 2),
        listOf(0 to 0, 1 to 1, 2 to 2), listOf(0 to 2, 1 to 1, 2 to 0)
    )
    for (line in winLines) {
        val a = board[line[0].first][line[0].second]
        val b = board[line[1].first][line[1].second]
        val c = board[line[2].first][line[2].second]
        if (a != null && a == b && b == c) {
            onResult(a, true)
            return
        }
    }
    if (board.all { row -> row.all { it != null } }) onResult(null, true)
    else onResult(null, false)
}

fun isWinningCell(board: Array<Array<String?>>, r: Int, c: Int, winner: String?): Boolean {
    if (winner == null) return false
    val lines = listOf(
        listOf(0 to 0, 0 to 1, 0 to 2), listOf(1 to 0, 1 to 1, 1 to 2), listOf(2 to 0, 2 to 1, 2 to 2),
        listOf(0 to 0, 1 to 0, 2 to 0), listOf(0 to 1, 1 to 1, 2 to 1), listOf(0 to 2, 1 to 2, 2 to 2),
        listOf(0 to 0, 1 to 1, 2 to 2), listOf(0 to 2, 1 to 1, 2 to 0)
    )
    for (line in lines) {
        if (board[line[0].first][line[0].second] == winner && 
            board[line[1].first][line[1].second] == winner && 
            board[line[2].first][line[2].second] == winner) {
            if (line.contains(r to c)) return true
        }
    }
    return false
}

fun getCPUMove(board: Array<Array<String?>>, diff: String): Pair<Int, Int>? {
    val empty = mutableListOf<Pair<Int, Int>>()
    for (i in 0..2) for (j in 0..2) if (board[i][j] == null) empty.add(i to j)
    if (empty.isEmpty()) return null
    return if (diff == "Fácil") empty.random() else empty.random() // Simplificado para demo
}

fun generateHintText(board: Array<Array<String?>>, p: String): String {
    if (board[1][1] == null) return "El centro es clave"
    return "Busca una esquina"
}
