package com.ficusflower.pomodoroasmr.domain.engines

import androidx.annotation.StringRes
import com.ficusflower.pomodoroasmr.domain.audio.AudioMode

enum class UnifiedStatus { IDLE, RUNNING, PAUSED }

data class UnifiedTrackingState(
    @StringRes val title: Int,
    val formattedTime: String,
    val status: UnifiedStatus,
    val audioMode: AudioMode = AudioMode.SessionEndAlert
)

sealed interface UnifiedEffect {
    data class SessionFinished(val audioMode: AudioMode) : UnifiedEffect
}