package com.ficusflower.pomodoroasmr.features.pomodoro

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.ficusflower.pomodoroasmr.MainDispatcherRule
import com.ficusflower.pomodoroasmr.domain.repository.FakeSessionRepository
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroConfig
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngine
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroStatus
import com.ficusflower.pomodoroasmr.infrastructure.audio.AudioPlayer
import com.ficusflower.pomodoroasmr.infrastructure.service.TimeTrackingService
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class TimerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var pomodoroEngine: PomodoroEngine
    private val context: Context = mockk(relaxed = true)
    private val audioPlayer: AudioPlayer = mockk(relaxed = true)
    private lateinit var viewModel: TimerViewModel
    private lateinit var fakeRepository: FakeSessionRepository

    @Before
    fun setUp() {
        fakeRepository = FakeSessionRepository()
        pomodoroEngine = PomodoroEngine(
            repository = fakeRepository,
            scope = kotlinx.coroutines.CoroutineScope(mainDispatcherRule.testDispatcher)
        )

        mockkStatic(ContextCompat::class)
        mockkConstructor(Intent::class)

        var capturedAction: String? = null

        every { anyConstructed<Intent>().setAction(any()) } answers {
            capturedAction = firstArg<String>()
            it.invocation.self as Intent
        }

        every { ContextCompat.startForegroundService(any(), any()) } answers {
            when (capturedAction) {
                TimeTrackingService.ACTION_START -> pomodoroEngine.start(PomodoroConfig())
                TimeTrackingService.ACTION_PAUSE -> pomodoroEngine.pause()
                TimeTrackingService.ACTION_STOP -> pomodoroEngine.stop()
            }
            Unit
        }

        viewModel = TimerViewModel(
            pomodoroEngine = pomodoroEngine,
            context = context
        )
    }

    @After
    fun tearDown() {
        if (::pomodoroEngine.isInitialized) {
            pomodoroEngine.stop()
        }
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `state reflects engine state`() = runTest(mainDispatcherRule.testDispatcher) {
        val stateBeforeStartTimer = viewModel.state.value

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        val stateAfterStartTimer = viewModel.state.first()

        viewModel.pauseTimer()

        val stateAfterPauseTimer = viewModel.state.first()

        viewModel.resumeTimer()

        val stateAfterResumeTimer = viewModel.state.first()

        viewModel.stopTimer()

        val stateAfterStopTimer = viewModel.state.first()

        viewModel.startTimer(config)

        val stateAfterStartTimer2 = viewModel.state.first()

        viewModel.stopTimer()

        val stateAfterStopTimer2 = viewModel.state.first()

        assertEquals(PomodoroStatus.IDLE, stateBeforeStartTimer.status)
        assertEquals(PomodoroStatus.RUNNING, stateAfterStartTimer.status)
        assertEquals(PomodoroStatus.PAUSED, stateAfterPauseTimer.status)
        assertEquals(PomodoroStatus.RUNNING, stateAfterResumeTimer.status)
        assertEquals(PomodoroStatus.IDLE, stateAfterStopTimer.status)
        assertEquals(PomodoroStatus.RUNNING, stateAfterStartTimer2.status)
        assertEquals(PomodoroStatus.IDLE, stateAfterStopTimer2.status)
    }

    @Test
    fun `resumeTimer when already running does nothing`() = runTest {
        backgroundScope.launch { viewModel.state.collect {} }

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        viewModel.resumeTimer()

        val stateAfterResumeRunningTimer = viewModel.state.first()

        viewModel.stopTimer()

        assertEquals(PomodoroStatus.RUNNING, stateAfterResumeRunningTimer.status)
    }

    @Test
    fun `stopTimer when timer is idle should do nothing`() = runTest(mainDispatcherRule.testDispatcher) {
        backgroundScope.launch { viewModel.state.collect {} }

        viewModel.stopTimer()

        val actualShowDialog = viewModel.showSaveDialog.value
        assertEquals(false, actualShowDialog)
    }

    @Test
    fun `stopTimer when timer is running should display save dialog`() = runTest(mainDispatcherRule.testDispatcher) {
        backgroundScope.launch { viewModel.state.collect {} }

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        viewModel.stopTimer()

        val actualShowDialog = viewModel.showSaveDialog.value
        assertEquals(true, actualShowDialog)
    }

    @Test
    fun `stopTimer when paused shows save dialog`() = runTest(mainDispatcherRule.testDispatcher) {
        backgroundScope.launch { viewModel.state.collect {} }

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        viewModel.pauseTimer()

        viewModel.stopTimer()

        val actualShowDialog = viewModel.showSaveDialog.value
        assertEquals(true, actualShowDialog)
    }

    @Test
    fun `onDiscardWorkTime should hide save dialog`() = runTest(mainDispatcherRule.testDispatcher) {
        backgroundScope.launch { viewModel.state.collect {} }

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        viewModel.stopTimer()

        viewModel.onDiscardWorkTime()

        val actualShowDialog = viewModel.showSaveDialog.value
        val actualPendingTime = pomodoroEngine.pendingWorkDuration.value

        assertEquals(false, actualShowDialog)
        assertEquals(0L, actualPendingTime)
    }

    @Test
    fun `onSaveWorkTime should save duration and hide dialog`() = runTest(mainDispatcherRule.testDispatcher) {
        backgroundScope.launch { viewModel.state.collect {} }

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        advanceTimeBy(10 * 60 * 1000L)
        runCurrent()

        viewModel.stopTimer()

        viewModel.onSaveWorkTime()

        val actualShowDialog = viewModel.showSaveDialog.value

        assertEquals(false, actualShowDialog)

        val totalFlow = fakeRepository.getTotalWorkDuration()
        val total = totalFlow.first()

        assertEquals(10 * 60 * 1000L, total)
    }

    @Test
    fun `pauseTimer should save duration correctly`() = runTest(mainDispatcherRule.testDispatcher) {
        backgroundScope.launch { viewModel.state.collect {} }

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        advanceTimeBy(10 * 60 * 1000L)
        runCurrent()

        viewModel.pauseTimer()

        advanceTimeBy(10 * 60 * 1000L)
        runCurrent()

        viewModel.resumeTimer()

        advanceTimeBy(10 * 60 * 1000L)
        runCurrent()

        viewModel.stopTimer()

        viewModel.onSaveWorkTime()

        val totalFlow = fakeRepository.getTotalWorkDuration()
        val total = totalFlow.first()

        assertEquals(20 * 60 * 1000L, total)
    }

    @Test
    fun `pendingTimeFormatted formats pending duration correctly`() = runTest(mainDispatcherRule.testDispatcher) {
        backgroundScope.launch { viewModel.state.collect {} }

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        advanceTimeBy(10 * 60 * 1000L)
        runCurrent()

        viewModel.stopTimer()

        val formatted = viewModel.pendingTimeFormatted.first()
        assertEquals("10 : 00", formatted)
    }

    @Test
    fun `startTimer sends commands to service correctly`() = runTest(mainDispatcherRule.testDispatcher) {
        backgroundScope.launch { viewModel.state.collect {} }

        val config = PomodoroConfig()
        viewModel.startTimer(config)

        viewModel.pauseTimer()

        viewModel.resumeTimer()

        viewModel.stopTimer()

        verify(exactly = 2) {
            anyConstructed<Intent>().setAction(TimeTrackingService.ACTION_START)
        }

        verify(exactly = 1) {
            anyConstructed<Intent>().setAction(TimeTrackingService.ACTION_PAUSE)
        }

        verify(exactly = 1) {
            anyConstructed<Intent>().setAction(TimeTrackingService.ACTION_STOP)
        }

        verify(atLeast = 4) {
            ContextCompat.startForegroundService(any(), any())
        }
    }
}