package com.ficusflower.pomodoroasmr.features.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ficusflower.pomodoroasmr.domain.engines.AppMode
import com.ficusflower.pomodoroasmr.domain.engines.StopwatchStatus
import com.ficusflower.pomodoroasmr.domain.engines.TrackingManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StopwatchViewModel(
    private val trackingManager: TrackingManager
) : ViewModel() {

    private val stopwatchEngine = trackingManager.stopwatchEngine

    val uiState: StateFlow<StopwatchUiState> = stopwatchEngine.state
        .map { engineState ->
            StopwatchUiState(
                status = engineState.status,
                elapsedMillis = engineState.elapsedMillis,
                formattedTime = formatMillisToTime(engineState.elapsedMillis)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StopwatchUiState()
        )

    fun onStart() {
        trackingManager.setMode(AppMode.STOPWATCH)
        trackingManager.startCurrent()
    }

    fun onPause() {
        trackingManager.pauseCurrent()
    }

    fun onStopAndSave() {
        trackingManager.stopCurrent()
    }

    private fun formatMillisToTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}

data class StopwatchUiState(
    val status: StopwatchStatus = StopwatchStatus.IDLE,
    val elapsedMillis: Long = 0L,
    val formattedTime: String = "00:00"
)