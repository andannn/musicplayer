package com.andannn.melodify.di

import com.andannn.melodify.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainActivityViewModel(get(), get())
    }
}
