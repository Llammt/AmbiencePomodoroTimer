package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.pomodoroasmr.databinding.FragmentSelectSessionBinding

class SelectSessionFragment : Fragment() {
    private var _binding : FragmentSelectSessionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectSessionBinding.inflate(inflater, container, false)
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