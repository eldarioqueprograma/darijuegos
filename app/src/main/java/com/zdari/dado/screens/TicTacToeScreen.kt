package com.zdari.dado.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Computer
import androidx.compose.ui.draw.scale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeScreen(navController: NavController) {
    var board by remember { mutableStateOf(Array(3) { arrayOfNulls<String>(3) }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameStatus by remember { mutableStateOf("Turno de X") }
    var isGameOver by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf<String?>(null) }
    var scoreX by remember { mutableStateOf(0) }
    var scoreO by remember { mutableStateOf(0) }
    var isVsComputer by remember { mutableStateOf(false) }
    var computerDifficulty by remember { mutableStateOf("Medio") }
    var showSettings by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    var hintPenalty by remember { mutableStateOf(0) }
    
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "⭕ Juego de Tres en Raya",
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
                    Row {
                        IconButton(onClick = { showSettings = true }) {
                            Icon(
                                imageVector = if (isVsComputer) Icons.Default.Computer else Icons.Default.Person,
                                contentDescription = "Configuración",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { 
                            board = Array(3) { arrayOfNulls<String>(3) }
                            currentPlayer = "X"
                            gameStatus = "Turno de X"
                            isGameOver = false
                            winner = null
                        }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Nueva Partida",
                                tint = Color.White
                            )
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF2196F3), Color(0xFF64B5F6))
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
                    ScoreCard("Jugador X", scoreX.toString(), Color(0xFF1976D2), currentPlayer == "X" && !isGameOver)
                    ScoreCard("Jugador O", scoreO.toString(), Color(0xFFD32F2F), currentPlayer == "O" && !isGameOver)
                    InfoCard("Modo", if (isVsComputer) "VS CPU" else "2 Jugadores", Color(0xFF388E3C))
                    InfoCard("Dificultad", computerDifficulty, Color(0xFFFF9800))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Estado del juego
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        gameWon && winner != null -> Color(0xFFE8F5E8)
                        gameWon && winner == null -> Color(0xFFFFF3E0)
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
                    Text(
                        text = when {
                            gameWon && winner != null -> "🎉 ¡${winner} gana! 🎉"
                            gameWon && winner == null -> "🤝 ¡Empate! 🤝"
                            isVsComputer && currentPlayer == "O" -> "💭 CPU pensando..."
                            else -> "Turno de $currentPlayer"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = when {
                            gameWon && winner != null -> Color(0xFF4CAF50)
                            gameWon && winner == null -> Color(0xFFFF9800)
                            else -> Color(0xFF1976D2)
                        }
                    )
                    
                    if (showHint && !isGameOver) {
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
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Tablero de juego
            Card(
                modifier = Modifier.size(300.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..2) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (j in 0..2) {
                                TicTacToeCell(
                                    value = board[i][j],
                                    onClick = {
                                        if (!isGameOver && board[i][j] == null && 
                                            (!isVsComputer || currentPlayer == "X")) {
                                            makeMove(board, i, j, currentPlayer) { newBoard ->
                                                board = newBoard
                                                checkGameState(newBoard, currentPlayer) { status, won, theWinner ->
                                                    gameStatus = status
                                                    isGameOver = won
                                                    winner = theWinner
                                                    
                                                    if (won && theWinner != null) {
                                                        if (theWinner == "X") scoreX++ else scoreO++
                                                    } else if (won && theWinner == null) {
                                                        // Empate
                                                    }
                                                    
                                                    if (!won) {
                                                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                                                        
                                                        // Turno de la CPU
                                                        if (isVsComputer && currentPlayer == "O" && !won) {
                                                            scope.launch {
                                                                delay(1000) // Pausa para simular pensamiento
                                                                val cpuMove = getCPUMove(board, computerDifficulty)
                                                                if (cpuMove != null) {
                                                                    makeMove(board, cpuMove.first, cpuMove.second, "O") { newBoard2 ->
                                                                        board = newBoard2
                                                                        checkGameState(newBoard2, "O") { status2, won2, winner2 ->
                                                                            gameStatus = status2
                                                                            isGameOver = won2
                                                                            winner = winner2
                                                                            
                                                                            if (won2 && winner2 != null) {
                                                                                if (winner2 == "X") scoreX++ else scoreO++
                                                                            }
                                                                            
                                                                            if (!won2) {
                                                                                currentPlayer = "X"
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    isWinning = isWinningCell(board, i, j, winner),
                                    isClickable = !isGameOver && board[i][j] == null && 
                                                 (!isVsComputer || currentPlayer == "X")
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Controles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        showHint = true
                        hintPenalty += 3
                        val hint = generateHint(board, currentPlayer)
                    },
                    enabled = !showHint && !isGameOver,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Text("Pista (-3 pts)")
                }
                
                if (isGameOver) {
                    Button(
                        onClick = {
                            board = Array(3) { arrayOfNulls<String>(3) }
                            currentPlayer = "X"
                            gameStatus = "Turno de X"
                            isGameOver = false
                            winner = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Nueva Partida")
                    }
                }
            }
        }
    }
    
    // Diálogo de configuración
    if (showSettings) {
        AlertDialog(
            onDismissRequest = { showSettings = false },
            title = { Text("Configuración") },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Modo de juego:", modifier = Modifier.weight(1f))
                        Switch(
                            checked = isVsComputer,
                            onCheckedChange = { isVsComputer = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF1976D2),
                                checkedTrackColor = Color(0xFF64B5F6)
                            )
                        )
                        Text(if (isVsComputer) "VS CPU" else "2 Jugadores")
                    }
                    
                    if (isVsComputer) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Dificultad de la CPU:")
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = computerDifficulty == "Fácil",
                                onClick = { computerDifficulty = "Fácil" }
                            )
                            Text("Fácil", modifier = Modifier.clickable { computerDifficulty = "Fácil" })
                            Spacer(modifier = Modifier.width(16.dp))
                            RadioButton(
                                selected = computerDifficulty == "Medio",
                                onClick = { computerDifficulty = "Medio" }
                            )
                            Text("Medio", modifier = Modifier.clickable { computerDifficulty = "Medio" })
                            Spacer(modifier = Modifier.width(16.dp))
                            RadioButton(
                                selected = computerDifficulty == "Difícil",
                                onClick = { computerDifficulty = "Difícil" }
                            )
                            Text("Difícil", modifier = Modifier.clickable { computerDifficulty = "Difícil" })
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showSettings = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Composable
fun ScoreCard(title: String, value: String, color: Color, isActive: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) color.copy(alpha = 0.2f) else color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.scale(if (isActive) 1.05f else 1f)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = color
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                color = color
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
fun TicTacToeCell(
    value: String?,
    onClick: () -> Unit,
    isWinning: Boolean,
    isClickable: Boolean
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .size(90.dp)
            .scale(if (isWinning) 1.1f else 1f),
        enabled = isClickable,
        colors = CardDefaults.cardColors(
            containerColor = when {
                isWinning -> Color(0xFF4CAF50)
                value != null -> Color(0xFFE3F2FD)
                else -> Color(0xFFBBDEFB)
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isWinning) 8.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isWinning) Color.White else when (value) {
                        "X" -> Color(0xFF1976D2)
                        else -> Color(0xFFD32F2F)
                    }
                )
            }
        }
    }
}

fun makeMove(board: Array<Array<String?>>, row: Int, col: Int, player: String, onComplete: (Array<Array<String?>>) -> Unit) {
    if (board[row][col] == null) {
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = player
        onComplete(newBoard)
    }
}

fun checkGameState(board: Array<Array<String?>>, currentPlayer: String, onComplete: (String, Boolean, String?) -> Unit) {
    // Verificar filas
    for (i in 0..2) {
        if (board[i][0] != null && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
            onComplete("¡${board[i][0]} gana!", true, board[i][0])
            return
        }
    }
    
    // Verificar columnas
    for (j in 0..2) {
        if (board[0][j] != null && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
            onComplete("¡${board[0][j]} gana!", true, board[0][j])
            return
        }
    }
    
    // Verificar diagonales
    if (board[0][0] != null && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
        onComplete("¡${board[0][0]} gana!", true, board[0][0])
        return
    }
    
    if (board[0][2] != null && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
        onComplete("¡${board[0][2]} gana!", true, board[0][2])
        return
    }
    
    // Verificar empate
    if (board.all { row -> row.all { it != null } }) {
        onComplete("¡Empate!", true, null)
        return
    }
    
    // Juego continúa
    onComplete("Turno de $currentPlayer", false, null)
}

fun isWinningCell(board: Array<Array<String?>>, row: Int, col: Int, winner: String?): Boolean {
    if (winner == null) return false
    
    // Verificar si esta celda es parte de la línea ganadora
    val cellValue = board[row][col]
    if (cellValue != winner) return false
    
    // Verificar fila
    if (board[row][0] == winner && board[row][1] == winner && board[row][2] == winner) return true
    
    // Verificar columna
    if (board[0][col] == winner && board[1][col] == winner && board[2][col] == winner) return true
    
    // Verificar diagonales
    if (row == col && board[0][0] == winner && board[1][1] == winner && board[2][2] == winner) return true
    if (row + col == 2 && board[0][2] == winner && board[1][1] == winner && board[2][0] == winner) return true
    
    return false
}

fun getCPUMove(board: Array<Array<String?>>, difficulty: String): Pair<Int, Int>? {
    val availableMoves = mutableListOf<Pair<Int, Int>>()
    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == null) {
                availableMoves.add(i to j)
            }
        }
    }
    
    if (availableMoves.isEmpty()) return null
    
    return when (difficulty) {
        "Fácil" -> availableMoves.random()
        "Medio" -> {
            // Intentar ganar o bloquear
            val winningMove = findWinningMove(board, "O")
            if (winningMove != null) return winningMove
            
            val blockingMove = findWinningMove(board, "X")
            if (blockingMove != null) return blockingMove
            
            // Tomar el centro si está disponible
            if (board[1][1] == null) return 1 to 1
            
            // Tomar una esquina
            val corners = listOf(0 to 0, 0 to 2, 2 to 0, 2 to 2)
            val availableCorners = corners.filter { board[it.first][it.second] == null }
            if (availableCorners.isNotEmpty()) return availableCorners.random()
            
            availableMoves.random()
        }
        "Difícil" -> {
            // Implementar algoritmo minimax simplificado
            var bestMove: Pair<Int, Int>? = null
            var bestScore = Int.MIN_VALUE
            
            for (move in availableMoves) {
                val newBoard = board.map { it.copyOf() }.toTypedArray()
                newBoard[move.first][move.second] = "O"
                
                val score = minimax(newBoard, 0, false)
                if (score > bestScore) {
                    bestScore = score
                    bestMove = move
                }
            }
            
            bestMove ?: availableMoves.random()
        }
        else -> availableMoves.random()
    }
}

fun findWinningMove(board: Array<Array<String?>>, player: String): Pair<Int, Int>? {
    for (i in 0..2) {
        for (j in 0..2) {
            if (board[i][j] == null) {
                val newBoard = board.map { it.copyOf() }.toTypedArray()
                newBoard[i][j] = player
                
                var canWin = false
                checkGameState(newBoard, player) { _, won, winner ->
                    if (won && winner == player) canWin = true
                }
                
                if (canWin) return i to j
            }
        }
    }
    return null
}

fun minimax(board: Array<Array<String?>>, depth: Int, isMaximizing: Boolean): Int {
    var gameOver = false
    var winner: String? = null
    
    checkGameState(board, if (isMaximizing) "O" else "X") { _, over, win ->
        gameOver = over
        winner = win
    }
    
    if (gameOver) {
        return when (winner) {
            "O" -> 10 - depth
            "X" -> depth - 10
            else -> 0
        }
    }
    
    if (isMaximizing) {
        var maxScore = Int.MIN_VALUE
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == null) {
                    val newBoard = board.map { it.copyOf() }.toTypedArray()
                    newBoard[i][j] = "O"
                    val score = minimax(newBoard, depth + 1, false)
                    maxScore = maxOf(maxScore, score)
                }
            }
        }
        return maxScore
    } else {
        var minScore = Int.MAX_VALUE
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == null) {
                    val newBoard = board.map { it.copyOf() }.toTypedArray()
                    newBoard[i][j] = "X"
                    val score = minimax(newBoard, depth + 1, true)
                    minScore = minOf(minScore, score)
                }
            }
        }
        return minScore
    }
}

fun generateHint(board: Array<Array<String?>>, currentPlayer: String): String {
    // Buscar movimientos ganadores
    val winningMove = findWinningMove(board, currentPlayer)
    if (winningMove != null) {
        return "¡Puedes ganar en este turno!"
    }
    
    // Buscar movimientos para bloquear al oponente
    val opponent = if (currentPlayer == "X") "O" else "X"
    val blockingMove = findWinningMove(board, opponent)
    if (blockingMove != null) {
        return "¡Bloquea al oponente!"
    }
    
    // Sugerir tomar el centro
    if (board[1][1] == null) {
        return "El centro es una buena opción"
    }
    
    // Sugerir esquinas
    val corners = listOf(0 to 0, 0 to 2, 2 to 0, 2 to 2)
    val availableCorners = corners.filter { board[it.first][it.second] == null }
    if (availableCorners.isNotEmpty()) {
        return "Considera una esquina"
    }
    
    return "Cualquier movimiento válido es bueno"
}