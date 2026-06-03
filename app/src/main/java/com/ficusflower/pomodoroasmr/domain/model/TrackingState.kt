package com.ficusflower.pomodoroasmr.domain.model

enum class AppMode { POMODORO, STOPWATCH, TIMER }

enum class TrackingStatus { IDLE, RUNNING, PAUSED }

data class TrackingState(
    val mode: AppMode = AppMode.POMODORO,
    val status: TrackingStatus = TrackingStatus.IDLE,
    val millisLeft: Long = 0L,
    val extraInfo: Any? = null
)
