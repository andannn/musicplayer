package com.andannn.melodify.core.player.di

import com.andannn.melodify.core.player.MediaBrowserManager
import com.andannn.melodify.core.player.MediaBrowserManagerImpl
import com.andannn.melodify.core.player.MediaLibrarySource
import com.andannn.melodify.core.player.MediaLibrarySourceImpl
import com.andannn.melodify.core.player.PlayerWrapper
import com.andannn.melodify.core.player.PlayerWrapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlayerBinds {
    @Binds
    fun bindsMediaLibrarySource(musicRepository: MediaLibrarySourceImpl): MediaLibrarySource

    @Binds
    fun bindsPlayerWrapper(playerRepositoryImpl: PlayerWrapperImpl): PlayerWrapper

    @Binds
    fun bindsMediaBrowserManager(playerRepositoryImpl: MediaBrowserManagerImpl): MediaBrowserManager
}
