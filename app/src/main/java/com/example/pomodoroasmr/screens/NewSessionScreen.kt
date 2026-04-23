package com.example.pomodoroasmr.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.pomodoroasmr.R

@Composable
fun NewSessionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(text = "New Session Screen (TODO)",
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}