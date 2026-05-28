package com.example.pomodoroasmr.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodoroasmr.R
import com.example.pomodoroasmr.di.AppViewModelFactory
import com.example.pomodoroasmr.statistics.StatsViewModel
import com.example.pomodoroasmr.timer.TimerViewModel
import java.time.YearMonth
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StatsScreen(viewModel: StatsViewModel = viewModel()) {
    val kuraleFont = FontFamily(Font(R.font.kurale_regular))

    val viewedMonth by viewModel.viewedMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val totalDuration by viewModel.totalWorkDuration.collectAsState()
    val dailyDuration by viewModel.dailyWorkDuration.collectAsState()

    val calendarState = remember(viewedMonth) { createCalendarState(viewedMonth)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        StatisticsSummary(totalDuration = totalDuration, dailyDuration = dailyDuration, kuraleFont)
    }
}

data class CalendarUiState(
    val title: String,
    val days: List<String>
)

fun createCalendarState(month: YearMonth, locale: Locale = Locale.getDefault()): CalendarUiState {
    val title = "${month.month.getDisplayName(TextStyle.FULL, locale)} ${month.year}"
    val daysInMonth = month.lengthOfMonth()

    val firstDayOfMonth = month.atDay(1).dayOfWeek.value
    val days = (1 until firstDayOfMonth).map { "" } + (1..daysInMonth).map { it.toString() }

    return CalendarUiState(title, days)
}

@Composable
fun CalendarSection(calendarState: CalendarUiState,
                    fontFamily: FontFamily,
                    selectedDate: String,
                    onDayClick: (String) -> Unit,
                    viewedMonth: YearMonth,
                    onPrevClick: () -> Unit,
                    onNextClick: () -> Unit) {

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        CalendarHeader(
            title = calendarState.title,
            fontFamily = fontFamily,
            onPrevClick = onPrevClick,
            onNextClick = onNextClick
        )
        CalendarGrid(
            days = calendarState.days,
            fontFamily = fontFamily,
            selectedDate = selectedDate,
            viewedMonth = viewedMonth,
            onDayClick = onDayClick
        )
    }
}

@Composable
fun CalendarHeader(
    title: String,
    fontFamily: FontFamily,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrevClick) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous")
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = fontFamily
        )

        IconButton(onClick = onNextClick) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
        }
    }
}

@Composable
fun CalendarGrid(days: List<String>,
                 fontFamily: FontFamily,
                 selectedDate: String,
                 viewedMonth: YearMonth,
                 onDayClick: (String) -> Unit) {
    val weekdays = stringArrayResource(id = R.array.week_days)
    val parsedSelectedDate = LocalDate.parse(selectedDate)
    val isCurrentMonthViewed = YearMonth.from(parsedSelectedDate) == viewedMonth

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(weekdays) { DayOfWeekLabel(it, fontFamily) }
        items(days) { day ->
            DayCell(
                day = day,
                fontFamily = fontFamily,
                isSelected = (isCurrentMonthViewed && day == parsedSelectedDate.dayOfMonth.toString() && day.isNotEmpty()),
                onClick = { clickedDay -> onDayClick(clickedDay) }
            )
        }
    }
}

@Composable
fun DayOfWeekLabel(day: String, fontFamily: FontFamily) {
    Text(
        text = day,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontFamily = fontFamily
    )
}

@Composable
fun DayCell(day: String, fontFamily: FontFamily, isSelected : Boolean, onClick : (String) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent)
            .clickable(enabled = day.isNotEmpty()) { onClick(day) },
        contentAlignment = Alignment.Center
    ) {
        if (day.isNotEmpty()) {
            Text(text = day, fontFamily = fontFamily, color = if(isSelected) MaterialTheme.colorScheme.onPrimaryContainer else Color.Unspecified)
        }
    }
}

@Composable
fun StatisticsSummary(totalDuration: String, dailyDuration: String, fontFamily: FontFamily) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.work_time_daily, dailyDuration),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = fontFamily
        )
        Text(
            text = stringResource(R.string.work_time_total, totalDuration),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = fontFamily
        )
    }
}
