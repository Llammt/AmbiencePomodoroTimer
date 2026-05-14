package com.example.pomodoroasmr.statistics

import androidx.core.util.TimeUtils.formatDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoroasmr.data.SessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(private val repository: SessionRepository) : ViewModel() {

    val totalWorkDuration: StateFlow<String> = repository.getTotalWorkDuration()
        .map { duration ->
            formatDuration(duration ?: 0L)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "0 min"
        )

    private fun formatDuration(millis: Long): String {
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))

        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }
}