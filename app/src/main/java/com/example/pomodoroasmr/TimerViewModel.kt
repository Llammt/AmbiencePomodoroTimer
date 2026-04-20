package com.example.pomodoroasmr

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerViewModel : ViewModel() {
    private var timer: CountDownTimer? = null
    private var cycleCount = 0

    private val oneSecond = 1000L

    private val _state = MutableStateFlow<TimerState>(TimerState.Idle)
    val state: StateFlow<TimerState> = _state.asStateFlow()

    fun startTimer() {
        val current = _state.value

        if (current is TimerState.Idle) {
            val period = TimerState.Period.Work
            val duration = period.durationMillis

            _state.value = TimerState.Running(
                period = period,
                millisLeft = duration
            )

            startCountDown(duration)

        } else if (current is TimerState.Paused) {
            _state.value = TimerState.Running(
                period = current.period,
                millisLeft = current.millisLeft
            )

            startCountDown(current.millisLeft)
        }
    }

    private fun startCountDown(duration : Long) {
        timer?.cancel()

        timer = object : CountDownTimer(duration, oneSecond) {

            override fun onTick(millisUntilFinished: Long) {
                val current = _state.value

                if (current is TimerState.Running) {
                    _state.value = current.copy(
                        millisLeft = millisUntilFinished
                    )
                }
            }

            override fun onFinish() {
                val current = _state.value
                if (current !is TimerState.Running) return

                onPeriodFinished(current.period)
            }
        }.start()
    }

    private fun onPeriodFinished(period: TimerState.Period) {
        if (period == TimerState.Period.Work) {
            cycleCount++
        }

        val next = nextPeriod(period)

        _state.value = TimerState.Running(
            period = next,
            millisLeft = next.durationMillis
        )

        startCountDown(next.durationMillis)
    }

    fun stopTimer() {
        timer?.cancel()
        resetState()
    }

    fun pauseTimer() {
        val current = _state.value

        if (current is TimerState.Running) {
            timer?.cancel()

            _state.value = TimerState.Paused(
                period = current.period,
                millisLeft = current.millisLeft
            )
        }
    }

    fun nextPeriod(period: TimerState.Period): TimerState.Period =
        when (period) {
            TimerState.Period.Work ->
                if ((cycleCount + 1) % 4 == 0)
                    TimerState.Period.LongBreak
                else
                    TimerState.Period.ShortBreak

            TimerState.Period.ShortBreak,
            TimerState.Period.LongBreak ->
                TimerState.Period.Work
        }

    private fun resetState() {
        _state.value = TimerState.Idle
    }

    override fun onCleared() {
        timer?.cancel()
        timer = null
        super.onCleared()
    }

}
