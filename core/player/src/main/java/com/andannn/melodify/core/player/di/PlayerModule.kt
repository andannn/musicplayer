package com.andannn.melodify.core.player.di

import com.andannn.melodify.core.player.MediaBrowserManager
import com.andannn.melodify.core.player.MediaBrowserManagerImpl
import com.andannn.melodify.core.player.PlayerWrapper
import com.andannn.melodify.core.player.PlayerWrapperImpl
import com.andannn.melodify.core.player.timer.SleepTimerController
import com.andannn.melodify.core.player.timer.SleepTimerControllerImpl
import org.koin.dsl.module

val playerModule = module {
    single<PlayerWrapper> { PlayerWrapperImpl() }
    single<MediaBrowserManager> { MediaBrowserManagerImpl(get()) }
    single<SleepTimerController> { SleepTimerControllerImpl() }
}
