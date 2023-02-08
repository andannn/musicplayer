package com.andanana.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme
import com.andanana.musicplayer.core.player.PlayerStateViewModel
import com.andanana.musicplayer.ui.SimpleMusicApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                SimpleMusicApp()
            }
        }
    }
}
