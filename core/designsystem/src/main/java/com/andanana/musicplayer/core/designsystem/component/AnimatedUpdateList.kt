package com.andanana.musicplayer.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val TAG = "AnimatedUpdateList"

@Composable
fun <K : Any> AnimatedUpdateList(
    modifier: Modifier = Modifier,
    list: List<K>,
    content: @Composable (K) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        items(
            items = list,
            key = { it }
        ) { item ->
            content(item)
        }
    }
}

// private class AnimatedListState<K> {
//    private val _list: MutableList<Pair<K, MutableTransitionState<Boolean>>> = mutableStateListOf()
//    val list: List<Pair<K, MutableTransitionState<Boolean>>> = _list
//
//    fun submitList(newList: List<K>) {
//        val map = newList.associateBy(
//            keySelector = {
//                newList.indexOf(it)
//            },
//            valueTransform = {
//                _list.find { item -> item.first == it }
//                    ?: (it to MutableTransitionState(false).apply { targetState = true })
//            }
//        )
//        val newSortedList = map.toSortedMap().map { it.value }
//
//        // Remove item.
//        run {
//            val iterator = _list.listIterator()
//            while (iterator.hasNext()) {
//                val item = iterator.next()
//                if (!newSortedList.contains(item)) {
//                    item.second.targetState = false
//                }
//            }
//        }
//
//        // Add item.
//        val diffItems = newSortedList.minus(_list.toSet())
//        _list.addAll(diffItems)
//
//        // Sort the list by order.
//        _list.sortBy { newSortedList.indexOf(it) }
//    }
// }
