package com.example.pomodoroasmr.data

import kotlinx.coroutines.flow.Flow

class SessionRepository(private val dao: SessionDao) {
    suspend fun insert(session: SessionRecord) {
        dao.insert(session)
    }

    fun getAllSessions() : Flow<List<SessionRecord>> = dao.getAllRecords()

    fun getTotalWorkDuration() : Flow<Long?> = dao.getTotalWorkDuration()
}