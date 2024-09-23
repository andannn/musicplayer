package com.andannn.melodify.core.player

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.guava.future
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class PlayerService : MediaLibraryService(), CoroutineScope {
    @Inject
    lateinit var playerWrapper: PlayerWrapper

    @Inject
    lateinit var mediaLibrarySource: MediaLibrarySource

    private lateinit var mediaLibrarySession: MediaLibrarySession

    private val librarySessionCallback = CustomMediaLibrarySessionCallback()

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()

    companion object {
        private const val immutableFlag = PendingIntent.FLAG_IMMUTABLE
    }

    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(application)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        playerWrapper.setPlayer(player)
        mediaLibrarySession =
            MediaLibrarySession.Builder(this, player, librarySessionCallback)
                .setSessionActivity(getSingleTopActivity())
                .build()
    }

    override fun onDestroy() {
        super.onDestroy()

        playerWrapper.setPlayer(null)
        mediaLibrarySession.release()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaLibrarySession

    inner class CustomMediaLibrarySessionCallback : MediaLibrarySession.Callback {
        override fun onGetLibraryRoot(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            params: LibraryParams?,
        ): ListenableFuture<LibraryResult<MediaItem>> =
            future {
                LibraryResult.ofItem(
                    mediaLibrarySource.getLibraryRoot(),
                    params,
                )
            }

        override fun onGetChildren(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: LibraryParams?,
        ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> =
            future {
                LibraryResult.ofItemList(
                    mediaLibrarySource.getChildren(parentId),
                    params,
                )
            }

        override fun onGetItem(
            session: MediaLibrarySession,
            browser: MediaSession.ControllerInfo,
            mediaId: String,
        ): ListenableFuture<LibraryResult<MediaItem>> =
            future {
                mediaLibrarySource.getMediaItem(mediaId)?.let {
                    LibraryResult.ofItem(it, null)
                } ?: LibraryResult.ofError(LibraryResult.RESULT_ERROR_NOT_SUPPORTED)
            }

        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>,
        ): ListenableFuture<List<MediaItem>> {
            val updatedMediaItems =
                mediaItems.map { mediaItem ->
                    mediaItem.buildUpon()
                        .setUri(mediaItem.requestMetadata.mediaUri)
                        .build()
                }
            return Futures.immediateFuture(updatedMediaItems)
        }
    }

    private fun initializeSessionAndPlayer() {

    }

    private fun getSingleTopActivity(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, Class.forName("com.andannn.melodify.MainActivity")),
            immutableFlag or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }
}
