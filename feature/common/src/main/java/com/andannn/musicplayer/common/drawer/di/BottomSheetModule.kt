package com.andannn.musicplayer.common.drawer.di

import com.andanana.musicplayer.core.domain.repository.MediaControllerRepository
import com.andanana.musicplayer.core.domain.repository.PlayerStateRepository
import com.andannn.musicplayer.common.drawer.BottomSheetController
import com.andannn.musicplayer.common.drawer.BottomSheetControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
class BottomSheetModule {
    @Provides
    fun providesBottomSheetModule(
        mediaControllerRepository: MediaControllerRepository,
        playerMoRepository: PlayerStateRepository,
    ): BottomSheetController = BottomSheetControllerImpl(
        playerStateRepository = playerMoRepository,
        mediaControllerRepository = mediaControllerRepository
    )
}