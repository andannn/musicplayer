package com.andannn.melodify.core.network.di

import com.andannn.melodify.core.network.LrclibService
import com.andannn.melodify.core.network.LrclibServiceImpl
import com.andannn.melodify.core.network.lrclibResourceClientBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providesHttpClient(
    ): LrclibService {
        return LrclibServiceImpl(
            lrclibResourceClientBuilder()
        )
    }
}
