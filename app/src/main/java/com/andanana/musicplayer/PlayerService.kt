package com.andanana.musicplayer

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.andanana.musicplayer.core.data.repository.MusicRepository
import com.andanana.musicplayer.core.player.PlayerController
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.guava.future
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val TAG = "PlayerService"
@AndroidEntryPoint
class PlayerService : MediaLibraryService(), CoroutineScope {

    @Inject
    lateinit var player: Player

    @Inject
    lateinit var playerController: PlayerController

    @Inject
    lateinit var musicRepository: MusicRepository

    private lateinit var mediaLibrarySession: MediaLibrarySession

    private val librarySessionCallback = CustomMediaLibrarySessionCallback()

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()

    companion object {
        private const val immutableFlag = PendingIntent.FLAG_IMMUTABLE
    }

    override fun onCreate() {
        super.onCreate()
        initializeSessionAndPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()

        mediaLibrarySession.release()
        playerController.release()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaLibrarySession

    inner class CustomMediaLibrarySessionCallback : MediaLibrarySession.Callback {
        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<MediaItem>> = future {
            LibraryResult.ofItem(
                musicRepository.getLibraryRoot(),
                params
            )
        }

        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> = future {
            LibraryResult.ofItemList(
                musicRepository.getChildren(parentId),
                params
            )
        }
    }

    private fun initializeSessionAndPlayer() {
        playerController.initialize()
        mediaLibrarySession =
            MediaLibrarySession.Builder(this, player, librarySessionCallback)
                .setSessionActivity(getSingleTopActivity())
                .build()
    }

    private fun getSingleTopActivity(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            immutableFlag or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
