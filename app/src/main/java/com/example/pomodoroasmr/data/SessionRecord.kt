package com.example.pomodoroasmr.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_records")
class SessionRecord (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val date : Long,
    val workDuration : Long
)