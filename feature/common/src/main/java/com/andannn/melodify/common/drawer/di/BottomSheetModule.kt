package com.andanana.melodify.common.drawer.di

import com.andanana.melodify.core.domain.repository.MediaControllerRepository
import com.andanana.melodify.core.domain.repository.PlayerStateRepository
import com.andanana.melodify.common.drawer.BottomSheetController
import com.andanana.melodify.common.drawer.BottomSheetControllerImpl
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