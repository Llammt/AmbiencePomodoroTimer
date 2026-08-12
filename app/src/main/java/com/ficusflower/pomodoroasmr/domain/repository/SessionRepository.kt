package com.ficusflower.pomodoroasmr.domain.repository

import com.ficusflower.pomodoroasmr.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun insert(session: Session)

    fun getAllSessions(): Flow<List<Session>>

    fun getTotalWorkDuration(): Flow<Long?>

    fun getDailyWorkDuration(date: String): Flow<Long?>

    fun getSessionsBetweenDates(startDate: String, endDate: String): Flow<List<Session>>
}