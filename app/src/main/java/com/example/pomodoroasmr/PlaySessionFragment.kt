package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoroasmr.databinding.FragmentPlaySessionBinding

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


        binding.startSessionButton.setOnClickListener {
            viewModel.startTimer()
        }

        binding.stopSessionButton.setOnClickListener {
            viewModel.stopTimer()
        }

        return view
    }
}