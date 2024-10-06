package com.andannn.melodify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andannn.melodify.feature.common.GlobalUiController
import com.andannn.melodify.feature.common.BottomSheetStateProvider
import com.andannn.melodify.feature.common.DeleteMediaItemEventProvider
import com.andannn.melodify.core.player.MediaBrowserManager
import com.andannn.melodify.feature.common.UiEvent
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel(
    private val controller: GlobalUiController,
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
                Timber.tag(TAG).d("connect error:  $e")
                _state.value = MainUiState.Error(e)
            }
        }
    }

    fun onRequestDismissSheet(event: UiEvent) {
        viewModelScope.launch {
            controller.onEvent(event)
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

