package com.ficusflower.pomodoroasmr.data.database
import androidx.room.*
import com.ficusflower.pomodoroasmr.data.entities.SessionRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: SessionRecord)

    @Query("SELECT * FROM session_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<SessionRecord>>

    @Query("SELECT SUM(workDuration) FROM session_records")
        fun getTotalWorkDuration(): Flow<Long?>

    @Query("SELECT SUM(workDuration) FROM session_records WHERE date = :formattedDate")
    fun getWorkDurationForDate(formattedDate: String): Flow<Long?>
}