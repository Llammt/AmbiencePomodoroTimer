package com.example.pomodoroasmr.navigation

sealed class Routes(val route: String) {

    object StartScreen : Routes("start_screen")
    object SelectSession : Routes("select_session")
}