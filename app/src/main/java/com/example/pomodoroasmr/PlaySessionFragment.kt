package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodoroasmr.ui.PomodoroAppMainTheme

class PlaySessionFragment : Fragment() {
    lateinit var viewModel: TimerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                PomodoroAppMainTheme {
                    RenderTimer(viewModel)
                }
            }
        }
    }
}

@Composable
fun RenderTimer(viewModel: TimerViewModel) {
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
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
    }
}

@Composable
fun StopSessionButton(clicked : ()-> Unit) {
    Button(onClick = clicked) {
        Text(
            text = "Stop",
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

        val textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp
        )

        when (timerState) {
            TimerState.Idle -> Text("Start", style = textStyle)
            is TimerState.Running -> Text("Pause", style = textStyle)
            is TimerState.Paused -> Text("Resume", style = textStyle)
        }
    }
}

fun formatTime(millis: Long) : String {
    val minutes = (millis / 60_000).toString().padStart(2, '0')
    val seconds = ((millis / 1_000) % 60).toString().padStart(2, '0')

    return "$minutes : $seconds"
}

fun getSessionStatus(timerState: TimerState) : String {
    return when (timerState) {
        TimerState.Idle -> "Ready"
        is TimerState.Running -> "Work"
        is TimerState.Paused -> "(Paused)"
    }
}
