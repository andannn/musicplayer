package com.andannn.melodify.core.database.di

import com.andannn.melodify.core.database.LyricDao
import com.andannn.melodify.core.database.MelodifyDataBase
import org.koin.core.module.Module
import org.koin.dsl.module

internal val daoModule = module {
    single<LyricDao> { get<MelodifyDataBase>().getLyricDao() }
}

internal expect val platformDatabaseModule: Module

val databaseModule = listOf(
    daoModule,
    platformDatabaseModule,
)
