package com.example.pomodoroasmr.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.pomodoroasmr.navigation.Routes

@Composable
fun SelectSessionScreen(navController: NavController) {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SelectSessionImageHeader()

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            SelectSessionTextLabel()

            NewSessionButton {
                navController
                    .navigate(Routes.NewSession.route)
            }

            PlaySessionButton {
                navController
                    .navigate(Routes.PlaySession.route)
            }

            StatisticsButton {
                navController
                    .navigate(Routes.Statistics.route)
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
fun PlaySessionButton(clicked : ()-> Unit) {
    Button(onClick = clicked) {
        Text(
            text = stringResource(R.string.play_session_button_label),
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

@Composable
fun SelectSessionTextLabel() {
    Text(
        text = stringResource(R.string.select_session_text_label),
        fontFamily = FontFamily(Font(R.font.kurale_regular)),
        fontSize = 24.sp
    )
}

@Composable
fun SelectSessionImageHeader() {
    Image(
        painter = painterResource(R.drawable.select_session_header),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.TopEnd)
    )
}
