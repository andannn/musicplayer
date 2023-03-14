package com.andanana.musicplayer.core.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.andanana.musicplayer.core.player.repository.PlayerRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var playerRepository: PlayerRepository

    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playerRepository.initial()

        mediaSession = MediaSessionCompat(this, MEDIA_SESSION)
        val requiredChannels = listOf(
            NotificationChannel(
                PLAYER_SERVICE,
                "Player",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(requiredChannels)

        startForeground(
            10,
            getPlayerNotification(
                context = this,
                session = mediaSession
            )
        )

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        playerRepository.release()
    }

    private fun getPlayerNotification(
        context: Context,
        session: MediaSessionCompat
    ): Notification {
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(1, 2, 3)
            .setMediaSession(session.sessionToken)
        return NotificationCompat.Builder(context, PLAYER_SERVICE).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Now Playing")
            setContentText("")
            setOngoing(true)
            priority = NotificationCompat.PRIORITY_MAX
            setSilent(true)
            setStyle(mediaStyle)
//        addAction(if (isLiked) filledLikeAction else outlinedLikeAction)
//        addAction(previousAction)
//        addAction(if (showPlayButton) playAction else pauseAction)
//        addAction(nextAction)
//        addAction(cancelAction)
//        setContentIntent(activityIntent)  // use with android:launchMode="singleTask" in manifest
        }.build()
    }

    companion object {
        const val MEDIA_SESSION = "media_session"
        const val PLAYER_SERVICE = "smp_player"
    }
}
