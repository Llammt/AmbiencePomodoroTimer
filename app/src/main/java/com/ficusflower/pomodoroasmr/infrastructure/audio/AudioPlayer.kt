package com.ficusflower.pomodoroasmr.infrastructure.audio

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.ficusflower.pomodoroasmr.domain.audio.AudioMode
import androidx.core.net.toUri

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

        ambientPlayer?.stop()
        ambientPlayer?.release()

        when (audioMode) {
            is AudioMode.Silence -> {
                ambientPlayer = null
            }
            is AudioMode.Ambient -> {
                try {
                    ambientPlayer = MediaPlayer.create(context, audioMode.ambientSound).apply {
                        isLooping = true
                        start()
                    }
                } catch (e: Exception) {
                    ambientPlayer = null
                }
            }
            is AudioMode.CustomAmbient -> {
                try {
                    val uri = Uri.parse(audioMode.uriString)
                    ambientPlayer = MediaPlayer().apply {
                        setDataSource(context, uri)
                        isLooping = true
                        setOnPreparedListener { player ->
                            player.start()
                        }
                        prepareAsync()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ambientPlayer = null
                }
            }
            else -> { /* SessionEndAlert logic */ }
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