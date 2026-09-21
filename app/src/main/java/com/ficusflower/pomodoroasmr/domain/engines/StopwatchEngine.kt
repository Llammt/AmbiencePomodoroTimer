package com.ficusflower.pomodoroasmr.domain.engines

import com.ficusflower.pomodoroasmr.domain.model.Session
import com.ficusflower.pomodoroasmr.domain.repository.SessionRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class StopwatchEngine(
    private val repository: SessionRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
) {
    private var timerJob: Job? = null

    private val _state = MutableStateFlow(StopwatchEngineState())
    val state: StateFlow<StopwatchEngineState> = _state.asStateFlow()

    fun start() {
        if (_state.value.status == StopwatchStatus.RUNNING) return

        _state.value = _state.value.copy(status = StopwatchStatus.RUNNING)

        timerJob?.cancel()
        timerJob = scope.launch {
            while (isActive) {
                delay(1000)
                _state.update { currentState ->
                    currentState.copy(elapsedMillis = currentState.elapsedMillis + 1000L)
                }
            }
        }
    }

    fun pause() {
        if (_state.value.status == StopwatchStatus.RUNNING) {
            timerJob?.cancel()
            _state.value = _state.value.copy(status = StopwatchStatus.PAUSED)
        }
    }

    fun stopAndSave() {
        timerJob?.cancel()
        val totalElapsed = _state.value.elapsedMillis

        if (totalElapsed > 0) {
            scope.launch {
                saveSession(totalElapsed)
            }
        }

        _state.value = StopwatchEngineState(
            status = StopwatchStatus.IDLE,
            elapsedMillis = 0L
        )
    }

    private suspend fun saveSession(durationMillis: Long) {
        val record = Session(
            date = LocalDate.now().toString(),
            workDuration = durationMillis
        )
        repository.insert(record)
    }
}

data class StopwatchEngineState(
    val status: StopwatchStatus = StopwatchStatus.IDLE,
    val elapsedMillis: Long = 0L
)

enum class StopwatchStatus { IDLE, RUNNING, PAUSED }