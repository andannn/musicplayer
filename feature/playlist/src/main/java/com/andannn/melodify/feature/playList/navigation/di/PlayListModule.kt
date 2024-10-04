package com.andannn.melodify.feature.playList.navigation.di

import com.andannn.melodify.feature.playList.navigation.PlayListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val playListModule = module {
    viewModelOf(::PlayListViewModel)
}