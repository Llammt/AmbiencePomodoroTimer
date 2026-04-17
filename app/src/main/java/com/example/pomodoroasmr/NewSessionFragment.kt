package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.pomodoroasmr.databinding.FragmentNewSessionBinding

class NewSessionFragment : Fragment() {
    private var _binding : FragmentNewSessionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentNewSessionBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tempNewSessionButton.setOnClickListener() {
            val action = NewSessionFragmentDirections.actionNewSessionFragmentToPlaySessionFragment()
            view.findNavController().navigate(action)
        }

        return view
    }
}