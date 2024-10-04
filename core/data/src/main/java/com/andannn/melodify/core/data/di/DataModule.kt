package com.andannn.melodify.core.data.di

import com.andannn.melodify.core.data.repository.LyricRepositoryImpl
import com.andannn.melodify.core.data.repository.MediaContentObserverRepositoryImpl
import com.andannn.melodify.core.domain.repository.MediaControllerRepository
import com.andannn.melodify.core.data.repository.MediaControllerRepositoryImpl
import com.andannn.melodify.core.domain.repository.PlayerStateRepository
import com.andannn.melodify.core.data.repository.PlayerStateRepositoryImpl
import com.andannn.melodify.core.domain.repository.UserPreferenceRepository
import com.andannn.melodify.core.data.repository.UserPreferenceRepositoryImpl
import com.andannn.melodify.core.domain.repository.LyricRepository
import com.andannn.melodify.core.domain.repository.MediaContentObserverRepository
import org.koin.dsl.module

val dataModule = module {
    single<PlayerStateRepository> { PlayerStateRepositoryImpl(get()) }
    single<UserPreferenceRepository> { UserPreferenceRepositoryImpl(get()) }
    single<MediaControllerRepository> { MediaControllerRepositoryImpl(get(), get()) }
    single<MediaContentObserverRepository> { MediaContentObserverRepositoryImpl(get()) }
    single<LyricRepository> { LyricRepositoryImpl(get(), get()) }
}
