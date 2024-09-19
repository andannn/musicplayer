package com.andanana.musicplayer.core.player.di

import android.app.Application
import android.content.ComponentName
import androidx.media3.common.AudioAttributes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.andanana.musicplayer.core.player.MediaLibrarySource
import com.andanana.musicplayer.core.player.MediaLibrarySourceImpl
import com.andanana.musicplayer.core.player.PlayerMonitor
import com.andanana.musicplayer.core.player.PlayerMonitorImpl
import com.google.common.util.concurrent.ListenableFuture
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityRetainedScoped
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

    @Provides
    @Singleton
    fun providerMediaBrowser(application: Application): ListenableFuture<MediaBrowser> {
        return MediaBrowser.Builder(
            application,
            SessionToken(
                application,
                ComponentName(application, "com.andanana.musicplayer.core.player.PlayerService"),
            ),
        )
            .buildAsync()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface PlayerBinds {
    @Binds
    fun bindsMediaLibrarySource(musicRepository: MediaLibrarySourceImpl): MediaLibrarySource

    @Binds
    fun bindsPlayerRepository(playerRepositoryImpl: PlayerMonitorImpl): PlayerMonitor
}
