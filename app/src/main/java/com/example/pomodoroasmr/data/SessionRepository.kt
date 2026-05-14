package com.example.pomodoroasmr.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class SessionRepository(private val dao: SessionDao) {
    suspend fun insert(session: SessionRecord) {
        dao.insert(session)
    }

    fun getAllSessions() : Flow<List<SessionRecord>> = dao.getAllRecords()

    fun getTotalWorkDuration() : Flow<Long?> = dao.getTotalWorkDuration()

    fun getTodaysWorkDuration() : Flow<Long?> = dao.getWorkDurationForDate(LocalDate.now().toString())
}