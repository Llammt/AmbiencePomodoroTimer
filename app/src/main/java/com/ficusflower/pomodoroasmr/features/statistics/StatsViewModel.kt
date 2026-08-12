package com.ficusflower.pomodoroasmr.features.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ficusflower.pomodoroasmr.domain.model.Session
import com.ficusflower.pomodoroasmr.domain.repository.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import kotlin.collections.forEach

class StatsViewModel(private val repository: SessionRepository) : ViewModel() {
    private val _viewedMonth = MutableStateFlow(YearMonth.now())
    val viewedMonth: StateFlow<YearMonth> = _viewedMonth

    fun nextMonth() { _viewedMonth.value = _viewedMonth.value.plusMonths(1) }
    fun prevMonth() { _viewedMonth.value = _viewedMonth.value.minusMonths(1) }

    private val _selectedDate = MutableStateFlow(LocalDate.now().toString())
    val selectedDate: StateFlow<String> = _selectedDate

    fun onDateSelected(date: String) {
        _selectedDate.value = date
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val dailyWorkDuration: StateFlow<String> = combine(_selectedDate, _viewedMonth) { selectedDateStr, viewedMonth ->
        val selectedDate = LocalDate.parse(selectedDateStr)
        val today = LocalDate.now()
        val isCorrectMonth = YearMonth.from(selectedDate) == viewedMonth
        val isFutureDate = selectedDate.isAfter(today)

        Triple(selectedDateStr, isCorrectMonth, isFutureDate)
    }
        .flatMapLatest { (dateStr, isCorrectMonth, isFutureDate) ->
            if (!isCorrectMonth || isFutureDate) {
                flowOf(0L)
            } else {
                repository.getDailyWorkDuration(dateStr)
            }
        }
        .map { duration -> formatDuration(duration ?: 0L) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "0 min"
        )

    val totalWorkDuration: StateFlow<String> = repository.getTotalWorkDuration()
        .map { duration -> formatDuration(duration ?: 0L) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "0 min"
        )

    private fun formatDuration(millis: Long): String {
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))
        val seconds = (millis / 1000) % 60

        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m ${seconds}s"
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val weeklyMinutes: StateFlow<List<Int>> = selectedDate
        .flatMapLatest { dateString ->
            val date = LocalDate.parse(dateString)
            val monday = date.with(DayOfWeek.MONDAY)
            val sunday = date.with(DayOfWeek.SUNDAY)

            repository.getSessionsBetweenDates(monday.toString(), sunday.toString())
                .map { sessions ->
                    calculateWeeklyMinutes(sessions)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = List(7) { 0 }
        )

    private fun calculateWeeklyMinutes(sessions: List<Session>): List<Int> {
        val result = IntArray(7) { 0 }

        sessions.forEach { session ->
            val sessionDate = LocalDate.parse(session.date)

            val dayIndex = sessionDate.dayOfWeek.value - 1

            if (dayIndex in 0..6) {
                val minutes = session.workDuration / 60
                result[dayIndex] += minutes.toInt()
            }
        }

        return result.toList()
    }
}