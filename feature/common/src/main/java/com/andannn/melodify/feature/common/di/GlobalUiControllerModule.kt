package com.andannn.melodify.feature.common.di

import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.feature.common.GlobalUiController
import com.andannn.melodify.feature.common.GlobalUiControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class GlobalUiControllerModule {

    @Provides
    @ActivityRetainedScoped
    fun providesBottomSheetModule(
        mediaControllerRepository: MediaControllerRepository,
        playerMoRepository: PlayerStateRepository,
    ): GlobalUiController = GlobalUiControllerImpl(
        playerStateRepository = playerMoRepository,
        mediaControllerRepository = mediaControllerRepository
    )
}
