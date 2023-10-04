package com.andanana.musicplayer.feature.playList

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.model.MusicModel
import com.andanana.musicplayer.core.data.model.MusicListType
import com.andanana.musicplayer.core.data.repository.MusicRepository
import com.andanana.musicplayer.core.player.repository.PlayerController
import com.andanana.musicplayer.feature.playList.navigation.MusicListIdKey
import com.andanana.musicplayer.feature.playList.navigation.MusicListTypeKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayListViewModel"

@HiltViewModel
class PlayListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playerController: PlayerController,
    private val musicRepository: MusicRepository,
) : ViewModel(), PlayerController by playerController {
    private val musicListType =
        savedStateHandle.get<MusicListType>(MusicListTypeKey) ?: MusicListType.ARTIST_REQUEST
    private val musicListId =
        savedStateHandle.get<Long>(MusicListIdKey) ?: -1

    private val _playListUiStateFlow = MutableStateFlow(PlayListUiState())
    val playListUiStateFlow = _playListUiStateFlow.asStateFlow()

    init {
        // update musics
        viewModelScope.launch {
            val musicsFLow: Flow<List<MusicModel>> = when (musicListType) {
                MusicListType.ALBUM_REQUEST -> {
                    musicRepository.getMusicsInAlbum(musicListId)
                }

                MusicListType.ARTIST_REQUEST -> {
                    musicRepository.getMusicsInArtist(musicListId)
                }

                MusicListType.PLAYLIST_REQUEST -> {
                    //TODO:
                    musicRepository.getMusicsInAlbum(musicListId)
                }
            }
            musicsFLow.distinctUntilChanged().collect { musics ->
                _playListUiStateFlow.update { lastState ->
                    lastState.copy(musicItems = musics)
                }
            }
        }

        viewModelScope.launch {
            val title = ""
            when (musicListType) {
                MusicListType.ALBUM_REQUEST -> {
                    musicRepository.getAlbumById(musicListId).title
                }

                MusicListType.ARTIST_REQUEST -> {
                    musicRepository.getArtistById(musicListId).name
                }

                MusicListType.PLAYLIST_REQUEST -> {
                    //TODO:
                    musicRepository.getAlbumById(musicListId).title
                }
            }
            _playListUiStateFlow.update { lastState ->
                lastState.copy(title = title)
            }
        }
    }

//    init {
//        viewModelScope.launch {
//            val uri = requestTypeFlow.value.toUri(requestUriLastSegmentFlow.value)
//            when (uri.toRequestType()) {
//                RequestType.ALBUM_REQUEST -> {
//                    val info = repository.getAlbumInfoById(
//                        id = uri.lastPathSegment?.toLong() ?: 0L
//                    )
//                    val title = info.title
//                    val artCoverUri = info.albumUri.toString()
//                    val trackCount = info.trackCount
//                    val musicItems = repository.getMusicInfoByAlbumId(
//                        id = uri.lastPathSegment?.toLong() ?: 0L
//                    ).sortedBy { it.cdTrackNumber }
//                    _playListUiStateFlow.value = PlayListUiState.Ready(
//                        title = title,
//                        contentUri = uri,
//                        type = uri.toRequestType()!!,
//                        artCoverUri = artCoverUri,
//                        trackCount = trackCount,
//                        musicItems = musicItems
//                    )
//                }
//                RequestType.ARTIST_REQUEST -> {
//                    val info = repository.getArtistInfoById(
//                        id = uri.lastPathSegment?.toLong() ?: 0L
//                    )
//                    val title = info.name
//                    val artCoverUri = info.artistCoverUri.toString()
//                    val trackCount = info.trackCount
//                    val musicItems = repository.getMusicInfoByArtistId(
//                        id = uri.lastPathSegment?.toLong() ?: 0L
//                    )
//                    _playListUiStateFlow.value = PlayListUiState.Ready(
//                        title = title,
//                        contentUri = uri,
//                        type = uri.toRequestType()!!,
//                        artCoverUri = artCoverUri,
//                        trackCount = trackCount,
//                        musicItems = musicItems
//                    )
//                }
//                RequestType.PLAYLIST_REQUEST -> {
//                    val playListId = uri.lastPathSegment?.toLong() ?: 0L
//
//                    val playList = useCases.getPlayListByPlayListId.invoke(
//                        playListId = playListId
//                    )
//                    val title = playList.name
//                    val artCoverUri = ""
//                    _playListUiStateFlow.value = PlayListUiState.Ready(
//                        title = title,
//                        contentUri = uri,
//                        type = uri.toRequestType()!!,
//                        artCoverUri = artCoverUri,
//                        trackCount = 0,
//                        musicItems = emptyList()
//                    )
//
//                    viewModelScope.launch {
//                        useCases.getMusicInPlayList.invoke(
//                            playListId = playListId
//                        )
//                            .map { it.sortedByDescending { it.musicAddedDate } }
//                            .map { ids ->
//                                ids.map {
//                                    repository.getMusicInfoById(
//                                        id = it.music.mediaStoreId
//                                    ) ?: MusicInfo(contentUri = Uri.parse(""))
//                                }
//                            }
//                            .collect { infos ->
//                                _playListUiStateFlow.value = playListReadyState?.copy(
//                                    trackCount = infos.size,
//                                    musicItems = infos
//                                ) ?: return@collect
//                            }
//                    }
//                }
//                else -> error("Invalid type")
//            }
//
//            playerRepository.observePlayingUri().collect { playingUri ->
//                _playListUiStateFlow.update {
//                    playListReadyState?.copy(
//                        interactingUri = playingUri
//                    ) ?: return@collect
//                }
//            }
//        }
//    }

    init {
        viewModelScope.launch {
            _playListUiStateFlow.collect {
                Log.d(TAG, ":_playListUiStateFlow $it")
            }
        }
    }
}

data class PlayListUiState(
    val title: String = "",
    val type: MusicListType = MusicListType.PLAYLIST_REQUEST,
    val artCoverUri: String = "",
    val trackCount: Int = 0,
    val musicItems: List<MusicModel> = emptyList(),
    val interactingUri: Uri? = null,
    val contentUri: Uri = Uri.EMPTY
)
