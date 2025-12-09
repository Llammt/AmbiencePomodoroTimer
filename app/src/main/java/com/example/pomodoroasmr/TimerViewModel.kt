package com.example.pomodoroasmr

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import android.util.Log

class TimerViewModel : ViewModel() {
    private var timer: CountDownTimer? = null
    private var isWorkInterval = true // true — короткий обычный интервал, false — большой перерыв (после 2-х часов работы)
    private var cycleCount = 0        // количество завершённых рабочих интервалов

    fun startTimer() {
        val duration = when {
            isWorkInterval -> 25 * 60 * 1000L  // 25 минут
            cycleCount % 4 == 0 -> 15 * 60 * 1000L // длинный перерыв
            else -> 5 * 60 * 1000L // короткий перерыв
        }

        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                // Обновляем UI, например TextView
                Log.d("Timer: ", String.format("%02d:%02d", minutes, seconds))
            }

            override fun onFinish() {
                if (isWorkInterval) cycleCount++
                isWorkInterval = !isWorkInterval
                startTimer() // запускаем следующий интервал
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel() // остановка таймера
    }
}
