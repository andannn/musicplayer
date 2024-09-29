package com.andannn.melodify.core.database.di

import android.app.Application
import androidx.room.Room
import com.andannn.melodify.core.database.LyricDao
import com.andannn.melodify.core.database.MelodifyDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSmpDataBase(
        application: Application
    ): MelodifyDataBase = Room.databaseBuilder(
        context = application,
        klass = MelodifyDataBase::class.java,
        name = "melodify_database"
    ).build()

    @Provides
    fun providesPlayListDao(
        database: MelodifyDataBase,
    ): LyricDao = database.getLyricDao()
}
