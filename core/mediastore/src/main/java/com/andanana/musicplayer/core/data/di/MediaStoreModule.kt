package com.andanana.musicplayer.core.data.di

import com.andanana.musicplayer.core.data.data.MediaStoreSource
import com.andanana.musicplayer.core.data.data.MediaStoreSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MediaStoreModule {

    @Binds
    fun bindsMediaStoreSource(
        localMusicRepository: MediaStoreSourceImpl
    ): MediaStoreSource
}
