package com.andanana.musicplayer.core.data.di

import android.app.Application
import com.andanana.musicplayer.core.data.ContentChangeFlowProvider
import com.andanana.musicplayer.core.domain.repository.MediaControllerRepository
import com.andanana.musicplayer.core.data.repository.MediaControllerRepositoryImpl
import com.andanana.musicplayer.core.domain.repository.PlayerStateRepository
import com.andanana.musicplayer.core.data.repository.PlayerStateRepositoryImpl
import com.andanana.musicplayer.core.domain.repository.SmpPreferenceRepository
import com.andanana.musicplayer.core.data.repository.SmpPreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
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
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataModuleProvider {
    @Provides
    fun provideContentChangeFlowProvider(application: Application): ContentChangeFlowProvider {
        return ContentChangeFlowProvider(application)
    }
}
