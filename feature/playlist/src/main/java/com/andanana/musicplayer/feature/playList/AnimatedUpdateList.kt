package com.andanana.musicplayer.feature.playList

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.designsystem.component.MusicCard
import com.andanana.musicplayer.core.model.MusicInfo
import kotlinx.coroutines.delay

private const val TAG = "AnimatedUpdateList"

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
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            listState.submitList(
                listState._list.toMutableList().apply { removeAt(0) }.map { it.info }.also {
                    Log.d(TAG, "AnimatedUpdateList: ${it.map { it.title }}")
                }
            )
        }
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        items(
            items = listState._list,
            key = { it.hashCode() }
        ) { item ->
            val info = item.info
            val visibleState = item.visibleState
            AnimatedVisibility(
                visibleState,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
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
    val _list: MutableList<ListItem> = mutableStateListOf()

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
                _list.find { item -> item.info == it }
                    ?: ListItem(
                        visibleState = MutableTransitionState(false).apply { targetState = true },
                        info = it
                    )
            }
        )
        val newSortedList = map.toSortedMap().map { it.value }

        val iterator = _list.listIterator()
//        while (iterator.hasNext()) {
//            val item = iterator.next()
//            if (!newSortedList.contains(item)) {
//                item.visibleState.targetState = false
//            }
//        }
        newSortedList.forEach {
            val item = iterator.next()

        }
    }
}
