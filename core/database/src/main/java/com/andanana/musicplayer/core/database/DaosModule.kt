package com.andanana.musicplayer.core.database

import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.dao.PlayListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesPlayListDao(
        database: SmpDataBase,
    ): PlayListDao = database.playListDao()

    @Provides
    fun providesMusicDao(
        database: SmpDataBase,
    ): MusicDao = database.musicDao()
}
