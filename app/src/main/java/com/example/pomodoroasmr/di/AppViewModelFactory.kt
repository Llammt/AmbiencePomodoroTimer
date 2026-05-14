package com.example.pomodoroasmr.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoroasmr.data.SessionRepository
import com.example.pomodoroasmr.statistics.StatsViewModel
import com.example.pomodoroasmr.timer.TimerViewModel

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