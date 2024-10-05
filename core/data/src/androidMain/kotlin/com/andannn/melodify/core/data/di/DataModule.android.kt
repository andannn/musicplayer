package com.andannn.melodify.core.data.di

import com.andannn.melodify.core.data.MediaContentObserverRepository
import com.andannn.melodify.core.data.MediaContentRepository
import com.andannn.melodify.core.data.MediaControllerRepository
import com.andannn.melodify.core.data.PlayerStateMonitoryRepository
import com.andannn.melodify.core.data.repository.MediaContentObserverRepositoryImpl
import com.andannn.melodify.core.data.repository.MediaContentRepositoryImpl
import com.andannn.melodify.core.data.repository.MediaControllerRepositoryImpl
import com.andannn.melodify.core.data.repository.PlayerStateMonitoryRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformDataModule = module {
    singleOf(::MediaContentObserverRepositoryImpl).bind(MediaContentObserverRepository::class)
    singleOf(::MediaControllerRepositoryImpl).bind(MediaControllerRepository::class)
    singleOf(::MediaContentRepositoryImpl).bind(MediaContentRepository::class)
    singleOf(::PlayerStateMonitoryRepositoryImpl).bind(PlayerStateMonitoryRepository::class)
}