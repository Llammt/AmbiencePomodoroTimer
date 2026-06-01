package com.ficusflower.pomodoroasmr.infrastructure.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ficusflower.pomodoroasmr.domain.repository.SessionRepository
import com.ficusflower.pomodoroasmr.features.statistics.StatsViewModel
import com.ficusflower.pomodoroasmr.domain.timer.TimerViewModel

class AppViewModelFactory(
    private val repository: SessionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TimerViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TimerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StatsViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                StatsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}