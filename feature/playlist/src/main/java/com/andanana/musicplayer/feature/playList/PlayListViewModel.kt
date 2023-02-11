package com.andanana.musicplayer.feature.playList

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.feature.playList.navigation.RequestType
import com.andanana.musicplayer.feature.playList.navigation.RequestType.Companion.toRequestType
import com.andanana.musicplayer.feature.playList.navigation.RequestType.Companion.toUri
import com.andanana.musicplayer.feature.playList.navigation.requestUriLastSegmentArg
import com.andanana.musicplayer.feature.playList.navigation.requestUriTypeArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayListViewModel"

@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: LocalMusicRepository
) : ViewModel() {
    private val requestTypeFlow =
        savedStateHandle.getStateFlow(requestUriTypeArg, RequestType.ARTIST_REQUEST)
    private val requestUriLastSegmentFlow =
        savedStateHandle.getStateFlow(requestUriLastSegmentArg, "")

    private val requestUri = combine(
        requestTypeFlow,
        requestUriLastSegmentFlow
    ) { type, lastSegment ->
        type.toUri(lastSegment)
    }

    private val _playListUiStateFlow = MutableStateFlow<PlayListUiState>(PlayListUiState.Loading)
    val playListUiStateFlow = _playListUiStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            requestUri.collect { uri ->
                val title: String
                val artCoverUri: String
                val trackCount: Int
                val musicItems: List<MusicInfo>
                when (uri.toRequestType()) {
                    RequestType.ALBUM_REQUEST -> {
                        val info = repository.getAlbumInfoById(
                            id = uri.lastPathSegment?.toLong() ?: 0L
                        )
                        title = info.title
                        artCoverUri = info.albumUri.toString()
                        trackCount = info.trackCount
                        musicItems = repository.getMusicInfoByAlbumId(
                            id = uri.lastPathSegment?.toLong() ?: 0L
                        )
                    }
                    RequestType.ARTIST_REQUEST -> {
                        val info = repository.getArtistInfoById(
                            id = uri.lastPathSegment?.toLong() ?: 0L
                        )
                        title = info.name
                        artCoverUri = info.artistCoverUri.toString()
                        trackCount = info.trackCount
                        musicItems = repository.getMusicInfoByArtistId(
                            id = uri.lastPathSegment?.toLong() ?: 0L
                        )
                    }
                    else -> error("Invalid type")
                }
                _playListUiStateFlow.value = PlayListUiState.Ready(
                    title = title,
                    artCoverUri = artCoverUri,
                    trackCount = trackCount,
                    musicItems = musicItems
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            requestUri.collect {
                Log.d(TAG, ": $it")
            }
        }
        viewModelScope.launch {
            _playListUiStateFlow.collect {
                Log.d(TAG, ":_playListUiStateFlow $it")
            }
        }
    }
}

sealed interface PlayListUiState {
    object Loading : PlayListUiState
    data class Ready(
        val title: String,
        val artCoverUri: String,
        val trackCount: Int,
        val musicItems: List<MusicInfo>
    ) : PlayListUiState
}
