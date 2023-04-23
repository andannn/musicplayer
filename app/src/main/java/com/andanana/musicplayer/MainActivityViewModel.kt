package com.andanana.musicplayer

import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.core.database.usecases.FAVORITE_PLAY_LIST_ID
import com.andanana.musicplayer.core.database.usecases.PlayListUseCases
import com.andanana.musicplayer.core.designsystem.DrawerItem
import com.andanana.musicplayer.core.model.RequestType
import com.andanana.musicplayer.core.model.RequestType.Companion.toRequestType
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val playerRepository: PlayerRepository,
    private val localMusicRepository: LocalMusicRepository,
    private val useCases: PlayListUseCases
) : ViewModel(), PlayerRepository by playerRepository {

    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val mainUiState = _mainUiState.asStateFlow()

    var syncJob: Job? = null

    val interactingUri = MutableStateFlow<Uri?>(null)
    private val interactingType = interactingUri.map {
        it?.toRequestType()
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private val musicInFavorite = useCases.getMusicInFavorite.invoke()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        syncMediaStore()
    }

    fun syncMediaStore() {
        syncJob?.cancel()
        _mainUiState.value = MainUiState.Loading
        syncJob = viewModelScope.launch {
            // Read all audio media id from MediaStore.
            val idList = localMusicRepository.getAllMusicMediaId()

            // Add all media id to data base.
            useCases.addMusicEntities(idList)

            // Add favorite play list entity, ignore if exist.
            useCases.addFavoritePlayListEntity(System.currentTimeMillis())

            _mainUiState.value = MainUiState.Ready
        }
    }

    fun onDrawerItemClick(item: DrawerItem) {
        val type = interactingUri.value!!.toRequestType()
        when (item) {
            DrawerItem.PLAY_NEXT -> {
                val id = interactingUri.value!!.lastPathSegment!!.toLong()
                onPlayNextClicked(id, type)
            }

            DrawerItem.ADD_TO_FAVORITE -> {
                if (type == RequestType.MUSIC_REQUEST) {
                    interactingUri.value?.lastPathSegment?.toLong()?.let {
                        addMusicToFavorite(it)
                    }

                    clearInteractingUri()
                }
            }

            DrawerItem.DELETE -> {
                val id = interactingUri.value!!.lastPathSegment!!.toLong()
                if (id == FAVORITE_PLAY_LIST_ID) {
                    return
                }
                viewModelScope.launch {
                    useCases.deletePlayList(id)
                }
            }

            else -> {}
        }
    }

    private fun onPlayNextClicked(id: Long, type: RequestType?) {
        viewModelScope.launch {
            val uris = when (type) {
                RequestType.ALBUM_REQUEST -> {
                    localMusicRepository.getMusicInfoByAlbumId(id).map { info ->
                        info.contentUri
                    }
                }

                RequestType.ARTIST_REQUEST -> {
                    localMusicRepository.getMusicInfoByArtistId(id).map { info ->
                        info.contentUri
                    }
                }

                RequestType.MUSIC_REQUEST -> {
                    localMusicRepository.getMusicInfoById(id)?.contentUri?.let {
                        listOf(it)
                    } ?: emptyList()
                }

                RequestType.PLAYLIST_REQUEST -> {
                    useCases.getMusicInPlayList(id).first().map {
                        Uri.withAppendedPath(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            it.music.mediaStoreId.toString()
                        )
                    }
                }

                else -> emptyList()
            }
            playerRepository.setPlayNext(uris)
        }
    }

    fun onToggleFavorite(uri: Uri) {
        uri.lastPathSegment?.toLong()?.let { mediaId ->
            if (musicInFavorite.value.map { it.music.mediaStoreId }.contains(mediaId)) {
                deleteMusicInFavorite(mediaId)
            } else {
                addMusicToFavorite(mediaId)
            }
        }
    }

    private fun deleteMusicInFavorite(mediaId: Long) {
        viewModelScope.launch {
            useCases.deleteMusicInFavorite(mediaId)
            _snackBarEvent.emit(SnackBarEvent.MUSIC_REMOVED)
        }
    }

    private fun addMusicToFavorite(mediaId: Long) {
        viewModelScope.launch {
            useCases.addMusicToFavorite(
                mediaId,
                addedDate = System.currentTimeMillis()
            )
            _snackBarEvent.emit(SnackBarEvent.SAVED_TO_FAVORITE_COMPLETE)
        }
    }

    fun setCurrentInteractingUri(uri: Uri) {
        interactingUri.value = uri
    }

    private fun clearInteractingUri() {
        interactingUri.value = null
    }

    fun onNewPlaylist(name: String) {
        viewModelScope.launch {
            useCases.addPlayListEntity(
                playListName = name,
                createdDate = System.currentTimeMillis()
            )
        }
    }
}

sealed interface MainUiState {
    object Loading : MainUiState
    object Ready : MainUiState
}
