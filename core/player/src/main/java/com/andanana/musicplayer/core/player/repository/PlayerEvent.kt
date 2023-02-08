package com.andanana.musicplayer.core.player.repository

import com.andanana.musicplayer.core.model.MusicInfo

sealed interface PlayerEvent {
    data class OnPlayMusicInPlayList(val playList: List<MusicInfo>, val index: Int) : PlayerEvent
}