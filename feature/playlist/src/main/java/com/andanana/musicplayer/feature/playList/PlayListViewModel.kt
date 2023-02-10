package com.andanana.musicplayer.feature.playList

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.feature.playList.navigation.RequestType
import com.andanana.musicplayer.feature.playList.navigation.RequestType.Companion.getTypeByContentUri
import com.andanana.musicplayer.feature.playList.navigation.RequestType.Companion.toUri
import com.andanana.musicplayer.feature.playList.navigation.requestUriLastSegmentArg
import com.andanana.musicplayer.feature.playList.navigation.requestUriTypeArg
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val musicItemsFlow = requestUri.map {
        it.getTypeByContentUri()?.let { type ->
            when (type) {
                RequestType.ALBUM_REQUEST -> {
                    repository.getMusicInfoByAlbumId(it.lastPathSegment?.toLong() ?: 0L)
                }
                RequestType.ARTIST_REQUEST -> {
                    repository.getMusicInfoByArtistId(it.lastPathSegment?.toLong() ?: 0L)
                }
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
            musicItemsFlow.collect {
                Log.d(TAG, ":musicItemsFlow $it")
            }
        }
    }
}
