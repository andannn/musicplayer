package com.andannn.melodify.core.datastore.di

import com.andannn.melodify.core.datastore.UserSettingPreferences
import com.andannn.melodify.core.datastore.createDataStore
import com.andannn.melodify.core.datastore.dataStoreFileName
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSDocumentDirectory

@OptIn(ExperimentalForeignApi::class)
actual val userPreferencesModule = module {
    single<UserSettingPreferences> {
        val datastore = createDataStore(
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/$dataStoreFileName"
            }
        )
        UserSettingPreferences(datastore)
    }
}
