package com.andanana.musicplayer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import com.andanana.musicplayer.core.database.dao.MusicDao
import com.andanana.musicplayer.core.database.entity.Music
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val localMusicRepository: LocalMusicRepository,
    private val musicDao: MusicDao
) : ViewModel() {

    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val mainUiState = _mainUiState.asStateFlow()

    var syncJob: Job? = null

    init {
        syncMediaStore()

        viewModelScope.launch {
            mainUiState.collect {
                Log.d(TAG, "state $it: ")
            }
        }
    }

    fun syncMediaStore() {
        syncJob?.cancel()
        _mainUiState.value = MainUiState.Loading
        syncJob = viewModelScope.launch {
            val idList = localMusicRepository.getAllMusicMediaId()
            musicDao.insertOrIgnoreMusicEntities(
                idList.map { mediaId ->
                    Music(mediaId)
                }
            )
            _mainUiState.value = MainUiState.Ready
        }
    }
}

sealed interface MainUiState {
    object Loading : MainUiState
    object Ready : MainUiState
}
