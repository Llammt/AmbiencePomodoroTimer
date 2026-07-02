package com.ficusflower.pomodoroasmr.domain.repository

import com.ficusflower.pomodoroasmr.domain.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSessionRepository : SessionRepository {
    private val _sessions = MutableStateFlow<List<Session>>(emptyList())

    override suspend fun insert(session: Session) {
        _sessions.value += session
    }

    override fun getAllSessions(): Flow<List<Session>> {
        return _sessions
    }

    override fun getTotalWorkDuration(): Flow<Long?> {
        return _sessions.map { list -> list.sumOf { it.workDuration } }
    }

    override fun getDailyWorkDuration(date: String): Flow<Long?> {
        return _sessions.map { list ->
            list.filter { it.date == date }.sumOf { it.workDuration }
        }
    }
}