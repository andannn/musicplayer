package com.andannn.melodify.core.database.di

import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.andannn.melodify.core.database.LyricDao
import com.andannn.melodify.core.database.MelodifyDataBase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule = module {
    single<MelodifyDataBase> {
        val appContext = androidContext().applicationContext
        val dbFile = appContext.getDatabasePath("melodify_database.db")
        Room.databaseBuilder<MelodifyDataBase>(
            context = appContext,
            name = dbFile.absolutePath
        )
            .setDriver(AndroidSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single<LyricDao> { get<MelodifyDataBase>().getLyricDao() }
}
