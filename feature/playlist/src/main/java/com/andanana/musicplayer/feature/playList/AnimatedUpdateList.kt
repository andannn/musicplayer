package com.andanana.musicplayer.feature.playList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andanana.musicplayer.core.model.MusicInfo

private const val TAG = "AnimatedUpdateList"

@Composable
fun AnimatedUpdateList(
    modifier: Modifier = Modifier,
    list: List<MusicInfo>,
    content: @Composable () -> Unit
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
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                content()
            }
        }
    }
}

private class AnimatedListState {
    private val _list: MutableList<ListItem> = mutableStateListOf()
    val list: List<ListItem> = _list

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

        // Remove item.
        run {
            val iterator = _list.listIterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (!newSortedList.contains(item)) {
                    item.visibleState.targetState = false
                }
            }
        }

        // Add item.
        val diffItems = newSortedList.minus(_list.toSet())
        _list.addAll(diffItems)

        // Sort the list by order.
        _list.sortBy { newSortedList.indexOf(it) }
    }
}
