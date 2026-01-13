package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
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

        viewModel.UIminutes.observe(viewLifecycleOwner, Observer {newValue ->
            binding.UIMinutesTextLabel.text = "$newValue"
        })

        viewModel.UIseconds.observe(viewLifecycleOwner, Observer {newValue ->
            binding.UISecundesTextLabel.text = "$newValue"
        })

        viewModel.UIPeriodTextLabel.observe(viewLifecycleOwner, Observer {newValue ->
            binding.UIPeriodTextLabel.text = "$newValue"
        })


        binding.startSessionButton.setOnClickListener {
            viewModel.startTimer()
            binding.startSessionButton.visibility = View.INVISIBLE
            binding.pauseSessionButton.visibility = View.VISIBLE
        }

        binding.stopSessionButton.setOnClickListener {
            viewModel.stopTimer()
            binding.pauseSessionButton.visibility = View.INVISIBLE
            binding.startSessionButton.visibility = View.VISIBLE
        }

        binding.pauseSessionButton.setOnClickListener {
            viewModel.pauseTimer()
            binding.pauseSessionButton.visibility = View.INVISIBLE
            binding.startSessionButton.visibility = View.VISIBLE
        }

        return view
    }
}