package com.andanana.musicplayer.core.designsystem.component

import android.util.Log
import android.util.Range
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun rememberCenterTabLayoutState(
    coroutineScope: CoroutineScope,
    scrollState: ScrollState,
    initialSelectedIndex: Int = 0
) = remember(key1 = coroutineScope, key2 = scrollState) {
    CenterTabLayoutState(coroutineScope, scrollState, initialSelectedIndex)
}

private const val TAG = "CenterTabLayoutState"

class CenterTabLayoutState(
    private val coroutineScope: CoroutineScope,
    val scrollState: ScrollState,
    private val initialSelectedIndex: Int
) {
    val centerItemIndex = MutableStateFlow(0)

    private var isScrollToInitialPosition: Boolean = false

    private var tabPositions: List<TabPosition> = emptyList()
    private var totalTabRowWidth: Int = 0
    private var visibleWidth: Int = 0

    /**
     * This function will be called every successful layout.
    S     */
    internal fun onLaidOut(
        totalTabRowWidth: Int,
        visibleWidth: Int,
        tabPositions: List<TabPosition>
    ) {
        this.tabPositions = tabPositions
        this.totalTabRowWidth = totalTabRowWidth
        this.visibleWidth = visibleWidth

        if (!isScrollToInitialPosition) {
            coroutineScope.launch {
                tabPositions.getOrNull(initialSelectedIndex)?.let { tabPosition ->
                    val offset = tabPosition.calculateTabOffset(totalTabRowWidth, visibleWidth)
                    Log.d(TAG, "scrollTo: $offset")
                    scrollState.scrollTo(offset)
                }
                isScrollToInitialPosition = true
            }
        }

        updateCenterIndex()
    }

    /**
     * launch a coroutine to scroll to center of [index] tab.
     */
    fun animateScrollToCenterOfIndex(index: Int) {
        coroutineScope.launch {
            tabPositions.getOrNull(index)?.let { tabPosition ->
                val offset = tabPosition.calculateTabOffset(totalTabRowWidth, visibleWidth)
                Log.d(TAG, "animateScrollToCenterOfIndex: $index")
                scrollState.animateScrollTo(offset)
            }
        }
    }

    private fun TabPosition.calculateTabOffset(
        totalTabRowWidth: Int,
        visibleWidth: Int
    ): Int {
        val tabOffset = left
        val tabWidth = width
        val scrollerCenter = visibleWidth / 2
        val centeredTabOffset = tabOffset - (scrollerCenter - tabWidth / 2)
        // How much space we have to scroll. If the visible width is <= to the total width, then
        // we have no space to scroll as everything is always visible.
        val availableSpace = (totalTabRowWidth - visibleWidth).coerceAtLeast(0)
        return centeredTabOffset.coerceIn(0, availableSpace)
    }

    private fun updateCenterIndex() {
        val size = tabPositions.size
        val ranges = tabPositions.mapIndexed { index, tabPosition ->
            // Center position of tab.
            val centerOffset = tabPosition.calculateTabOffset(
                totalTabRowWidth,
                visibleWidth
            )
            val halfWidth = tabPosition.width.div(2)
            when (index) {
                0 -> { // first
                    Range(0, halfWidth)
                }
                size - 1 -> { // last
                    Range(centerOffset - halfWidth, centerOffset)
                }
                else -> {
                    Range(centerOffset - halfWidth, centerOffset + halfWidth)
                }
            }
        }
        centerItemIndex.value = ranges.indexOfFirst { range ->
            scrollState.value in range
        }
    }
}

data class TabPosition(
    val left: Int,
    val width: Int
) {
    val right get() = left + width
}