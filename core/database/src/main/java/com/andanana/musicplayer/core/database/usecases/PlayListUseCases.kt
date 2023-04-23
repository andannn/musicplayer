package com.andanana.musicplayer.core.database.usecases

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class PlayListUseCases @Inject constructor(
    val addMusicToPlayList: AddMusicToPlayList,
    val addMusicToFavorite: AddMusicToFavorite,
    val addMusicsToPlayList: AddMusicsToPlayList,
    val addMusicEntities: AddMusicEntities,
    val addFavoritePlayListEntity: AddFavoritePlayListEntity,
    val addPlayListEntity: AddPlayListEntity,
    val deleteMusicInFavorite: DeleteMusicInFavorite,
    val deleteMusicInPlayList: DeleteMusicInPlayList,
    val deletePlayList: DeletePlayList,
    val getMusicInFavorite: GetMusicInFavorite,
    val getMusicInPlayList: GetMusicInPlayList,
    val getAllPlayList: GetAllPlayList,
    val getPlayListsOfMusic: GetPlayListsOfMusic,
    val getPlayListByPlayListId: GetPlayListByPlayListId,
    val getAllPlayListMusicCount: GetAllPlayListMusicCount
)
