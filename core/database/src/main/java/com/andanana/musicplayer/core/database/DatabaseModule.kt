package com.andanana.musicplayer.core.database

import android.app.Application
import androidx.room.Room
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
    ): SmpDataBase = Room.databaseBuilder(
        context = application,
        klass = SmpDataBase::class.java,
        name = "simple_music_player_database"
    ).build()
}
