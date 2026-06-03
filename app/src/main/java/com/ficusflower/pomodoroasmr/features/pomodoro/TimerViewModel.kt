package com.ficusflower.pomodoroasmr.features.pomodoro

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEffect
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngine
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngineState
import com.ficusflower.pomodoroasmr.infrastructure.audio.AudioPlayer
import com.ficusflower.pomodoroasmr.infrastructure.service.TimeTrackingService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerViewModel(
    private val pomodoroEngine: PomodoroEngine,
    private val context: Context,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    val state: StateFlow<PomodoroEngineState> = pomodoroEngine.state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PomodoroEngineState()
        )

    init {
        viewModelScope.launch {
            pomodoroEngine.effects.collect { effect ->
                when (effect) {
                    PomodoroEffect.PeriodFinished -> {
                        audioPlayer.playSessionBasicEndSound()
                    }
                }
            }
        }
    }

    fun startTimer() {
        sendCommand(TimeTrackingService.ACTION_START)
    }

    fun pauseTimer() {
        sendCommand(TimeTrackingService.ACTION_PAUSE)
    }

    fun stopTimer() {
        sendCommand(TimeTrackingService.ACTION_STOP)
    }

    private fun sendCommand(action: String) {
        val intent = Intent(context, TimeTrackingService::class.java).apply {
            this.action = action
        }
        ContextCompat.startForegroundService(context, intent)
    }

    override fun onCleared() {
        audioPlayer.stop()
        super.onCleared()
    }
}
