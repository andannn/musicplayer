package com.andanana.melodify.core.mediastore.di

import com.andanana.melodify.core.mediastore.MediaStoreSource
import com.andanana.melodify.core.mediastore.MediaStoreSourceImpl
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
