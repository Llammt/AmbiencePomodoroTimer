package com.ficusflower.pomodoroasmr.infrastructure.audio

import android.content.Context
import android.media.MediaPlayer
import com.ficusflower.pomodoroasmr.R

class AudioPlayer(private val context: Context) {
    private var mediaPlayer : MediaPlayer? = null

    fun playSessionBasicEndSound() {
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(context, R.raw.classic_bike_bell).apply {
            start()
            setOnCompletionListener {
                release()
                mediaPlayer = null
            }
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}