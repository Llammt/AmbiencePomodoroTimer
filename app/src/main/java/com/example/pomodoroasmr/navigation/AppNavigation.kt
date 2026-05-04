package com.example.pomodoroasmr.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pomodoroasmr.timer.TimerViewModel
import com.example.pomodoroasmr.data.AppDatabase
import com.example.pomodoroasmr.data.SessionRepository
import com.example.pomodoroasmr.screens.NewSessionScreen
import com.example.pomodoroasmr.screens.PlaySessionScreen
import com.example.pomodoroasmr.screens.SelectSessionScreen
import com.example.pomodoroasmr.screens.SplashScreen
import com.example.pomodoroasmr.screens.StatsScreen
import com.example.pomodoroasmr.timer.TimerViewModelFactory

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
            val factory = TimerViewModelFactory(
                SessionRepository(AppDatabase.getInstance(LocalContext.current).sessionDao())
            )
            val viewModel: TimerViewModel = viewModel(factory = factory)
            PlaySessionScreen(viewModel, navController)
        }

        composable(Routes.Statistics.route) {
            StatsScreen()
        }
    }
}
