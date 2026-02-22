package com.zdari.darijuegos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zdari.darijuegos.screens.DiceGameScreen
import com.zdari.darijuegos.screens.MainMenuScreen
import com.zdari.darijuegos.screens.MemoryGameScreen
import com.zdari.darijuegos.screens.GuessNumberScreen
import com.zdari.darijuegos.screens.RockPaperScissorsScreen
import com.zdari.darijuegos.screens.TicTacToeScreen
import com.zdari.darijuegos.screens.ReflexGameScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = AppScript.MainMenu.route) {
        composable(AppScript.MainMenu.route) {
            MainMenuScreen(navController)
        }
        composable(AppScript.DiceGame.route) {
            DiceGameScreen(navController)
        }
        composable(AppScript.MemoryGame.route) {
            MemoryGameScreen(navController)
        }
        composable(AppScript.GuessNumberGame.route) {
            GuessNumberScreen(navController)
        }
        composable(AppScript.RockPaperScissorsGame.route) {
            RockPaperScissorsScreen(navController)
        }
        composable(AppScript.TicTacToeGame.route) {
            TicTacToeScreen(navController)
        }
        composable(AppScript.ReflexGame.route) {
            ReflexGameScreen(navController)
        }
    }
}
