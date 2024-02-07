package com.andanana.musicplayer.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.andanana.musicplayer.feature.player.PlayerSheet
import com.andanana.musicplayer.navigation.SmpNavHost

@Composable
fun SimpleMusicApp(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        val navController = rememberNavController()
        SmpNavHost(
            modifier = Modifier.fillMaxWidth(),
            navHostController = navController,
        )

        PlayerSheet()
    }
}
