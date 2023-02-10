package com.andanana.musicplayer.feature.playList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
}