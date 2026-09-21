package com.ficusflower.pomodoroasmr.infrastructure.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.ficusflower.pomodoroasmr.R
import com.ficusflower.pomodoroasmr.domain.engines.TrackingManager
import com.ficusflower.pomodoroasmr.domain.engines.UnifiedEffect
import com.ficusflower.pomodoroasmr.domain.engines.UnifiedStatus
import com.ficusflower.pomodoroasmr.infrastructure.audio.AudioPlayer
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class TimeTrackingService : Service() {

    private val trackingManager: TrackingManager by inject()
    private val audioPlayer: AudioPlayer by inject()

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var observeJob: Job? = null
    private var effectsJob: Job? = null

    companion object {
        const val CHANNEL_ID = "time_tracking_channel"
        const val NOTIFICATION_ID = 42

        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTracking()
            ACTION_PAUSE -> pauseTracking()
            ACTION_STOP -> stopTracking()
        }
        return START_STICKY
    }

    private fun startTracking() {

        trackingManager.startCurrent()

        val notification = buildNotification(getString(R.string.time_tracker_text_label), "00:00")
        startForeground(NOTIFICATION_ID, notification)

        observeJob?.cancel()
        observeJob = serviceScope.launch {
            trackingManager.activeState.collect { state ->
                when (state.status) {
                    UnifiedStatus.RUNNING -> {
                        updateNotification(state.title, state.formattedTime)
                        audioPlayer.playAmbient(state.audioMode)
                    }
                    UnifiedStatus.PAUSED -> {
                        updateNotification(state.title, getString(R.string.paused_state_text_label))
                        audioPlayer.pause()
                    }
                    UnifiedStatus.IDLE -> {
                        stopTracking()
                    }
                }
            }
        }

        effectsJob?.cancel()
        effectsJob = serviceScope.launch {
            trackingManager.effects.collect { effect ->
                when (effect) {
                    is UnifiedEffect.SessionFinished -> {
                        audioPlayer.playSessionBasicEndSound(effect.audioMode)
                    }
                }
            }
        }
    }

    private fun pauseTracking() {
        trackingManager.pauseCurrent()
        audioPlayer.pause()
    }

    private fun stopTracking() {
        trackingManager.stopCurrent()
        observeJob?.cancel()
        effectsJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        audioPlayer.stop()
        stopSelf()
    }

    private fun buildNotification(title: String, content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(@StringRes titleRes: Int, content: String) {
        val title = getString(titleRes)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, buildNotification(title, content))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Time Tracking Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
}