package com.example.pomodoroasmr

import org.junit.Assert.*
import org.junit.Test

class TimerViewModelTest {

    @Test
    fun `after work period comes short break`() {
        val viewModel = TimerViewModel()
        val next = viewModel.nextPeriod(TimerState.Period.Work)
        assertEquals(TimerState.Period.ShortBreak, next)
    }

    @Test
    fun `after short break comes work`() {
        val viewModel = TimerViewModel()
        val next = viewModel.nextPeriod(TimerState.Period.ShortBreak)
        assertEquals(TimerState.Period.Work, next)
    }
}