package com.ficusflower.pomodoroasmr.domain.audio

import com.ficusflower.pomodoroasmr.R

sealed class AudioMode {
    val sessionEndSound = R.raw.classic_bike_bell

    data object Silence : AudioMode()

    data object SessionEndAlert : AudioMode()

    data class Ambient(val mode: AmbientMode) : AudioMode() {
        val ambientSound: Int = mode.sound
    }

    data class CustomAmbient(
        val uriString: String
    ) : AudioMode()
}

sealed class AmbientMode() {
    abstract val sound : Int

    data object SpringForest : AmbientMode() {
        override val sound: Int = R.raw.ambient_spring_forest
    }

    data object Water : AmbientMode() {
        override val sound: Int = R.raw.ambient_water
    }
}