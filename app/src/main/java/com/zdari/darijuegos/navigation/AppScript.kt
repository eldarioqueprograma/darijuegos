package com.zdari.darijuegos.navigation

sealed class AppScript(val route: String) {
    object MainMenu : AppScript("main_menu")
    object DiceGame : AppScript("dice_game")
    object MemoryGame : AppScript("memory_game")
    object GuessNumberGame : AppScript("guess_number")
    object RockPaperScissorsGame : AppScript("rock_paper_scissors")
    object TicTacToeGame : AppScript("tic_tac_toe")
    object ReflexGame : AppScript("reflex_game")
}
