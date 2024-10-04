package com.andannn.melodify.core.network.di

import com.andannn.melodify.core.network.LrclibService
import com.andannn.melodify.core.network.LrclibServiceImpl
import com.andannn.melodify.core.network.lrclibResourceClientBuilder
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val serviceModule = module {
    single<LrclibService> {
        LrclibServiceImpl(
            androidApplication(),
            lrclibResourceClientBuilder(),
        )
    }
}
