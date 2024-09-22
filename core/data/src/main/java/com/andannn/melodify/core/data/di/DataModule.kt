package com.andannn.melodify.core.data.di

import android.app.Application
import com.andannn.melodify.core.data.ContentChangeFlowProvider
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.data.repository.MediaControllerRepositoryImpl
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.core.data.repository.PlayerStateRepositoryImpl
import com.andannn.melodify.core.domain.repository.SmpPreferenceRepository
import com.andannn.melodify.core.data.repository.SmpPreferenceRepositoryImpl
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
