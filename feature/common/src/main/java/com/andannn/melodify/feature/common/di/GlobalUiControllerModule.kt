package com.andannn.melodify.feature.common.di

import com.andannn.melodify.feature.common.GlobalUiController
import com.andannn.melodify.feature.common.GlobalUiControllerImpl
import org.koin.dsl.module

val globalUiControllerModule = module {
    single<GlobalUiController> {
        GlobalUiControllerImpl(get(), get())
    }
}
