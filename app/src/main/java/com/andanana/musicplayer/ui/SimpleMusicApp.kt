package com.andanana.musicplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleMusicApp() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "aaa", color = Color.Black) }
            )
        },
        bottomBar = {
            NavigationBar(
                content = {  Box(modifier = Modifier.size(10.dp).background(Color.Yellow)) }
            )
        }
    ) {
    }
}
