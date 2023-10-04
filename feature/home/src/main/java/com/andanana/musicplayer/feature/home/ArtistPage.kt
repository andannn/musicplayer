package com.andanana.musicplayer.feature.home

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andanana.musicplayer.core.data.model.MusicListType
import com.andanana.musicplayer.core.designsystem.component.ArtistCard

@Composable
fun ArtistPage(
    modifier: Modifier = Modifier,
    artistPageViewModel: ArtistPageViewModel = hiltViewModel(),
    onNavigateToPlayList: (mediaListId: Long, musicListType: MusicListType) -> Unit,
) {
    val state by artistPageViewModel.artistPageUiState.collectAsState()
    AlbumPageContent(
        modifier = modifier,
        state = state,
        onNavigateToPlayList = onNavigateToPlayList
    )
}

@Composable
private fun AlbumPageContent(
    modifier: Modifier = Modifier,
    state: ArtistPageUiState,
    onNavigateToPlayList: (mediaListId: Long, musicListType: MusicListType) -> Unit,
) {
    when (state) {
        ArtistPageUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is ArtistPageUiState.Ready -> {
            val musicInfoList = state.infoList

            LazyVerticalStaggeredGrid(
                modifier = modifier,
                columns = StaggeredGridCells.Fixed(2)
            ) {
                items(
                    items = musicInfoList,
                    key = { it.name }
                ) { info ->
                    ArtistCard(
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
                        artistUri = info.artistUri,
                        name = info.name,
                        trackCount = info.trackCount,
                        onClick = {
                            onNavigateToPlayList.invoke(info.artistId, MusicListType.ARTIST_REQUEST)
                        }
                    )
                }
            }
        }
    }
}
