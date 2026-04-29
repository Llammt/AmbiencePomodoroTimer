package com.example.pomodoroasmr.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pomodoroasmr.R
import com.example.pomodoroasmr.TimerState
import com.example.pomodoroasmr.TimerViewModel

@Composable
fun PlaySessionScreen(viewModel: TimerViewModel, navController: NavController) {
    val timerState by viewModel.state.collectAsState()

    val millisLeft = when (timerState) {
        TimerState.Idle -> 0L
        is TimerState.Running -> (timerState as TimerState.Running).millisLeft
        is TimerState.Paused -> (timerState as TimerState.Paused).millisLeft
    }

    val formattedTime = formatTime(millisLeft)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formattedTime,
                fontFamily = FontFamily(Font(R.font.kurale_regular)),
                fontSize = 48.sp
            )

            Text(
                text = getSessionStatus(timerState),
                fontFamily = FontFamily(Font(R.font.kurale_regular)),
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                StartPauseSessionButton(
                    timerState = timerState,
                    onStart = { viewModel.startTimer() },
                    onPause = { viewModel.pauseTimer() }
                )

                StopSessionButton {
                    viewModel.stopTimer()
                }
            }
        }
        PlaySessionImageFooter(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun StopSessionButton(clicked : ()-> Unit) {
    Button(onClick = clicked) {
        Text(
            text = stringResource(R.string.stop_button_text_label),
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp)
    }
}

@Composable
fun StartPauseSessionButton(
    timerState: TimerState,
    onStart: () -> Unit,
    onPause: () -> Unit
) {
    Button(
        onClick = {
            when (timerState) {
                TimerState.Idle -> onStart()
                is TimerState.Running -> onPause()
                is TimerState.Paused -> onStart()
            }
        }
    ) {

        val buttonText = when (timerState) {
            TimerState.Idle -> stringResource(R.string.start_button_text_label)
            is TimerState.Running -> stringResource(R.string.pause_button_text_label)
            is TimerState.Paused -> stringResource(R.string.resume_button_text_label)
        }

        Text(
            text = buttonText,
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp
        )
    }
}

fun formatTime(millis: Long) : String {
    val minutes = (millis / 60_000).toString().padStart(2, '0')
    val seconds = ((millis / 1_000) % 60).toString().padStart(2, '0')

    return "$minutes : $seconds"
}

@Composable
fun getSessionStatus(timerState: TimerState) : String {
    return when (timerState) {
        TimerState.Idle -> stringResource(R.string.idle_state_text_label)
        is TimerState.Running -> stringResource(R.string.work_state_text_label)
        is TimerState.Paused -> stringResource(R.string.paused_state_text_label)
    }
}

@Composable
fun PlaySessionImageFooter(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.play_session_footer),
        contentDescription = null,
        modifier = modifier.fillMaxWidth()
    )
}
