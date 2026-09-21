package com.ficusflower.pomodoroasmr.features.stopwatch

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ficusflower.pomodoroasmr.R
import com.ficusflower.pomodoroasmr.domain.engines.StopwatchStatus
import org.koin.androidx.compose.koinViewModel

@Composable
fun StopwatchScreen(
    viewModel: StopwatchViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StopwatchContent(
        state = uiState,
        onStart = viewModel::onStart,
        onPause = viewModel::onPause,
        onStop = viewModel::onStopAndSave
    )
}

@Composable
fun StopwatchContent(
    state: StopwatchUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.stopwatch_text_label),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = state.formattedTime,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displayLarge
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            when (state.status) {
                StopwatchStatus.IDLE -> {
                    Button(
                        onClick = onStart,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(text = "Старт")
                    }
                }
                StopwatchStatus.RUNNING -> {
                    Button(
                        onClick = onPause,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(text = "Пауза")
                    }
                    OutlinedButton(
                        onClick = onStop,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(text = "Завершить")
                    }
                }
                StopwatchStatus.PAUSED -> {
                    Button(
                        onClick = onStart,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(text = "Продолжить")
                    }
                    OutlinedButton(
                        onClick = onStop,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(text = "Сохранить")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StopwatchContentPreview() {
    MaterialTheme {
        StopwatchContent(
            state = StopwatchUiState(
                status = StopwatchStatus.RUNNING,
                formattedTime = "12:34"
            ),
            onStart = {},
            onPause = {},
            onStop = {}
        )
    }
}