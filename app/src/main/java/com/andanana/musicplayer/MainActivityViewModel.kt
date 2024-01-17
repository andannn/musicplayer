package com.andanana.musicplayer

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.ContentChangeFlowProvider
import com.andanana.musicplayer.core.data.model.MusicListType
import com.andanana.musicplayer.core.designsystem.DrawerItem
import com.andanana.musicplayer.core.player.PlayerMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        private val playerMonitor: PlayerMonitor,
        private val contentChangeFlowProvider: ContentChangeFlowProvider,
    ) : ViewModel(), PlayerMonitor by playerMonitor {
        private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
        val mainUiState = _mainUiState.asStateFlow()

        val interactingUri = MutableStateFlow<Uri?>(null)
        private val interactingType =
            interactingUri.map {
                null
//        it?.toRequestType()
            }
                .stateIn(viewModelScope, SharingStarted.Eagerly, null)

        private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>()
        val snackBarEvent = _snackBarEvent.asSharedFlow()

        fun onDrawerItemClick(item: DrawerItem) {
//        val type = interactingUri.value!!.toRequestType()
//        when (item) {
//            DrawerItem.PLAY_NEXT -> {
//                val id = interactingUri.value!!.lastPathSegment!!.toLong()
//                onPlayNextClicked(id, type)
//            }
//
//            DrawerItem.ADD_TO_FAVORITE -> {
//                if (type == MusicListType.MUSIC_REQUEST) {
//                    interactingUri.value?.lastPathSegment?.toLong()?.let {
//                        addMusicToFavorite(it)
//                    }
//
//                    clearInteractingUri()
//                }
//            }
//
//            DrawerItem.DELETE -> {
//                val id = interactingUri.value!!.lastPathSegment!!.toLong()
//                if (id == FAVORITE_PLAY_LIST_ID) {
//                    return
//                }
//                viewModelScope.launch {
//                    useCases.deletePlayList(id)
//                }
//            }
//
//            else -> {}
//        }
        }

        private fun onPlayNextClicked(
            id: Long,
            type: MusicListType?,
        ) {
            viewModelScope.launch {
                val uris =
                    when (type) {
                        MusicListType.ALBUM_REQUEST -> {
//                    mediaStoreSource.getMusicInfoByAlbumId(id).map { info ->
//                        info.contentUri
//                    }
                            emptyList<Uri>()
                        }

                        MusicListType.ARTIST_REQUEST -> {
//                    mediaStoreSource.getMusicInfoByArtistId(id).map { info ->
//                        info.contentUri
//                    }
                            emptyList<Uri>()
                        }

                        MusicListType.PLAYLIST_REQUEST -> {
//                    useCases.getMusicInPlayList(id).first().map {
//                        Uri.withAppendedPath(
//                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                            it.music.mediaStoreId.toString()
//                        )
//                    }
                            emptyList<Uri>()
                        }

                        else -> emptyList()
                    }
//            playerMonitor.setPlayNext(uris)
            }
        }

        fun setCurrentInteractingUri(uri: Uri) {
            interactingUri.value = uri
        }
    }

sealed interface MainUiState {
    object Loading : MainUiState

    object Ready : MainUiState
}
