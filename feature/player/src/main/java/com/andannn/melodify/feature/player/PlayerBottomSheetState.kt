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
    var sheetItems by mutableStateOf(SheetTab.entries.toTypedArray())

    var selectedTab by mutableStateOf(SheetTab.NEXT_SONG)

    val selectedIndex: Int
        get() = sheetItems.indexOf(selectedTab)

    fun onSelectItem(item: SheetTab) {
        selectedTab = item
    }
}