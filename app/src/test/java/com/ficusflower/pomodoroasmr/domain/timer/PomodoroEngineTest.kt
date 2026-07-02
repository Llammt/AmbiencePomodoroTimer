package com.ficusflower.pomodoroasmr.domain.timer

import com.ficusflower.pomodoroasmr.domain.repository.FakeSessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PomodoroEngineTest {
    private lateinit var fakeRepository: FakeSessionRepository
    private lateinit var engine: PomodoroEngine
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        fakeRepository = FakeSessionRepository()
        engine = PomodoroEngine(
            repository = fakeRepository,
            scope = testScope
        )
    }

    @Test
    fun `initial state should be IDLE`() = runTest {
        val initialState = engine.state.value
        assertEquals(PomodoroStatus.IDLE, initialState.status)
    }

    @Test
    fun `startTimer should change status to RUNNING and set initial time`() = runTest(testDispatcher) {
        val config = PomodoroConfig(workDurationMillis = 10000L)
        engine.start(config)

        val state = engine.state.value
        engine.stop()

        assertEquals(PomodoroStatus.RUNNING, state.status)
        assertEquals(10000L, state.millisLeft)
    }

    @Test
    fun `pauseTimer should change status to PAUSED`() = runTest(testDispatcher) {
        engine.start(PomodoroConfig())

        engine.pause()
        val state = engine.state.value

        assertEquals(PomodoroStatus.PAUSED, state.status)
        engine.stop()
    }

    @Test
    fun `stopTimer should reset state and set initial time`() = runTest(testDispatcher) {
        engine.start(PomodoroConfig())
        engine.stop()
        val state = engine.state.value
        assertEquals(PomodoroStatus.IDLE, state.status)
        assertEquals(25 * 60 * 1000L, state.millisLeft)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `timer finishing naturally should update buffer and change period`() = runTest(testDispatcher) {
        val config = PomodoroConfig(workDurationMillis = 25 * 60 * 1000L, shortBreakDurationMillis = 5 * 60 * 1000L)
        engine.start(config)

        advanceTimeBy(26 * 60 * 1000L)
        runCurrent()

        val state = engine.state.value
        val workDuration = engine.pendingWorkDuration.value
        val pomodoroPeriod = state.period.label

        assertEquals("Short Break", pomodoroPeriod)
        assertEquals(4 * 60 * 1000L, state.millisLeft)
        assertEquals(25 * 60 * 1000L, workDuration)
        assertEquals(engine.cycleCount, 1)

        engine.stop()
    }

    @Test
    fun `stopTimer after 0 minutes should save 0 to work duration`() = runTest(testDispatcher) {
        engine.start(PomodoroConfig())
        engine.stop()
        val workDuration = engine.pendingWorkDuration.value
        assertEquals(0L, workDuration)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `stopTimer after 10 minutes should save 10 minutes to buffer`() = runTest(testDispatcher) {
        val config = PomodoroConfig(workDurationMillis = 25 * 60 * 1000L)
        engine.start(config)
        advanceTimeBy(10 * 60 * 1000L)
        runCurrent()
        engine.stop()

        val workDuration = engine.pendingWorkDuration.value
        assertEquals(10 * 60 * 1000L, workDuration)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `break period shouldn't save to work duration`() = runTest(testDispatcher) {
        val config = PomodoroConfig(workDurationMillis = 25 * 60 * 1000L, shortBreakDurationMillis = 5 * 60 * 1000L)
        engine.start(config)

        advanceTimeBy(36 * 60 * 1000L)
        runCurrent()
        engine.stop()

        val workDuration = engine.pendingWorkDuration.value

        assertEquals(31 * 60 * 1000L, workDuration)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `work duration should accumulate`() = runTest(testDispatcher) {
        val config = PomodoroConfig(workDurationMillis = 25 * 60 * 1000L)
        engine.start(config)

        advanceTimeBy(10 * 60 * 1000L)
        runCurrent()
        engine.stop()

        engine.start(config)
        advanceTimeBy(5 * 60 * 1000L)
        runCurrent()
        engine.stop()

        val workDuration = engine.pendingWorkDuration.value

        assertEquals(15 * 60 * 1000L, workDuration)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `work duration should reset correctly`() = runTest(testDispatcher) {
        val config = PomodoroConfig(workDurationMillis = 25 * 60 * 1000L)
        engine.start(config)

        advanceTimeBy(15 * 60 * 1000L)
        runCurrent()
        engine.stop()

        engine.resetWorkDuration()
        val workDuration = engine.pendingWorkDuration.value

        assertEquals(0L, workDuration)
    }
}