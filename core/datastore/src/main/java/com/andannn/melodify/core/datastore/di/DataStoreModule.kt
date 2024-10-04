package com.andannn.melodify.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.andannn.melodify.core.datastore.UserPreferences
import com.andannn.melodify.core.datastore.UserPreferencesSerializer
import com.andannn.melodify.core.datastore.UserSettingPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule =
    module {
        single<DataStore<UserPreferences>> {
            DataStoreFactory.create(
                serializer = UserPreferencesSerializer(),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            ) {
                androidContext().dataStoreFile("user_preferences.pb")
            }
        }

        single<UserSettingPreferences> { UserSettingPreferences(get()) }
    }