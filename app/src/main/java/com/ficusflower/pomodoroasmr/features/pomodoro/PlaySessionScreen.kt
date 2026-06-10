package com.ficusflower.pomodoroasmr.features.pomodoro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ficusflower.pomodoroasmr.R
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngineState
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroPeriod
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroStatus
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlaySessionScreen(
    navController: NavController,
    viewModel: TimerViewModel = koinViewModel()
) {
    val timerState by viewModel.state.collectAsState()
    val formattedTime = formatTime(timerState.millisLeft)

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
                    onStart = { viewModel.startTimer(viewModel.pomodoroEngine.currentConfig) },
                    onPause = { viewModel.pauseTimer() },
                    onResume = { viewModel.resumeTimer() }
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
fun StartPauseSessionButton(
    timerState: PomodoroEngineState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit
) {
    Button(
        onClick = {
            when (timerState.status) {
                PomodoroStatus.IDLE -> onStart()
                PomodoroStatus.RUNNING -> onPause()
                PomodoroStatus.PAUSED -> onResume()
            }
        }
    ) {
        val buttonText = when (timerState.status) {
            PomodoroStatus.IDLE -> stringResource(R.string.start_button_text_label)
            PomodoroStatus.RUNNING -> stringResource(R.string.pause_button_text_label)
            PomodoroStatus.PAUSED -> stringResource(R.string.resume_button_text_label)
        }

        Text(
            text = buttonText,
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp
        )
    }
}

fun formatTime(millis: Long): String {
    val minutes = (millis / 60_000).toString().padStart(2, '0')
    val seconds = ((millis / 1_000) % 60).toString().padStart(2, '0')
    return "$minutes : $seconds"
}

@Composable
fun getSessionStatus(timerState: PomodoroEngineState): String {
    return when (timerState.status) {
        PomodoroStatus.IDLE -> stringResource(R.string.idle_state_text_label)
        PomodoroStatus.PAUSED -> stringResource(R.string.paused_state_text_label)
        PomodoroStatus.RUNNING -> {
            when (timerState.period) {
                is PomodoroPeriod.Work -> {
                    stringResource(R.string.work_state_text_label)
                }

                is PomodoroPeriod.ShortBreak -> {
                    stringResource(R.string.short_break_state_text_label)
                }

                is PomodoroPeriod.LongBreak -> {
                    stringResource(R.string.long_break_state_text_label)
                }
            }
        }
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
