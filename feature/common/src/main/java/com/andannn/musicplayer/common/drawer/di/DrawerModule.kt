package com.andannn.musicplayer.common.drawer.di

import com.andannn.musicplayer.common.drawer.BottomSheetController
import com.andannn.musicplayer.common.drawer.BottomSheetControllerImpl
import com.andannn.musicplayer.common.drawer.BottomSheetEventSink
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DrawerModule {
    @Provides
    @Singleton
    fun provides(): BottomSheetController {
        return BottomSheetControllerImpl()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface DrawerModule2 {
    @Binds
    fun bindsDrawerSink(bottomDrawerController: BottomSheetController): BottomSheetEventSink
}
