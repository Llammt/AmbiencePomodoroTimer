package com.ficusflower.pomodoroasmr.infrastructure.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ficusflower.pomodoroasmr.R
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroStatus
import com.ficusflower.pomodoroasmr.domain.timer.TrackingManager
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class TimeTrackingService : Service() {

    private val trackingManager: TrackingManager by inject()

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var observeJob: Job? = null

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

        val notification = buildNotification("Focus", "00:00")
        startForeground(NOTIFICATION_ID, notification)

        observeJob?.cancel()
        observeJob = serviceScope.launch {
            trackingManager.pomodoroEngine.state.collect { state ->
                when (state.status) {
                    PomodoroStatus.RUNNING -> {
                        val minutes = (state.millisLeft / 60_000).toString().padStart(2, '0')
                        val seconds = ((state.millisLeft / 1_000) % 60).toString().padStart(2, '0')
                        updateNotification(state.period.label, "$minutes : $seconds")
                    }
                    PomodoroStatus.PAUSED -> {
                        updateNotification("Paused", "Timer is paused")
                    }
                    PomodoroStatus.IDLE -> {
                        stopTracking()
                    }
                }
            }
        }
    }

    private fun pauseTracking() {
        trackingManager.pauseCurrent()
    }

    private fun stopTracking() {
        trackingManager.stopCurrent()
        observeJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildNotification(title: String, content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: app icon
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(title: String, content: String) {
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
