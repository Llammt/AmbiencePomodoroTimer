package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoroasmr.databinding.FragmentPlaySessionBinding
import androidx.lifecycle.Observer

class PlaySessionFragment : Fragment() {

    private var _binding : FragmentPlaySessionBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: TimerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaySessionBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        binding.pauseSessionButton.visibility = View.INVISIBLE

        viewModel.state.observe(viewLifecycleOwner, Observer {newValue ->
            render(newValue)
        })


        binding.startSessionButton.setOnClickListener {
            viewModel.startTimer()
        }

        binding.stopSessionButton.setOnClickListener {
            viewModel.stopTimer()
        }

        binding.pauseSessionButton.setOnClickListener {
            viewModel.pauseTimer()
        }

        return view
    }

    private fun render(state: TimerState) {
        when (state) {

            TimerState.Idle -> {
                binding.UIMinutesTextLabel.text = "00"
                binding.UISecondsTextLabel.text = "00"
                binding.UIPeriodTextLabel.text = "Ready"

                binding.pauseSessionButton.visibility = View.INVISIBLE
                binding.startSessionButton.visibility = View.VISIBLE
            }

            is TimerState.Running -> {
                renderTime(state.millisLeft)
                binding.UIPeriodTextLabel.text = state.period.label

                binding.startSessionButton.visibility = View.INVISIBLE
                binding.pauseSessionButton.visibility = View.VISIBLE
            }

            is TimerState.Paused -> {
                renderTime(state.millisLeft)
                binding.UIPeriodTextLabel.text = "(Paused)"

                binding.pauseSessionButton.visibility = View.INVISIBLE
                binding.startSessionButton.visibility = View.VISIBLE
            }
        }
    }

    private fun renderTime(millis: Long) {
        val minutes = millis / 60_000
        val seconds = (millis / 1_000) % 60

        binding.UIMinutesTextLabel.text = "%02d".format(minutes)
        binding.UISecondsTextLabel.text = "%02d".format(seconds)
    }
}