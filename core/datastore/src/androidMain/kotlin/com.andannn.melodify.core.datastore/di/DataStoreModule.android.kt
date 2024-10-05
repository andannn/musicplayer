package com.andannn.melodify.core.datastore.di

import com.andannn.melodify.core.datastore.UserSettingPreferences
import com.andannn.melodify.core.datastore.createDataStore
import com.andannn.melodify.core.datastore.dataStoreFileName
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val userPreferencesModule = module {
    single<UserSettingPreferences> {
        val datastore = createDataStore(
            producePath = { androidContext().filesDir.resolve(dataStoreFileName).absolutePath }
        )
        UserSettingPreferences(datastore)
    }
}
