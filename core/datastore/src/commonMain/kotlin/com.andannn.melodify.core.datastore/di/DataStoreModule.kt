package com.andannn.melodify.core.datastore.di

import com.andannn.melodify.core.datastore.UserSettingPreferences
import org.koin.core.module.Module
import org.koin.dsl.module

expect val dataStoreModule: Module

val userPreferencesModule = module {
    single<UserSettingPreferences> { UserSettingPreferences(get()) }
}