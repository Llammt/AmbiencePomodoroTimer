package com.ficusflower.pomodoroasmr.data.repository

import com.ficusflower.pomodoroasmr.data.database.SessionDao
import com.ficusflower.pomodoroasmr.data.entities.SessionRecord
import com.ficusflower.pomodoroasmr.domain.model.Session
import com.ficusflower.pomodoroasmr.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionRepositoryImpl(private val dao: SessionDao) : SessionRepository {

    override suspend fun insert(session: Session) {
        dao.insert(session.toEntity())
    }

    override fun getAllSessions(): Flow<List<Session>> {
        return dao.getAllRecords().map { records ->
            records.map { it.toDomain() }
        }
    }

    override fun getTotalWorkDuration(): Flow<Long?> = dao.getTotalWorkDuration()

    override fun getDailyWorkDuration(date: String): Flow<Long?> = dao.getWorkDurationForDate(date)

    override fun getSessionsBetweenDates(startDate: String, endDate: String): Flow<List<Session>> {
        return dao.getSessionsBetweenDates(startDate, endDate).map { records ->
            records.map { it.toDomain() }
        }
    }
}

fun Session.toEntity() = SessionRecord(id = id, workDuration = workDuration, date = date)
fun SessionRecord.toDomain() = Session(id = id, workDuration = workDuration, date = date)