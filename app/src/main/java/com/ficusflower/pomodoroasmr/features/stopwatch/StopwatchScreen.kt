package com.ficusflower.pomodoroasmr.features.stopwatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ficusflower.pomodoroasmr.R
import com.ficusflower.pomodoroasmr.domain.engines.StopwatchStatus
import com.ficusflower.pomodoroasmr.features.pomodoro.PlaySessionImageFooter
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
        onStop = viewModel::onStopClicked,
        onConfirmSave = viewModel::onConfirmSave,
        onDiscardSave = viewModel::onDiscardSave,
        onDismissDialog = viewModel::onDismissDialog
    )
}

@Composable
fun StopwatchContent(
    state: StopwatchUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onConfirmSave: () -> Unit = {},
    onDiscardSave: () -> Unit = {},
    onDismissDialog: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StopWatchSessionImageHeader(modifier = Modifier)

            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = stringResource(id = R.string.stopwatch_text_label),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = state.formattedTime,
                fontSize = 56.sp,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(56.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StartPauseSessionButton(
                    state = state,
                    onStart = onStart,
                    onPause = onPause,
                    onResume = onStart
                )

                StopSessionButton(clicked = onStop)
            }

            Spacer(modifier = Modifier.height(56.dp))

            StopWatchSessionImageFooter(modifier = Modifier)
        }

        if (state.showSaveDialog) {
            DurationSavingDialogue(
                formattedTime = state.formattedTime,
                onConfirm = onConfirmSave,
                onDiscard = onDiscardSave,
                onDismissRequest = onDismissDialog
            )
        }
    }
}

@Composable
fun StartPauseSessionButton(
    state: StopwatchUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit
) {
    Button(
        onClick = {
            when (state.status) {
                StopwatchStatus.IDLE -> onStart()
                StopwatchStatus.RUNNING -> onPause()
                StopwatchStatus.PAUSED -> onResume()
            }
        }
    ) {
        val buttonText = when (state.status) {
            StopwatchStatus.IDLE -> stringResource(R.string.start_button_text_label)
            StopwatchStatus.RUNNING -> stringResource(R.string.pause_button_text_label)
            StopwatchStatus.PAUSED -> stringResource(R.string.resume_button_text_label)
        }

        Text(
            text = buttonText,
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp
        )
    }
}

@Composable
fun StopSessionButton(clicked: () -> Unit) {
    Button(onClick = clicked) {
        Text(
            text = stringResource(R.string.stop_button_text_label),
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp
        )
    }
}

@Composable
private fun DurationSavingDialogue(
    formattedTime: String,
    onConfirm: () -> Unit,
    onDiscard: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.pomodoro_session_if_user_want_to_save_work_time)) },
        text = {
            Text(stringResource(R.string.pomodoro_session_work_time_dialog_info, formattedTime))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.pomodoro_session_save_work_time))
            }
        },
        dismissButton = {
            TextButton(onClick = onDiscard) {
                Text(stringResource(R.string.pomodoro_session_not_save_work_time))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun StopwatchContentPreview() {
    MaterialTheme {
        StopwatchContent(
            state = StopwatchUiState(
                status = StopwatchStatus.RUNNING,
                formattedTime = "12:34",
                showSaveDialog = false
            ),
            onStart = {},
            onPause = {},
            onStop = {}
        )
    }
}

@Composable
fun StopWatchSessionImageHeader(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.play_session_footer),
        contentDescription = null,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun StopWatchSessionImageFooter(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.play_session_footer),
        contentDescription = null,
        modifier = modifier.fillMaxWidth()
    )
}