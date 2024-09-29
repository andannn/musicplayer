package com.andannn.melodify.feature.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

enum class SheetTab {
    NEXT_SONG,
    LYRICS,
}

@Composable
fun rememberPlayerBottomSheetState() = remember {
    PlayerBottomSheetState()
}

class PlayerBottomSheetState {
    var sheetItems by mutableStateOf(listOf(SheetTab.NEXT_SONG))

    var selectedTab by mutableStateOf(SheetTab.NEXT_SONG)

    val selectedIndex: Int
        get() =
            if (sheetItems.contains(selectedTab))
                sheetItems.indexOf(selectedTab)
            else
                0

    fun onHasLyricsChange(hasLyrics: Boolean) {
        if (!hasLyrics) {
            if (selectedTab == SheetTab.LYRICS) {
                selectedTab = SheetTab.NEXT_SONG
            }
            sheetItems = listOf(SheetTab.NEXT_SONG)
        } else {
            sheetItems = SheetTab.entries
        }
    }

    fun onClickTab(item: SheetTab) {
        selectedTab = item
    }
}