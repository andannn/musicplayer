package com.andanana.musicplayer.di

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.andanana.musicplayer.SmpNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @Provides
    fun providesNotificationManager(application: Application): SmpNotificationManager {
        return SmpNotificationManager(application)
    }
}
