package com.ficusflower.pomodoroasmr.domain.audio

fun mapIndexToAudioMode(index: Int): AudioMode {
    return when (index) {
        0 -> AudioMode.Silence
        1 -> AudioMode.SessionEndAlert
        2 -> AudioMode.Ambient(AmbientMode.SpringForest)
        3 -> AudioMode.Ambient(AmbientMode.Water)
        4 -> AudioMode.Ambient(AmbientMode.Music_1)
        5 -> AudioMode.Ambient(AmbientMode.Music_2)
        6 -> AudioMode.Ambient(AmbientMode.Music_3)
        7 -> AudioMode.Ambient(AmbientMode.Music_4)

        else -> AudioMode.SessionEndAlert
    }
}
