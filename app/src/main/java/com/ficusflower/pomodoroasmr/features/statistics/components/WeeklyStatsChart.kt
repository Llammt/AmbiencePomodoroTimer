package com.ficusflower.pomodoroasmr.features.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ficusflower.pomodoroasmr.R

@Composable
fun WeeklyStatsChart(
    weeklyMinutes: List<Int>,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onBackground
    val barColor = MaterialTheme.colorScheme.tertiary

    val maxMinutes = weeklyMinutes.maxOrNull()?.coerceAtLeast(1) ?: 1
    val daysOfWeek = stringArrayResource(id = R.array.week_days)

    val textMeasurer = rememberTextMeasurer()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.weekly_activity_text_label),
            style = MaterialTheme.typography.titleMedium,
            fontFamily = fontFamily,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .border(
                    width = 1.dp,
                    color = textColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val totalWidth = size.width
                val totalHeight = size.height
                val barCount = weeklyMinutes.size

                val sectionWidth = totalWidth / barCount
                val barWidth = sectionWidth * 0.5f

                val textSpacePx = 24.dp.toPx()
                val availableHeight = totalHeight - textSpacePx

                weeklyMinutes.forEachIndexed { index, minutes ->
                    val barHeight = availableHeight * (minutes.toFloat() / maxMinutes)

                    val x = index * sectionWidth + (sectionWidth - barWidth) / 2
                    val y = totalHeight - barHeight

                    if (minutes > 0) {
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )
                    }

                    val textLayoutResult = textMeasurer.measure(
                        text = "${minutes}м",
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = fontFamily,
                            fontSize = 11.sp,
                            color = textColor.copy(alpha = 0.8f)
                        )
                    )

                    val textX = x + (barWidth - textLayoutResult.size.width) / 2
                    val textY = y - textLayoutResult.size.height - 4.dp.toPx()
                    drawText(textLayoutResult, topLeft = Offset(textX, textY))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = fontFamily,
                    textAlign = TextAlign.Center,
                    color = textColor.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}