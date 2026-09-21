package com.ficusflower.pomodoroasmr.domain.engines

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.ficusflower.pomodoroasmr.domain.audio.AudioMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import com.ficusflower.pomodoroasmr.R

enum class AppMode { POMODORO, STOPWATCH, TIMER }

@OptIn(ExperimentalCoroutinesApi::class)
class TrackingManager(
    val pomodoroEngine: PomodoroEngine,
    val stopwatchEngine: StopwatchEngine
) {
    private val _currentMode = MutableStateFlow(AppMode.POMODORO)
    val currentMode = _currentMode.asStateFlow()

    var stopwatchAudioMode: AudioMode = AudioMode.SessionEndAlert

    val activeState: Flow<UnifiedTrackingState> = currentMode.flatMapLatest { mode ->
        when (mode) {
            AppMode.POMODORO -> pomodoroEngine.state.map { pState ->
                UnifiedTrackingState(
                    title = pState.period.label,
                    formattedTime = formatMillisToMinSec(pState.millisLeft),
                    status = when (pState.status) {
                        PomodoroStatus.RUNNING -> UnifiedStatus.RUNNING
                        PomodoroStatus.PAUSED -> UnifiedStatus.PAUSED
                        PomodoroStatus.IDLE -> UnifiedStatus.IDLE
                    },
                    audioMode = getAudioModeForPeriod(pState.period)
                )
            }
            AppMode.STOPWATCH -> stopwatchEngine.state.map { sState ->
                UnifiedTrackingState(
                    title = R.string.stopwatch_text_label,
                    formattedTime = formatMillisToTime(sState.elapsedMillis),
                    status = when (sState.status) {
                        StopwatchStatus.RUNNING -> UnifiedStatus.RUNNING
                        StopwatchStatus.PAUSED -> UnifiedStatus.PAUSED
                        StopwatchStatus.IDLE -> UnifiedStatus.IDLE
                    },
                    audioMode = stopwatchAudioMode
                )
            }
            AppMode.TIMER -> emptyFlow()
        }
    }

    val effects: Flow<UnifiedEffect> = currentMode.flatMapLatest { mode ->
        when (mode) {
            AppMode.POMODORO -> pomodoroEngine.effects.map { effect ->
                when (effect) {
                    is PomodoroEffect.PeriodFinished -> UnifiedEffect.SessionFinished(
                        getAudioModeForPeriod(effect.completedPeriod)
                    )
                }
            }
            AppMode.STOPWATCH -> emptyFlow()
            AppMode.TIMER -> emptyFlow()
        }
    }

    fun setMode(mode: AppMode) {
        if (_currentMode.value != mode) {
            stopCurrent()
            _currentMode.value = mode
        }
    }

    fun startCurrent() {
        when (_currentMode.value) {
            AppMode.POMODORO -> pomodoroEngine.start(pomodoroEngine.currentConfig)
            AppMode.STOPWATCH -> stopwatchEngine.start()
            AppMode.TIMER -> {}
        }
    }

    fun pauseCurrent() {
        when (_currentMode.value) {
            AppMode.POMODORO -> pomodoroEngine.pause()
            AppMode.STOPWATCH -> stopwatchEngine.pause()
            AppMode.TIMER -> {}
        }
    }

    fun stopCurrent() {
        when (_currentMode.value) {
            AppMode.POMODORO -> pomodoroEngine.stop()
            AppMode.STOPWATCH -> stopwatchEngine.stopAndSave()
            AppMode.TIMER -> {}
        }
    }

    private fun getAudioModeForPeriod(period: PomodoroPeriod): AudioMode {
        val config = pomodoroEngine.currentConfig
        return when (period) {
            is PomodoroPeriod.Work -> config.workAudioMode
            is PomodoroPeriod.ShortBreak -> config.shortBreakAudioMode
            is PomodoroPeriod.LongBreak -> config.longBreakAudioMode
        }
    }

    private fun formatMillisToMinSec(millis: Long): String {
        val minutes = (millis / 60_000).toString().padStart(2, '0')
        val seconds = ((millis / 1_000) % 60).toString().padStart(2, '0')
        return "$minutes : $seconds"
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
