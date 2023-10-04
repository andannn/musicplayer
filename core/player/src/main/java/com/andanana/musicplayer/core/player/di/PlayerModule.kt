package com.andanana.musicplayer.core.player.di

import android.app.Application
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.andanana.musicplayer.core.player.repository.PlayerController
import com.andanana.musicplayer.core.player.repository.PlayerControllerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayerModule {

    @Provides
    @Singleton
    fun providePlayer(
        application: Application
    ): Player {
        return ExoPlayer.Builder(application).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface PlayerBins {
    @Binds
    fun bindsPlayerRepository(
        playerRepositoryImpl: PlayerControllerImpl
    ): PlayerController
}
