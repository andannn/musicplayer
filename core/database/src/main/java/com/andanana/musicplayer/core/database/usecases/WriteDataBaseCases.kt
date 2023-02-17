package com.andanana.musicplayer.core.database.usecases

import javax.inject.Inject

data class WriteDataBaseCases @Inject constructor(
    val addMusicToPlayList: AddMusicToPlayList,
    val addMusicToFavorite: AddMusicToFavorite,
    val addMusicsToPlayList: AddMusicsToPlayList,
    val addMusicEntities: AddMusicEntities,
    val addFavoritePlayListEntity: AddFavoritePlayListEntity
)