package com.andanana.musicplayer.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.andanana.musicplayer.core.data.repository.LocalMusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localMusicRepository: LocalMusicRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel()
