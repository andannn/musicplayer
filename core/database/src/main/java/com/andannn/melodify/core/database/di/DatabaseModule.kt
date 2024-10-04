package com.andannn.melodify.core.database.di

import androidx.room.Room
import com.andannn.melodify.core.database.LyricDao
import com.andannn.melodify.core.database.MelodifyDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<MelodifyDataBase> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = MelodifyDataBase::class.java,
            name = "melodify_database"
        ).build()
    }

    single<LyricDao> { get<MelodifyDataBase>().getLyricDao() }
}
