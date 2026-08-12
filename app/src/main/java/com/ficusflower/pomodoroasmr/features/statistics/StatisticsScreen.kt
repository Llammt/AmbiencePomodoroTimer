package com.ficusflower.pomodoroasmr.features.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ficusflower.pomodoroasmr.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.ficusflower.pomodoroasmr.features.statistics.components.CalendarSection
import com.ficusflower.pomodoroasmr.features.statistics.components.StatisticsSummary
import com.ficusflower.pomodoroasmr.features.statistics.components.WeeklyStatsChart
import com.ficusflower.pomodoroasmr.features.statistics.components.createCalendarState

@Composable
fun StatsScreen(viewModel: StatsViewModel = viewModel()) {
    val kuraleFont = FontFamily(Font(R.font.kurale_regular))
    val viewedMonth by viewModel.viewedMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val totalDuration by viewModel.totalWorkDuration.collectAsState()
    val dailyDuration by viewModel.dailyWorkDuration.collectAsState()

    val calendarState = remember(viewedMonth) { createCalendarState(viewedMonth)}

    val weeklyMinutes by viewModel.weeklyMinutes.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        CalendarSection(
            calendarState = calendarState,
            fontFamily = kuraleFont,
            selectedDate = selectedDate,
            viewedMonth = viewedMonth,
            onDayClick = { day ->
                val formattedDate = "%s-%02d".format(viewedMonth.toString(), day.toInt())
                viewModel.onDateSelected(formattedDate)
            },
            onPrevClick = { viewModel.prevMonth() },
            onNextClick = { viewModel.nextMonth() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        StatisticsSummary(totalDuration = totalDuration, dailyDuration = dailyDuration, kuraleFont)

        Spacer(modifier = Modifier.height(40.dp))

        WeeklyStatsChart(
            weeklyMinutes = weeklyMinutes,
            fontFamily = kuraleFont,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}
