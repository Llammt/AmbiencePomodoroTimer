package com.ficusflower.pomodoroasmr.domain.audio

import com.ficusflower.pomodoroasmr.R

sealed class AudioMode {
    val sessionEndSound = R.raw.classic_bike_bell

    data object Silence : AudioMode()

    data object SessionEndAlert : AudioMode()

    data class Ambient(val mode: String) : AudioMode() {
        val ambientSound: Int
            get() = when (mode) {
                "spring forest" -> R.raw.ambient_spring_forest
                "water" -> R.raw.ambient_water
                else -> 0
            }
    }

    data class CustomAmbient(val trackName: String) : AudioMode()
}