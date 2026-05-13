package com.example.pomodoroasmr.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodoroasmr.R
import java.time.YearMonth
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StatsScreen() {
    val kuraleFont = FontFamily(Font(R.font.kurale_regular))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        CalendarSection(kuraleFont)

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        StatisticsSummary(kuraleFont)
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
fun CalendarSection(fontFamily: FontFamily) {
    val state = remember { createCalendarState(YearMonth.now()) }
    val kuraleFont = FontFamily(Font(R.font.kurale_regular))

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        CalendarHeader(state.title, fontFamily)
        CalendarGrid(state.days, fontFamily)
    }
}

@Composable
fun CalendarHeader(title: String, fontFamily: FontFamily) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp),
        fontFamily = fontFamily
    )
}

@Composable
fun CalendarGrid(days: List<String>, fontFamily: FontFamily) {
    val weekdays = stringArrayResource(id = R.array.week_days)

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(weekdays) { DayOfWeekLabel(it, fontFamily) }
        items(days) { DayCell(it, fontFamily) }
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
fun DayCell(day: String, fontFamily: FontFamily) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (day.isNotEmpty()) {
            Text(text = day, fontFamily = fontFamily)
        }
    }
}

@Composable
fun StatisticsSummary(fontFamily: FontFamily) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.work_time_daily, "TODO"),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = fontFamily
        )
        Text(
            text = stringResource(R.string.work_time_total, "TODO"),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = fontFamily
        )
    }
}
