package com.ficusflower.pomodoroasmr.domain.audio

fun mapIndexToAudioMode(index: Int): AudioMode {
    return when (index) {
        0 -> AudioMode.Silence
        1 -> AudioMode.SessionEndAlert
        2 -> AudioMode.Ambient(AmbientMode.SpringForest)
        3 -> AudioMode.Ambient(AmbientMode.Water)

        else -> AudioMode.SessionEndAlert
    }
}
