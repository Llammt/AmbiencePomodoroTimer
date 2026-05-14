package com.example.pomodoroasmr.statistics

import androidx.core.util.TimeUtils.formatDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoroasmr.data.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class StatsViewModel(private val repository: SessionRepository) : ViewModel() {
    // 1. Храним выбранную дату (строкой "yyyy-MM-dd")
    private val _selectedDate = MutableStateFlow(LocalDate.now().toString())
    val selectedDate: StateFlow<String> = _selectedDate

    // 2. Метод для смены даты
    fun onDateSelected(date: String) {
        _selectedDate.value = date
    }

    // 3. "Магия": связываем дату и запрос к БД
    // flatMapLatest значит: "как только изменится дата, отпишись от старого запроса и начни новый"
    @OptIn(ExperimentalCoroutinesApi::class)
    val dailyWorkDuration: StateFlow<String> = _selectedDate
        .flatMapLatest { date ->
            repository.getDailyWorkDuration(date)
        }
        .map { duration -> formatDuration(duration ?: 0L) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "0 min"
        )

    val totalWorkDuration: StateFlow<String> = repository.getTotalWorkDuration()
        .map { duration ->
            formatDuration(duration ?: 0L)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "0 min"
        )

//    val dailyWorkDuration: StateFlow<String> = repository.getDailyWorkDuration()
//        .map { duration -> formatDuration(duration ?: 0L) }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = "0 min"
//        )

    private fun formatDuration(millis: Long): String {
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))

        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }
}