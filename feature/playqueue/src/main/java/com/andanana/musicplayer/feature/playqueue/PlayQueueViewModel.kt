package com.andanana.musicplayer.feature.playqueue

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PlayQueueViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playerRepository: PlayerRepository,
    private val localMusicRepository: LocalMusicRepository
) : ViewModel() {
    val playQueueFlow = playerRepository.observePlayListQueue()
        .map { uriList ->
            uriList.map {
                localMusicRepository.getMusicInfoById(
                    it.lastPathSegment?.toLong() ?: error("invalid uri.")
                ) ?: error("invalid uri.")
            }
        }
}
