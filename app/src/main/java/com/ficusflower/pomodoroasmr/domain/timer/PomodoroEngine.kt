package com.ficusflower.pomodoroasmr.domain.timer

import com.ficusflower.pomodoroasmr.domain.model.Session
import com.ficusflower.pomodoroasmr.domain.repository.SessionRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate

sealed interface PomodoroEffect {
    object PeriodFinished : PomodoroEffect
}

class PomodoroEngine(
    private val repository: SessionRepository
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var timerJob: Job? = null

    private var cycleCount = 0
    private var currentPeriod: PomodoroPeriod = PomodoroPeriod.Work

    private val _state = MutableStateFlow(PomodoroEngineState())
    val state: StateFlow<PomodoroEngineState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<PomodoroEffect>()
    val effects: SharedFlow<PomodoroEffect> = _effects.asSharedFlow()

    fun start() {
        if (_state.value.status == PomodoroStatus.RUNNING) return

        if (_state.value.status == PomodoroStatus.IDLE) {
            currentPeriod = PomodoroPeriod.Work
            _state.value = PomodoroEngineState(
                status = PomodoroStatus.RUNNING,
                period = currentPeriod,
                millisLeft = currentPeriod.durationMillis
            )
        } else {
            _state.value = _state.value.copy(status = PomodoroStatus.RUNNING)
        }

        startTicker(_state.value.millisLeft)
    }

    fun pause() {
        if (_state.value.status == PomodoroStatus.RUNNING) {
            timerJob?.cancel()
            _state.value = _state.value.copy(status = PomodoroStatus.PAUSED)
        }
    }

    fun stop() {
        timerJob?.cancel()
        currentPeriod = PomodoroPeriod.Work
        _state.value = PomodoroEngineState()
    }

    private fun startTicker(duration: Long) {
        timerJob?.cancel()
        timerJob = scope.launch {
            var timeLeft = duration
            while (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1000
                _state.value = _state.value.copy(millisLeft = timeLeft)
            }
            onPeriodFinished()
        }
    }

    private suspend fun onPeriodFinished() {
        if (currentPeriod == PomodoroPeriod.Work) {
            cycleCount++
            val record = Session(
                date = LocalDate.now().toString(),
                workDuration = currentPeriod.durationMillis
            )
            repository.insert(record)
        }

        currentPeriod = nextPeriod(currentPeriod)

        _state.value = PomodoroEngineState(
            status = PomodoroStatus.RUNNING,
            period = currentPeriod,
            millisLeft = currentPeriod.durationMillis
        )

        _effects.emit(PomodoroEffect.PeriodFinished)

        startTicker(currentPeriod.durationMillis)
    }

    private fun nextPeriod(period: PomodoroPeriod): PomodoroPeriod = when (period) {
        PomodoroPeriod.Work -> if ((cycleCount + 1) % 4 == 0) PomodoroPeriod.LongBreak else PomodoroPeriod.ShortBreak
        PomodoroPeriod.ShortBreak, PomodoroPeriod.LongBreak -> PomodoroPeriod.Work
    }
}

data class PomodoroEngineState(
    val status: PomodoroStatus = PomodoroStatus.IDLE,
    val period: PomodoroPeriod = PomodoroPeriod.Work,
    val millisLeft: Long = PomodoroPeriod.Work.durationMillis
)

enum class PomodoroStatus { IDLE, RUNNING, PAUSED }
enum class PomodoroPeriod(val durationMillis: Long, val label: String) {
    Work(60_000L, "Work"),
    ShortBreak(60_000L, "Short Break"),
    LongBreak(60_000L, "Long Break")
}