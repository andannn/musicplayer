package com.andanana.musicplayer.feature.playqueue

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.andanana.musicplayer.core.data.data.MediaStoreSource
import com.andanana.musicplayer.core.player.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayQueueViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController,
    private val mediaStoreSource: MediaStoreSource
) : ViewModel() {
//    val playQueueFlow = playerRepository.observePlayListQueue()
//        .map { uriList ->
//            uriList.map {
//                localMusicRepository.getMusicInfoById(
//                    it.lastPathSegment?.toLong() ?: error("invalid uri.")
//                ) ?: error("invalid uri.")
//            }
//        }

    val playingMediaFlow = playerController.observePlayingMedia()
}
