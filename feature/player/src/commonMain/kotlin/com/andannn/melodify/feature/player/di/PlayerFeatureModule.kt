package com.andannn.melodify.feature.player.di

import com.andannn.melodify.feature.player.PlayerStateViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val playerFeatureModule = module {
    viewModelOf(::PlayerStateViewModel)
}