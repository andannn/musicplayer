package com.andanana.musicplayer

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.core.database.usecases.WriteDataBaseCases
import com.andanana.musicplayer.core.designsystem.DrawerItem
import com.andanana.musicplayer.core.model.RequestType
import com.andanana.musicplayer.core.model.RequestType.Companion.toRequestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val localMusicRepository: LocalMusicRepository,
    private val useCases: WriteDataBaseCases
) : ViewModel() {

    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val mainUiState = _mainUiState.asStateFlow()

    var syncJob: Job? = null

    private val interactingUri = MutableStateFlow<Uri?>(null)
    private val interactingType = interactingUri.map {
        it?.toRequestType()
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
            useCases.addFavoritePlayListEntity()

            _mainUiState.value = MainUiState.Ready
        }
    }

    fun onDrawerItemClick(item: DrawerItem) {
        when (interactingType.value) {
            RequestType.MUSIC_REQUEST -> {
                when (item) {
                    DrawerItem.ADD_TO_FAVORITE -> {
                        addMusicToFavorite(uri = interactingUri.value!!)
                    }
                    else -> {}
                }
            }
            else -> error("not impl")
        }

        clearInteractingUri()
    }

    private fun addMusicToFavorite(uri: Uri) {
        viewModelScope.launch {
            uri.lastPathSegment?.toLong()?.let { mediaId ->
                useCases.addMusicToFavorite(mediaId)
            }
        }
    }

    fun setCurrentInteractingUri(uri: Uri) {
        interactingUri.value = uri
    }

    private fun clearInteractingUri() {
        interactingUri.value = null
    }
}

sealed interface MainUiState {
    object Loading : MainUiState
    object Ready : MainUiState
}
