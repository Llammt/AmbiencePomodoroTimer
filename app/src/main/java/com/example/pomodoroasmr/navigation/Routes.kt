package com.example.pomodoroasmr.navigation

sealed class Routes(val route: String) {

    object StartScreen : Routes("start_screen")
    object SelectSession : Routes("select_session")

    object NewSession : Routes("new_session")

    object PlaySession : Routes("play_session")

    object Statistics : Routes("statistics")
}