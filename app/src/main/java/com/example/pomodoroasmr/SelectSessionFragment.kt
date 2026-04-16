package com.example.pomodoroasmr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pomodoroasmr.databinding.FragmentSelectSessionBinding

class SelectSessionFragment : Fragment() {
    private var _binding : FragmentSelectSessionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectSessionBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                SelectSessionScreen()
            }
        }
        val view = binding.root

        binding.tempSelectSessionButton.setOnClickListener() {
            val action = SelectSessionFragmentDirections.actionSelectSessionFragmentToNewSessionFragment()
            view.findNavController().navigate(action)
        }

        binding.tempSelectSessionButton2.setOnClickListener() {
            val action = SelectSessionFragmentDirections.actionSelectSessionFragmentToPlaySessionFragment()
            view.findNavController().navigate(action)
        }

        binding.tempSelectSessionButton3.setOnClickListener() {
            val action = SelectSessionFragmentDirections.actionSelectSessionFragmentToStatisticsFragment()
            view.findNavController().navigate(action)
        }

        return view
    }
}

@Composable
fun SelectSessionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(R.drawable.leafs),
            contentDescription = "Upper corner decorator",
            modifier = Modifier
                .align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.select_session_text_label),
                fontFamily = FontFamily(Font(R.font.kurale_regular)),
                fontSize = 24.sp
            )

            Button(onClick = {}) {
                Text(
                    text = stringResource(R.string.new_session_button_label),
                    fontFamily = FontFamily(Font(R.font.kurale_regular)),
                    fontSize = 24.sp) }
        }
    }
}
