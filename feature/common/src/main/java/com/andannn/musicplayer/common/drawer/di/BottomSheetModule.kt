package com.andannn.musicplayer.common.drawer.di

import androidx.media3.session.MediaBrowser
import com.andanana.musicplayer.core.data.repository.PlayerStateRepository
import com.andannn.musicplayer.common.drawer.BottomSheetController
import com.andannn.musicplayer.common.drawer.BottomSheetControllerImpl
import com.google.common.util.concurrent.ListenableFuture
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
class BottomSheetModule {
    @Provides
    fun providesBottomSheetModule(
        browserFuture: ListenableFuture<MediaBrowser>,
        playerMoRepository: PlayerStateRepository,
    ): BottomSheetController = BottomSheetControllerImpl(
        browserFuture = browserFuture,
        playerMoRepository = playerMoRepository
    )
}