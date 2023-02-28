package com.andanana.musicplayer.feature.library

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.model.MusicInfo
import com.andanana.musicplayer.core.model.RequestType

@Composable
fun AnimatedUpdateList(
    modifier: Modifier = Modifier,
    list: List<MusicInfo>
) {
    val listState = remember {
        AnimatedListState()
    }
    LaunchedEffect(list) {
        listState.submitList(list)
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        items(
            items = listState.list,
            key = { it.hashCode() }
        ) { item ->
            val info = item.info
            val visibleState = item.visibleState
            AnimatedVisibility(
                visibleState,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                MusicCard(
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(),
                    albumArtUri = info.albumUri,
                    title = info.title,
                    showTrackNum = false,
                    artist = info.artist,
                    trackNum = info.cdTrackNumber,
                    date = info.modifiedDate,
                    onMusicItemClick = {
//                    onAudioItemClick(musicItems, musicItems.indexOf(info))
                    },
                    onOptionButtonClick = {
//                    onShowMusicItemOption(info.contentUri)
                    }
                )
            }
        }
    }
}

private class AnimatedListState {
    var list: List<ListItem> = mutableListOf()

    class ListItem(
        val visibleState: MutableTransitionState<Boolean>,
        val info: MusicInfo
    )

    fun submitList(newList: List<MusicInfo>) {
        val map = newList.associateBy(
            keySelector = {
                newList.indexOf(it)
            },
            valueTransform = {
                list.find { item -> item.info == it }
                    ?: ListItem(
                        visibleState = MutableTransitionState(false).apply { targetState = true },
                        info = it
                    )
            }
        )
        list = map.toSortedMap().map { it.value }
    }
}
