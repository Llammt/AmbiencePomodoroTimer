package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.pomodoroasmr.ui.PomodoroAppMainTheme

class NewSessionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PomodoroAppMainTheme {
                    NewSessionScreen()
                }
            }
        }
    }
}

@Composable
fun NewSessionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(text = "New Session Screen",
            fontFamily = FontFamily(Font(R.font.kurale_regular)),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
            )
    }
}