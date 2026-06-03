package com.ficusflower.pomodoroasmr.features.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEffect
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngine
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngineState
import com.ficusflower.pomodoroasmr.infrastructure.audio.AudioPlayer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerViewModel(
    private val pomodoroEngine: PomodoroEngine,
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

    fun startTimer() = pomodoroEngine.start()
    fun pauseTimer() = pomodoroEngine.pause()
    fun stopTimer() = pomodoroEngine.stop()

    override fun onCleared() {
        audioPlayer.stop()
        super.onCleared()
    }
}