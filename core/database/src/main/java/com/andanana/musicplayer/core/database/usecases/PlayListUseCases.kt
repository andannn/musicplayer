package com.andanana.musicplayer.core.database.usecases

import javax.inject.Inject

data class PlayListUseCases @Inject constructor(
    val addMusicToPlayList: AddMusicToPlayList,
    val addMusicToFavorite: AddMusicToFavorite,
    val addMusicsToPlayList: AddMusicsToPlayList,
    val addMusicEntities: AddMusicEntities,
    val addFavoritePlayListEntity: AddFavoritePlayListEntity,
    val getMusicInFavorite: GetMusicInFavorite,
    val deleteMusicInFavorite: DeleteMusicInFavorite,
)