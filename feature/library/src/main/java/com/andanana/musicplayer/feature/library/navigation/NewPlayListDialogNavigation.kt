package com.andanana.musicplayer.feature.library.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.andanana.musicplayer.feature.library.NewPlayListDialog

const val newPlayListDialogRoute = "new_play_list_dialog_route"

fun NavController.navigateToNewPlayListDialog() {
    this.navigate(newPlayListDialogRoute)
}

fun NavGraphBuilder.newPlayListDialog(
    onNavigateBack: () -> Unit,
    onCreateButtonClick: (name: String) -> Unit
) {
    dialog(
        route = newPlayListDialogRoute
    ) {
        NewPlayListDialog(
            onNavigateBack = onNavigateBack,
            onCreateButtonClick = onCreateButtonClick
        )
    }
}
