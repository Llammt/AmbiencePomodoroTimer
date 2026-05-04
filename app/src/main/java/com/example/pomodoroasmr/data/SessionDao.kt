package com.example.pomodoroasmr.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: SessionRecord)

    @Query("SELECT * FROM session_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<SessionRecord>>

    @Query("SELECT SUM(workDuration) FROM session_records")
        fun getTotalWorkDuration(): Flow<Long?>
}