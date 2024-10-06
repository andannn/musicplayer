package com.andannn.melodify.feature.home.di

import com.andannn.melodify.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeFeatureModule = module {
    viewModelOf(::HomeViewModel)
}
