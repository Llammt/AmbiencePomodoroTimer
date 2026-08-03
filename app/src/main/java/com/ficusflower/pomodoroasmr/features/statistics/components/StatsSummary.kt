package com.ficusflower.pomodoroasmr.features.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.ficusflower.pomodoroasmr.R

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