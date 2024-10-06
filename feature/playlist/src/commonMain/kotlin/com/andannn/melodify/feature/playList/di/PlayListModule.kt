package com.andannn.melodify.feature.playList.di

import com.andannn.melodify.feature.playList.PlayListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val playListModule = module {
    viewModelOf(::PlayListViewModel)
}