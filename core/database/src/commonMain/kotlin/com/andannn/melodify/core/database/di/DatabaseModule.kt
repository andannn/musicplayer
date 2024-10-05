package com.andannn.melodify.core.database.di

import org.koin.core.module.Module

expect val databaseModule: Module

//val databaseModule = module {
//    single<MelodifyDataBase> {
//        Room.databaseBuilder(
//            context = androidContext(),
//            klass = MelodifyDataBase::class.java,
//            name = "melodify_database"
//        ).build()
//    }
//
//    single<LyricDao> { get<MelodifyDataBase>().getLyricDao() }
//}
