package com.example.pomodoroasmr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.pomodoroasmr.databinding.FragmentStartAnimationBinding

class StartAnimationFragment : Fragment() {
    private var _binding : FragmentStartAnimationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartAnimationBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tempStartAnimationButton.setOnClickListener() {
            val action = StartAnimationFragmentDirections.actionStartAnimationFragmentToSelectSessionFragment()
            view.findNavController().navigate(action)
        }

        return view
    }
}