package com.andannn.melodify.core.mediastore.di

import com.andannn.melodify.core.mediastore.MediaStoreSource
import com.andannn.melodify.core.mediastore.MediaStoreSourceImpl
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
