package com.example.pomodoroasmr.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodoroasmr.screens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.StartScreen.route
    ) {
        composable(Routes.StartScreen.route) {
            SplashScreen(
                onAnimationFinished = {
                    navController.navigate(Routes.SelectSession.route) {
                        popUpTo(Routes.StartScreen.route) { inclusive = true }
                    }
                }
            )
        }

        // Пока заглушка для следующего экрана
        composable("select_session") {
            // Позже сюда поставим SelectSessionScreen
            SelectSessionPlaceholder()
        }
    }
}

@Composable
fun SelectSessionPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Select Session Screen (soon)")
    }
}