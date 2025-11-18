package com.example.pomodoroasmr

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pomodoroasmr.databinding.FragmentPlaySessionBinding

class PlaySessionFragment : Fragment() {

    private var _binding : FragmentPlaySessionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaySessionBinding.inflate(inflater, container, false)
        val view = binding.root

        val tickLabel = binding.tempPlaySessionTextLabel

        // Just try a code
        val timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("Осталось: ", " ${millisUntilFinished / 1000} секунд" )
                tickLabel.text = String.format(" ${millisUntilFinished / 1000} seconds left")
            }

            override fun onFinish() {
                Log.d("TAG", "Таймер завершён!")
                tickLabel.text = String.format("Timer in finished.")
            }
        }
        timer.start()
        //-end

        return view
    }
}