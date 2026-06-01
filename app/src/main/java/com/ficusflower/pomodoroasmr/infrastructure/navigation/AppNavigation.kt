package com.ficusflower.pomodoroasmr.infrastructure.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ficusflower.pomodoroasmr.domain.timer.TimerViewModel
import com.ficusflower.pomodoroasmr.data.database.AppDatabase
import com.ficusflower.pomodoroasmr.features.screens.NewSessionScreen
import com.ficusflower.pomodoroasmr.features.screens.PlaySessionScreen
import com.ficusflower.pomodoroasmr.features.screens.SelectSessionScreen
import com.ficusflower.pomodoroasmr.features.screens.SplashScreen
import com.ficusflower.pomodoroasmr.features.screens.StatsScreen
import com.ficusflower.pomodoroasmr.infrastructure.di.AppViewModelFactory
import com.ficusflower.pomodoroasmr.features.statistics.StatsViewModel
import org.koin.androidx.compose.koinViewModel

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
            val viewModel: TimerViewModel = koinViewModel()
            PlaySessionScreen(viewModel, navController)
        }

        composable(Routes.Statistics.route) {
            val viewModel = koinViewModel<StatsViewModel>()
            StatsScreen(viewModel = viewModel)
        }
    }
}
