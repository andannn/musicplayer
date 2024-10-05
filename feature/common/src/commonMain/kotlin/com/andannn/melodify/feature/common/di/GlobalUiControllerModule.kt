package com.andannn.melodify.feature.common.di

import com.andannn.melodify.feature.common.GlobalUiController
import com.andannn.melodify.feature.common.GlobalUiControllerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val globalUiControllerModule = module {
    singleOf(::GlobalUiControllerImpl).bind(GlobalUiController::class)
}
