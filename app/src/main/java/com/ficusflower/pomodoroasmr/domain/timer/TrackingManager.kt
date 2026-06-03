package com.ficusflower.pomodoroasmr.domain.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppMode { POMODORO, STOPWATCH, TIMER }

class TrackingManager(
    val pomodoroEngine: PomodoroEngine
) {
    private val _currentMode = MutableStateFlow(AppMode.POMODORO)
    val currentMode = _currentMode.asStateFlow()

    fun startCurrent() {
        when (_currentMode.value) {
            AppMode.POMODORO -> pomodoroEngine.start()
            AppMode.STOPWATCH -> {}
            AppMode.TIMER -> {}
        }
    }

    fun pauseCurrent() {
        when (_currentMode.value) {
            AppMode.POMODORO -> pomodoroEngine.pause()
            AppMode.STOPWATCH -> {}
            AppMode.TIMER -> {}
        }
    }

    fun stopCurrent() {
        when (_currentMode.value) {
            AppMode.POMODORO -> pomodoroEngine.stop()
            AppMode.STOPWATCH -> {}
            AppMode.TIMER -> {}
        }
    }
}
