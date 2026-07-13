package com.ficusflower.pomodoroasmr.domain.audio

import org.junit.Assert.assertEquals
import org.junit.Test

class AudioMappingTest {

    @Test
    fun mapIndex_0_returnsSilence() {
        val result = mapIndexToAudioMode(0)
        assertEquals(AudioMode.Silence, result)
    }

    @Test
    fun mapIndex_2_returnsSpringForestAmbient() {
        val result = mapIndexToAudioMode(2)
        val expected = AudioMode.Ambient(AmbientMode.SpringForest)

        assertEquals(expected, result)
    }

    @Test
    fun mapIndex_invalidIndex_returnsDefaultSessionEndAlert() {
        val result = mapIndexToAudioMode(-1)
        assertEquals(AudioMode.SessionEndAlert, result)

        val resultLarge = mapIndexToAudioMode(999)
        assertEquals(AudioMode.SessionEndAlert, resultLarge)
    }
}