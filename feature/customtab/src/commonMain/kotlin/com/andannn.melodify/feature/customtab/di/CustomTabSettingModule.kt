package com.andannn.melodify.feature.customtab.di

import com.andannn.melodify.feature.customtab.CustomTabSettingViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf


val customTabSettingModule = module {
    viewModelOf(::CustomTabSettingViewModel)
}