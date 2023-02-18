package com.andanana.musicplayer.feature.library.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.andanana.musicplayer.feature.library.PlayListDialog

const val addPlayListDialogRoute = "play_list_dialog_route"

fun NavController.navigateToAddPlayListDialog() {
    this.navigate(addPlayListDialogRoute)
}

fun NavGraphBuilder.addPlayListDialog() {
    dialog(route = addPlayListDialogRoute) {
        PlayListDialog()
    }
}
