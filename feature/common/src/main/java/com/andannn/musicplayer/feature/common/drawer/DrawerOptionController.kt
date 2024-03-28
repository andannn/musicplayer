package com.andannn.musicplayer.feature.common.drawer

import android.media.browse.MediaBrowser.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

data class ShowDrawerRequest(
    val mediaItem: MediaItem,
    val drawer: Drawer,
)

data class OnDrawerClickEvent(
    val mediaItem: MediaItem,
    val drawer: DrawerItem,
)

interface DrawerOptionController {


    fun onRequestDrawer(mediaItem: MediaItem)

    fun onDrawerItemClick(drawerItem: DrawerItem)

    fun observeShowDrawerEvent(): Flow<ShowDrawerRequest>

    fun observeUserClickOption(): Flow<OnDrawerClickEvent>
}

internal class DrawerOptionControllerImpl : DrawerOptionController {

    private val showDrawerRequestFlow =
        MutableSharedFlow<ShowDrawerRequest>(extraBufferCapacity = 1)
    private val onDrawerClickEventFlow =
        MutableSharedFlow<OnDrawerClickEvent>(extraBufferCapacity = 1)

    private var lastRequestedMediaItem: MediaItem? = null

    override fun onRequestDrawer(mediaItem: MediaItem) {
    }

    override fun onDrawerItemClick(drawerItem: DrawerItem) {
        lastRequestedMediaItem = null
    }

    override fun observeShowDrawerEvent(): Flow<ShowDrawerRequest> {
        TODO("Not yet implemented")
    }

    override fun observeUserClickOption(): Flow<OnDrawerClickEvent> {
        TODO("Not yet implemented")
    }

}
