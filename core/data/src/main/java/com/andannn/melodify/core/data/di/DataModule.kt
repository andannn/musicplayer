package com.andannn.melodify.core.data.di

import com.andannn.melodify.core.data.repository.LyricRepositoryImpl
import com.andannn.melodify.core.data.repository.MediaContentObserverRepositoryImpl
import com.andannn.melodify.core.data.repository.MediaContentRepositoryImpl
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.data.repository.MediaControllerRepositoryImpl
import com.andannn.melodify.core.domain.repository.PlayerStateMonitoryRepository
import com.andannn.melodify.core.data.repository.PlayerStateMonitoryRepositoryImpl
import com.andannn.melodify.core.domain.repository.UserPreferenceRepository
import com.andannn.melodify.core.data.repository.UserPreferenceRepositoryImpl
import com.andannn.melodify.core.domain.repository.LyricRepository
import com.andannn.melodify.core.domain.repository.MediaContentObserverRepository
import com.andannn.melodify.core.domain.repository.MediaContentRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::LyricRepositoryImpl).bind(LyricRepository::class)
    singleOf(::MediaContentObserverRepositoryImpl).bind(MediaContentObserverRepository::class)
    singleOf(::MediaControllerRepositoryImpl).bind(MediaControllerRepository::class)
    singleOf(::MediaContentRepositoryImpl).bind(MediaContentRepository::class)
    singleOf(::PlayerStateMonitoryRepositoryImpl).bind(PlayerStateMonitoryRepository::class)
    singleOf(::UserPreferenceRepositoryImpl).bind(UserPreferenceRepository::class)
}
