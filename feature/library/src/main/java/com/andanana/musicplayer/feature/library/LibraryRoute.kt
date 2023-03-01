package com.andanana.musicplayer.feature.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.designsystem.theme.MusicPlayerTheme

@Composable
fun LibraryRoute(
    libraryViewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by libraryViewModel.uiState.collectAsState()
    LibraryScreen(
        uiState = uiState
    )
}

@Composable
private fun LibraryScreen(
    modifier: Modifier = Modifier,
    uiState: LibraryUiState
) {
    when (uiState) {
        LibraryUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is LibraryUiState.Ready -> {
            LibraryContent(
                modifier = modifier
            )
        }
    }
}

@Composable
private fun LibraryContent(
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.padding(20.dp)) {
        Column {
            Text(
                text = "Your play list",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun LibraryContentPreview() {
    MusicPlayerTheme {
        LibraryContent()
    }
}
