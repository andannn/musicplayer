package com.andanana.musicplayer.di

import android.app.Application
import com.andanana.musicplayer.feature_music_list.data.repository.LocalMusicRepositoryImpl
import com.andanana.musicplayer.feature_music_list.domain.repository.LocalMusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLocalMusicRepository(app: Application): LocalMusicRepository {
        return LocalMusicRepositoryImpl(app)
    }
}