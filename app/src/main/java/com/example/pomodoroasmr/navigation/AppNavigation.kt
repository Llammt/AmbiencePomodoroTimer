package com.example.pomodoroasmr.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodoroasmr.screens.SelectSessionScreen
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

        composable(Routes.SelectSession.route) {
            SelectSessionScreen(navController = navController)
        }

        composable(Routes.NewSession.route) {
            NewSessionScreen()
        }

        composable(Routes.PlaySession.route) {
            PlaySessionScreen()
        }

        composable(Routes.Statistics.route) {
            StatisticsScreen()
        }
    }
}

@Composable
fun NewSessionScreen() {
    Text("New Session Screen (TODO)")
}

@Composable
fun PlaySessionScreen() {
    Text("Play Session Screen (TODO)")
}

@Composable
fun StatisticsScreen() {
    Text("Statistics Screen (TODO)")
}
