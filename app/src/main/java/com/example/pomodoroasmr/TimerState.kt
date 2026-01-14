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

    sealed class Period(
        val durationMillis: Long,
        val label: String
    ) {
        companion object {
            const val ONE_MINUTE = 60_000L
        }
        object Work : Period(25 * ONE_MINUTE, "Work")
        object ShortBreak : Period(5 * ONE_MINUTE, "Short Break")
        object LongBreak : Period(15 * ONE_MINUTE, "Long Break")
    }
}