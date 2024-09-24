package com.andannn.melodify.common.drawer.di

import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.common.drawer.BottomSheetController
import com.andannn.melodify.common.drawer.BottomSheetControllerImpl
import com.andannn.melodify.common.drawer.BottomSheetStateProvider
import com.andannn.melodify.common.drawer.DeleteMediaItemEventProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class BottomSheetModule {

    @Provides
    @ActivityRetainedScoped
    fun providesBottomSheetModule(
        mediaControllerRepository: MediaControllerRepository,
        playerMoRepository: PlayerStateRepository,
    ): BottomSheetController = BottomSheetControllerImpl(
        playerStateRepository = playerMoRepository,
        mediaControllerRepository = mediaControllerRepository
    )
}
