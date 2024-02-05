package com.andanana.musicplayer

import androidx.lifecycle.ViewModel
import com.andanana.musicplayer.core.player.PlayerMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel
    @Inject
    constructor(
        private val playerMonitor: PlayerMonitor,
    ) : ViewModel(), PlayerMonitor by playerMonitor
