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
    var currentConfig = PomodoroConfig()
    private var currentPeriod: PomodoroPeriod = PomodoroPeriod.Work(currentConfig.workDurationMillis)
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var timerJob: Job? = null
    private var cycleCount = 0

    private val _state = MutableStateFlow(PomodoroEngineState())
    val state: StateFlow<PomodoroEngineState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<PomodoroEffect>()
    val effects: SharedFlow<PomodoroEffect> = _effects.asSharedFlow()

    fun start(config: PomodoroConfig) {
        if (_state.value.status == PomodoroStatus.RUNNING) return

        if (_state.value.status == PomodoroStatus.IDLE) {
            currentConfig = config
            currentPeriod = PomodoroPeriod.Work(config.workDurationMillis)

            _state.value = PomodoroEngineState(
                status = PomodoroStatus.RUNNING,
                period = currentPeriod,
                millisLeft = config.workDurationMillis
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
        currentPeriod = PomodoroPeriod.Work(currentConfig.workDurationMillis)

        _state.value = PomodoroEngineState(
            status = PomodoroStatus.IDLE,
            period = currentPeriod,
            millisLeft = currentConfig.workDurationMillis
        )
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
        if (currentPeriod is PomodoroPeriod.Work) {
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
        is PomodoroPeriod.Work -> {
            if ((cycleCount + 1) % 4 == 0) {
                PomodoroPeriod.LongBreak(currentConfig.longBreakDurationMillis)
            } else {
                PomodoroPeriod.ShortBreak(currentConfig.shortBreakDurationMillis)
            }
        }
        is PomodoroPeriod.ShortBreak, is PomodoroPeriod.LongBreak -> {
            PomodoroPeriod.Work(currentConfig.workDurationMillis)
        }
    }
}

data class PomodoroEngineState(
    val status: PomodoroStatus = PomodoroStatus.IDLE,
    val period: PomodoroPeriod = PomodoroPeriod.Work(25 * 60 * 1000L),  //хардкод:(( Подумать и убрать.
    val millisLeft: Long = 25 * 60 * 1000L
)

enum class PomodoroStatus { IDLE, RUNNING, PAUSED }
sealed class PomodoroPeriod(val durationMillis: Long, val label: String) {
    class Work(duration: Long) : PomodoroPeriod(duration, "Work")
    class ShortBreak(duration: Long) : PomodoroPeriod(duration, "Short Break")
    class LongBreak(duration: Long) : PomodoroPeriod(duration, "Long Break")
}

data class PomodoroConfig(
    val workDurationMillis: Long = 25 * 60 * 1000L,
    val shortBreakDurationMillis: Long = 5 * 60 * 1000L,
    val longBreakDurationMillis: Long = 15 * 60 * 1000L
)