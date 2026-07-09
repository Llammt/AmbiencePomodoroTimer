package com.ficusflower.pomodoroasmr.infrastructure.audio

import android.content.Context
import android.media.MediaPlayer
import com.ficusflower.pomodoroasmr.domain.audio.AudioMode

class AudioPlayer(private val context: Context) {
    private var ambientPlayer: MediaPlayer? = null
    private var alertPlayer: MediaPlayer? = null

    private var currentAmbientMode: AudioMode? = null

    fun playSessionBasicEndSound(audioMode: AudioMode) {
        if (audioMode is AudioMode.Silence) return

        alertPlayer?.release()
        alertPlayer = MediaPlayer.create(context, audioMode.sessionEndSound).apply {
            start()
            setOnCompletionListener {
                release()
                alertPlayer = null
            }
        }
    }

    fun playAmbient(audioMode: AudioMode) {
        if (currentAmbientMode == audioMode && ambientPlayer?.isPlaying == true) {
            return
        }

        stopAmbient()
        currentAmbientMode = audioMode

        when (audioMode) {
            is AudioMode.Ambient -> {
                if (audioMode.ambientSound != 0) {
                    ambientPlayer = MediaPlayer.create(context, audioMode.ambientSound).apply {
                        isLooping = true
                        start()
                    }
                }
            }
            is AudioMode.CustomAmbient -> {
                try {
                    val afd = context.assets.openFd(audioMode.trackName)
                    ambientPlayer = MediaPlayer().apply {
                        setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                        prepare()
                        isLooping = true
                        start()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ambientPlayer = null
                }
            }
            is AudioMode.Silence, is AudioMode.SessionEndAlert -> { }
        }
    }

    fun stop() {
        stopAmbient()

        alertPlayer?.stop()
        alertPlayer?.release()
        alertPlayer = null
    }

    fun pause() {
        ambientPlayer?.takeIf { it.isPlaying }?.pause()
    }

    private fun stopAmbient() {
        ambientPlayer?.stop()
        ambientPlayer?.release()
        ambientPlayer = null
        currentAmbientMode = null
    }
}