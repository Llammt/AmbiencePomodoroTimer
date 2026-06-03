package com.ficusflower.pomodoroasmr.infrastructure.di

import androidx.room.Room
import com.ficusflower.pomodoroasmr.data.database.AppDatabase
import com.ficusflower.pomodoroasmr.data.repository.SessionRepositoryImpl
import com.ficusflower.pomodoroasmr.domain.repository.SessionRepository
import com.ficusflower.pomodoroasmr.domain.timer.PomodoroEngine
import com.ficusflower.pomodoroasmr.features.pomodoro.TimerViewModel
import com.ficusflower.pomodoroasmr.features.statistics.StatsViewModel
import com.ficusflower.pomodoroasmr.infrastructure.audio.AudioPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "pomodoro_database"
        ).build()
    }
    single { AudioPlayer(context = get()) }

    single { get<AppDatabase>().sessionDao() }

    single<SessionRepository> { SessionRepositoryImpl(dao = get()) }

    single { PomodoroEngine(repository = get()) }

    viewModelOf(::StatsViewModel)
    viewModelOf(::TimerViewModel)
}