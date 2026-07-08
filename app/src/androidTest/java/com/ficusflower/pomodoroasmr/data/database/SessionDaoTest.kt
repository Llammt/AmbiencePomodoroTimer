package com.ficusflower.pomodoroasmr.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ficusflower.pomodoroasmr.data.database.AppDatabase
import com.ficusflower.pomodoroasmr.data.database.SessionDao
import com.ficusflower.pomodoroasmr.data.entities.SessionRecord
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import kotlinx.coroutines.test.runTest

@RunWith(AndroidJUnit4::class)
class SessionDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: SessionDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.sessionDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetAllRecords_orderedByDateDesc() = runTest {
        val record1 = SessionRecord(date = "2026-03-01", workDuration = 1500L)
        val record2 = SessionRecord(date = "2026-03-03", workDuration = 2000L)
        val record3 = SessionRecord(date = "2026-03-02", workDuration = 1800L)

        dao.insert(record1)
        dao.insert(record2)
        dao.insert(record3)

        val allRecords = dao.getAllRecords().first()

        assertEquals(3, allRecords.size)
        assertEquals("2026-03-03", allRecords[0].date)
        assertEquals("2026-03-02", allRecords[1].date)
        assertEquals("2026-03-01", allRecords[2].date)
    }

    @Test
    fun getTotalWorkDuration_whenTableIsEmpty_returnsNull() = runTest {
        val total = dao.getTotalWorkDuration().first()
        assertNull(total)
    }

    @Test
    fun getTotalWorkDuration_accumulatesCorrectly() = runTest {
        dao.insert(SessionRecord(date = "2026-03-01", workDuration = 1000L))
        dao.insert(SessionRecord(date = "2026-03-02", workDuration = 2500L))

        val total = dao.getTotalWorkDuration().first()

        assertEquals(3500L, total)
    }

    @Test
    fun getWorkDurationForDate_filtersAndSumsCorrectly() = runTest {
        val targetDate = "2026-03-05"
        val otherDate = "2026-03-06"

        dao.insert(SessionRecord(date = targetDate, workDuration = 1000L))
        dao.insert(SessionRecord(date = targetDate, workDuration = 1500L))
        dao.insert(SessionRecord(date = otherDate, workDuration = 5000L))

        val durationForTargetDate = dao.getWorkDurationForDate(targetDate).first()
        val durationForNonExistentDate = dao.getWorkDurationForDate("2026-01-01").first()

        assertEquals(2500L, durationForTargetDate)
        assertNull(durationForNonExistentDate)
    }
}