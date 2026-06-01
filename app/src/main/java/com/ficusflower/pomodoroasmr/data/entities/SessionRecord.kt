package com.ficusflower.pomodoroasmr.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_records")
class SessionRecord (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val date : String,
    val workDuration : Long
)