package com.andanana.musicplayer.core.data.di

import com.andanana.musicplayer.core.data.repository.MusicRepository
import com.andanana.musicplayer.core.data.repository.MusicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsMusicRepository(
        musicRepository: MusicRepositoryImpl
    ): MusicRepository
}