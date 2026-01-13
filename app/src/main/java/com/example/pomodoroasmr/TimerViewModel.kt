package com.example.pomodoroasmr

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData

class TimerViewModel : ViewModel() {
    private var timer: CountDownTimer? = null
    private var isWorkInterval = true
    private var cycleCount = 0
    private var isFirstStartOfPeriod = true

    private var currentPeriodDuration: Long = 0L
    private val workIntervalDuration = 1
    private val shortBreakDuration = 1
    private val longBreakDuration = 15

    private val oneSecond = 1000L
    private val oneMinute = 60 * oneSecond

    val UIminutes = MutableLiveData<Long>()
    val UIseconds = MutableLiveData<Long>()
    val UIPeriodTextLabel = MutableLiveData<String>()

    private val _state = MutableLiveData<TimerState>(TimerState.Idle)
    val state: MutableLiveData<TimerState> = _state

    fun startTimer() {
        if (isFirstStartOfPeriod) {
            currentPeriodDuration = calculatePeriodDuration()
            UIPeriodTextLabel.value = updatePeriodTextLabel()
            isFirstStartOfPeriod = false
        }

        startCountDown()
    }

    private fun startCountDown() {
        timer = object : CountDownTimer(currentPeriodDuration, oneSecond) {

            override fun onTick(millisUntilFinished: Long) {
                updateUI(millisUntilFinished)
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
    }

    fun calculatePeriodDuration() : Long =
         when {
            isWorkInterval -> workIntervalDuration * oneMinute
            isLongBreak() -> longBreakDuration * oneMinute
            else -> shortBreakDuration * oneMinute
        }

    fun updatePeriodTextLabel() : String =
        when {
            isWorkInterval -> "Work Interval"
            isLongBreak() -> "Long Break"
            else -> "Short Break"
        }

    private fun advanceToNextPeriod() {
        if (isWorkInterval) cycleCount++
        isWorkInterval = !isWorkInterval
        isFirstStartOfPeriod = true
    }

    private fun resetState() {
        isWorkInterval = true
        cycleCount = 0
        isFirstStartOfPeriod = true
    }

    private fun isLongBreak(): Boolean =
        cycleCount % 4 == 0

    override fun onCleared() {
        timer?.cancel()
        timer = null
        super.onCleared()
    }

    private fun updateUI(timeUntilFinished : Long) {
        currentPeriodDuration = timeUntilFinished
        UIminutes.value = timeUntilFinished / oneMinute
        UIseconds.value = (timeUntilFinished / oneSecond) % 60
    }
}
