package com.andannn.melodify

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andannn.melodify.common.drawer.BottomSheetController
import com.andannn.melodify.common.drawer.BottomSheetStateProvider
import com.andannn.melodify.common.drawer.DeleteMediaItemEventProvider
import com.andannn.melodify.core.player.MediaBrowserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val controller: BottomSheetController,
    private val mediaBrowserManager: MediaBrowserManager,
) : BottomSheetStateProvider by controller,
    DeleteMediaItemEventProvider by controller,
    ViewModel() {

    private val _state = MutableStateFlow<MainUiState>(MainUiState.Init)

    val state: StateFlow<MainUiState>
        get() = _state

    init {
        viewModelScope.launch {
            try {
                mediaBrowserManager.connect()
                _state.value = MainUiState.Ready
            } catch (e: TimeoutCancellationException) {
                Log.d(TAG, "connect error: $e")
                _state.value = MainUiState.Error(e)
            }
        }
    }

    override fun onCleared() {
        mediaBrowserManager.disConnect()
    }
}

sealed class MainUiState {
    data class Error(private val e: Exception) : MainUiState()

    data object Ready : MainUiState()

    data object Init : MainUiState()
}

