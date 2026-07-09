package com.ficusflower.pomodoroasmr.features.pomodoro

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroConfig
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngine
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngineState
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroStatus
import com.ficusflower.pomodoroasmr.infrastructure.service.TimeTrackingService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerViewModel(
    val pomodoroEngine: PomodoroEngine,
    private val context: Context
) : ViewModel() {
    private val _showSaveDialog = MutableStateFlow(false)
    val showSaveDialog: StateFlow<Boolean> = _showSaveDialog.asStateFlow()

    val state: StateFlow<PomodoroEngineState> = pomodoroEngine.state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PomodoroEngineState()
        )

    fun startTimer(config: PomodoroConfig) {
        pomodoroEngine.currentConfig = config
        sendCommand(TimeTrackingService.ACTION_START)
    }

    fun resumeTimer() {
        sendCommand(TimeTrackingService.ACTION_START)
    }

    fun pauseTimer() {
        sendCommand(TimeTrackingService.ACTION_PAUSE)
    }

    fun stopTimer() {
        when (state.value.status) {
            PomodoroStatus.IDLE -> {}
            else -> {
                sendCommand(TimeTrackingService.ACTION_STOP)
                _showSaveDialog.value = true
            }
        }
    }

    fun onSaveWorkTime() {
        val durationToSave = pomodoroEngine.pendingWorkDuration.value ?: 0L

        viewModelScope.launch {
            pomodoroEngine.saveWorkDuration(durationToSave)
        }

        _showSaveDialog.value = false
        pomodoroEngine.resetWorkDuration()
    }

    fun onDiscardWorkTime() {
        _showSaveDialog.value = false
        pomodoroEngine.resetWorkDuration()
    }

    private fun sendCommand(action: String) {
        val intent = Intent(context, TimeTrackingService::class.java).apply {
            this.action = action
        }
        ContextCompat.startForegroundService(context, intent)
    }

    val pendingTimeFormatted: StateFlow<String> = pomodoroEngine.pendingWorkDuration
        .map { millis -> formatTime(millis ?: 0L) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "00:00")
}
