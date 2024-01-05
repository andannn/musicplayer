package com.andanana.musicplayer.core.data.di

import android.app.Application
import android.content.ComponentName
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.andanana.musicplayer.core.data.Syncer
import com.andanana.musicplayer.core.data.SyncerImpl
import com.andanana.musicplayer.core.data.repository.MusicRepository
import com.andanana.musicplayer.core.data.repository.MusicRepositoryImpl
import com.google.common.util.concurrent.ListenableFuture
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsMusicRepository(musicRepository: MusicRepositoryImpl): MusicRepository

    @Binds
    @Singleton
    fun bindsSyncer(syncer: SyncerImpl): Syncer
}

@Module
@InstallIn(SingletonComponent::class)
object DataModuleProvider {
    @Provides
    fun providerMusicBrowser(application: Application): ListenableFuture<MediaBrowser> {
        return MediaBrowser.Builder(
            application,
            SessionToken(
                application,
                ComponentName(application, "com.andanana.musicplayer.PlayerService"),
            ),
        )
            .buildAsync()
    }
}
