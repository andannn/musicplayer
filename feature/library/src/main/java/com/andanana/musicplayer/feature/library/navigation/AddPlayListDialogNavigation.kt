package com.andanana.musicplayer.feature.library.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.andanana.musicplayer.core.model.RequestType
import com.andanana.musicplayer.core.model.RequestType.Companion.toRequestType
import com.andanana.musicplayer.feature.library.PlayListDialog

const val addPlayListDialogRoute = "play_list_dialog_route"
const val requestUriLastSegmentArg = "request_uri_lastSegment"
const val requestUriTypeArg = "request_play_list_uri"

fun NavController.navigateToAddPlayListDialog(uri: Uri) {
    this.navigate("$addPlayListDialogRoute/${uri.lastPathSegment}/${uri.toRequestType()}")
}

fun NavGraphBuilder.addPlayListDialog(onNewPlayListButtonClick: () -> Unit) {
    dialog(
        route = "$addPlayListDialogRoute/{$requestUriLastSegmentArg}/{$requestUriTypeArg}",
        arguments = listOf(
            navArgument(name = requestUriLastSegmentArg) {
                type = NavType.StringType
            },
            navArgument(name = requestUriTypeArg) {
                type = NavType.EnumType(RequestType::class.java)
            }
        )
    ) {
        PlayListDialog(
            onNewPlayListButtonClick = onNewPlayListButtonClick
        )
    }
}
