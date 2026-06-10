package com.ficusflower.pomodoroasmr.infrastructure.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ficusflower.pomodoroasmr.features.pomodoro.TimerViewModel
import com.ficusflower.pomodoroasmr.features.main.MainMenuScreen
import com.ficusflower.pomodoroasmr.features.main.SplashScreen
import com.ficusflower.pomodoroasmr.features.pomodoro.PomodoroSessionScreen
import com.ficusflower.pomodoroasmr.features.pomodoro.PomodoroSettingsScreen
import com.ficusflower.pomodoroasmr.features.statistics.StatsScreen
import com.ficusflower.pomodoroasmr.features.statistics.StatsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val timerViewModel: TimerViewModel = koinViewModel()

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
            MainMenuScreen(navController = navController)
        }

        composable(Routes.NewSession.route) {
            PomodoroSettingsScreen(
                onStartSession = { config ->
                    timerViewModel.startTimer(config)
                    navController.navigate(Routes.PlaySession.route){
                        popUpTo(Routes.NewSession.route) { inclusive = true }
                    }
                }
            )
        }


        composable(Routes.PlaySession.route) {
            val viewModel: TimerViewModel = koinViewModel()
            PomodoroSessionScreen(navController, viewModel)
        }

        composable(Routes.Statistics.route) {
            val viewModel = koinViewModel<StatsViewModel>()
            StatsScreen(viewModel = viewModel)
        }
    }
}
