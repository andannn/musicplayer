package com.andanana.musicplayer.core.player.di

import android.app.Application
import androidx.media3.common.AudioAttributes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.andanana.musicplayer.core.player.MediaLibrarySource
import com.andanana.musicplayer.core.player.MediaLibrarySourceImpl
import com.andanana.musicplayer.core.player.PlayerMonitor
import com.andanana.musicplayer.core.player.PlayerMonitorImpl
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
    fun providePlayer(application: Application): Player {
        return ExoPlayer.Builder(application)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface PlayerBinds {
    @Binds
    fun bindsMusicRepository(musicRepository: MediaLibrarySourceImpl): MediaLibrarySource

    @Binds
    fun bindsPlayerRepository(playerRepositoryImpl: PlayerMonitorImpl): PlayerMonitor
}
