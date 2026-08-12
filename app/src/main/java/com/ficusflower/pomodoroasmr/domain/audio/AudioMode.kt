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

    data object Music_1 : AmbientMode() {
        override val sound: Int = R.raw.hitslab_irish_celtic_medieval_music
    }

    data object Music_2 : AmbientMode() {
        override val sound: Int = R.raw.maksymmalko_medieval_irish_celtic_ireland_music
    }

    data object Music_3 : AmbientMode() {
        override val sound: Int = R.raw.medieval_horizons_in_the_courtyard_of_the_castle
    }

    data object Music_4 : AmbientMode() {
        override val sound: Int = R.raw.watermelon_beats_medieval_folk_music
    }
}