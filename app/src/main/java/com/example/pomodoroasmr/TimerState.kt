package com.example.pomodoroasmr

sealed class TimerState {

    object Idle : TimerState()

    data class Running(
        val period: Period,
        val millisLeft: Long
    ) : TimerState()

    data class Paused(
        val period: Period,
        val millisLeft: Long
    ) : TimerState()

    sealed class Period {
        object Work : Period()
        object ShortBreak : Period()
        object LongBreak : Period()
    }
}