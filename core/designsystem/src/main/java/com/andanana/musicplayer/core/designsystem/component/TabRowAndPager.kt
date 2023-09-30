package com.andanana.musicplayer.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T> TabRowAndPager(
    modifier: Modifier = Modifier,
    items: List<T>,
    tabRowContent: @Composable ColumnScope.(T) -> Unit,
    pagerContent: @Composable (T) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()
    val pageIndex = remember(key1 = pageState) {
        pageState.currentPage
    }
    var selectedPageIndex by remember { mutableStateOf(pageIndex) }
    val slectedItem = remember(selectedPageIndex) { items[selectedPageIndex] }

    LaunchedEffect(Unit) {
        snapshotFlow {
            pageState.currentPage
        }.collect { index ->
            selectedPageIndex = index
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedPageIndex
        ) {
            items.forEach { item ->
                Tab(
                    selected = item == slectedItem,
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(items.indexOf(item))
                        }
                    }
                ) {
                    tabRowContent(item)
                }
            }
        }

        HorizontalPager(
            count = items.size,
            state = pageState
        ) { index ->
            pagerContent(items[index])
        }
    }
}
