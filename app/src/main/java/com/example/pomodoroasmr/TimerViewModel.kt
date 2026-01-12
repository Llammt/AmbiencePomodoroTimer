package com.example.pomodoroasmr

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import android.util.Log

class TimerViewModel : ViewModel() {
    private var timer: CountDownTimer? = null
    private var isWorkInterval = true
    private var cycleCount = 0

    private var isRunning = false
    private var isFirstStartOfPeriod = true

    private var currentPeriodDuration: Long = 0L
    private val workIntervalDuration = 1
    private val shortBreakDuration = 1
    private val longBreakDuration = 15

    private val oneSecond = 1000L
    private val oneMinute = 60 * oneSecond

    fun startTimer() {
        if (isFirstStartOfPeriod) {
            currentPeriodDuration = calculatePeriodDuration()
            isFirstStartOfPeriod = false
        }

        isRunning = true
        startCountDown()
    }

    private fun startCountDown() {
        timer = object : CountDownTimer(currentPeriodDuration, oneSecond) {

            override fun onTick(millisUntilFinished: Long) {
                currentPeriodDuration = millisUntilFinished

                //temp: UI
                val minutes = (millisUntilFinished / oneMinute)
                val seconds = (millisUntilFinished / oneSecond) % 60
                Log.d("Timer: ${ if (isWorkInterval) "Work" else "Chill"}", String.format("%02d:%02d", minutes, seconds))
            }

            override fun onFinish() {
                onPeriodFinished()
            }
        }.start()
    }

    private fun onPeriodFinished() {
        advanceToNextPeriod()
        startTimer()
    }

    fun stopTimer() {
        timer?.cancel()
        resetState()
    }

    fun pauseTimer() {
        timer?.cancel()
        isRunning = false
    }

    fun calculatePeriodDuration() : Long =
         when {
            isWorkInterval -> workIntervalDuration * oneMinute
            isLongBreak() -> longBreakDuration * oneMinute
            else -> shortBreakDuration * oneMinute
        }

    private fun advanceToNextPeriod() {
        if (isWorkInterval) cycleCount++
        isWorkInterval = !isWorkInterval
        isRunning = false
        isFirstStartOfPeriod = true
    }

    private fun resetState() {
        isWorkInterval = true
        cycleCount = 0
        isRunning = false
        isFirstStartOfPeriod = true
    }

    private fun isLongBreak(): Boolean =
        cycleCount % 4 == 0
}
