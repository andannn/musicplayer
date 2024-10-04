package com.andannn.melodify.core.datastore.di

import com.andannn.melodify.core.datastore.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


actual val dataStoreModule = module {
    single {
        createDataStore(androidContext())
    }
}