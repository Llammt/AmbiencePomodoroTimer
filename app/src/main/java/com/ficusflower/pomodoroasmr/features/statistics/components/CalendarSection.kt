package com.ficusflower.pomodoroasmr.features.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ficusflower.pomodoroasmr.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

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
fun CalendarGrid(
    days: List<String>,
    fontFamily: FontFamily,
    selectedDate: String,
    viewedMonth: YearMonth,
    onDayClick: (String) -> Unit
) {
    val weekdays = stringArrayResource(id = R.array.week_days)
    val parsedSelectedDate = LocalDate.parse(selectedDate)
    val isCurrentMonthViewed = YearMonth.from(parsedSelectedDate) == viewedMonth

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            weekdays.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    DayOfWeekLabel(day, fontFamily)
                }
            }
        }

        val weeks = days.chunked(7)

        weeks.forEach { weekDays ->
            Row(modifier = Modifier.fillMaxWidth()) {
                weekDays.forEach { day ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        DayCell(
                            day = day,
                            fontFamily = fontFamily,
                            isSelected = (isCurrentMonthViewed && day == parsedSelectedDate.dayOfMonth.toString() && day.isNotEmpty()),
                            onClick = { clickedDay -> onDayClick(clickedDay) }
                        )
                    }
                }

                if (weekDays.size < 7) {
                    Spacer(modifier = Modifier.weight((7 - weekDays.size).toFloat()))
                }
            }
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

fun createCalendarState(month: YearMonth, locale: Locale = Locale.getDefault()): CalendarUiState {
    val title = "${month.month.getDisplayName(TextStyle.FULL, locale)} ${month.year}"
    val daysInMonth = month.lengthOfMonth()

    val firstDayOfMonth = month.atDay(1).dayOfWeek.value
    val days = (1 until firstDayOfMonth).map { "" } + (1..daysInMonth).map { it.toString() }

    return CalendarUiState(title, days)
}

data class CalendarUiState(
    val title: String,
    val days: List<String>
)