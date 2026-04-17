package com.example.pomodoroasmr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pomodoroasmr.ui.PomodoroAppMainTheme

class SelectSessionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PomodoroAppMainTheme {
                    view?.let {SelectSessionScreen(it)}
                }
            }
        }
    }
}

@Composable
fun SelectSessionScreen(view : View) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(R.drawable.select_session_header),
            contentDescription = "Upper corner decorator",
            modifier = Modifier
                .align(Alignment.TopEnd)
        )

        Image(
            painter = painterResource(R.drawable.select_session_footer),
            contentDescription = "Upper corner decorator",
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = stringResource(R.string.select_session_text_label),
                fontFamily = FontFamily(Font(R.font.kurale_regular)),
                fontSize = 24.sp
            )

            NewSessionButton {
                view.findNavController().navigate(R.id.action_selectSessionFragment_to_newSessionFragment)
            }

            SelectSessionButton {
                view.findNavController().navigate(R.id.action_selectSessionFragment_to_playSessionFragment)
            }

            StatisticsButton {
                view.findNavController().navigate(R.id.action_selectSessionFragment_to_statisticsFragment)
            }
        }
    }
}

@Composable
fun NewSessionButton(clicked : ()-> Unit) {
    Button(onClick = clicked) {
        Text(
            text = stringResource(R.string.new_session_button_label),
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp)
    }
}

@Composable
fun SelectSessionButton(clicked : ()-> Unit) {
    Button(onClick = clicked) {
        Text(
            text = stringResource(R.string.select_session_button_label),
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp)
    }
}

@Composable
fun StatisticsButton(clicked : ()-> Unit) {
    Button(onClick = clicked) {
        Text(
            text = stringResource(R.string.statistics_button_label),
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp)
    }
}
