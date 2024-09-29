package com.andannn.melodify.core.data.di

import com.andannn.melodify.core.data.repository.LyricRepositoryImpl
import com.andannn.melodify.core.data.repository.MediaContentObserverRepositoryImpl
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.data.repository.MediaControllerRepositoryImpl
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.core.data.repository.PlayerStateRepositoryImpl
import com.andannn.melodify.core.domain.repository.SmpPreferenceRepository
import com.andannn.melodify.core.data.repository.SmpPreferenceRepositoryImpl
import com.andannn.melodify.core.domain.repository.LyricRepository
import com.andannn.melodify.core.domain.repository.MediaContentObserverRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsPlayerStateRepository(musicRepository: PlayerStateRepositoryImpl): PlayerStateRepository

    @Binds
    fun bindsSmpPreferenceRepository(musicRepository: SmpPreferenceRepositoryImpl): SmpPreferenceRepository

    @Binds
    fun bindsMediaContentsRepository(mediaContentsRepository: MediaControllerRepositoryImpl): MediaControllerRepository

    @Binds
    fun bindsMediaContentObserverRepository(mediaContentsRepository: MediaContentObserverRepositoryImpl): MediaContentObserverRepository

    @Binds
    fun bindsLyricRepository(lyricRepository: LyricRepositoryImpl): LyricRepository
}

