package com.andanana.musicplayer

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class SmpNotificationManager @Inject constructor(
    private val application: Application
) {
    val notificationManager =
        application.getSystemService(Application.NOTIFICATION_SERVICE) as NotificationManager

    init {
        val requiredChannels = listOf(
            NotificationChannel(
                PlayerService.PLAYER_SERVICE,
                "Player",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        notificationManager.createNotificationChannels(requiredChannels)
    }

    private val activityIntent = PendingIntent.getActivity(
        application,
        0,
        Intent(application, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE
    )
    private val previousAction = NotificationCompat.Action.Builder(
        IconCompat.createWithResource(
            application,
            com.andanana.musicplayer.core.designsystem.R.drawable.music_music_player_player_previous_icon
        ),
        "Previous",
        PendingIntent.getBroadcast(
            application,
            1,
            Intent().putExtra(
                "control",
                ""
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
    ).build()

    private val cancelAction = NotificationCompat.Action.Builder(
        IconCompat.createWithResource(
            application,
            R.mipmap.ic_launcher
        ),
        "Close",
        PendingIntent.getBroadcast(
            application,
            2,
            Intent(application.packageName).putExtra(
                "control",
                1
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
    ).build()

    private val nextAction = NotificationCompat.Action.Builder(
        IconCompat.createWithResource(application, R.mipmap.ic_launcher),
        "Next",
        PendingIntent.getBroadcast(
            application,
            12,
            Intent(application.packageName).putExtra(
                "control",
                12
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
    ).build()
    private val pauseAction = NotificationCompat.Action.Builder(
        IconCompat.createWithResource(application, R.mipmap.ic_launcher),
        "Pause",
        PendingIntent.getBroadcast(
            application,
            18,
            Intent(application.packageName).putExtra(
                "control",
                15
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
    ).build()
    private val playAction = NotificationCompat.Action.Builder(
        IconCompat.createWithResource(application, R.mipmap.ic_launcher),
        "Play",
        PendingIntent.getBroadcast(
            application,
            13,
            Intent(application.packageName).putExtra(
                "control",
                13
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
    ).build()
    private val outlinedLikeAction = NotificationCompat.Action.Builder(
        IconCompat.createWithResource(application, R.mipmap.ic_launcher),
        "Like",
        PendingIntent.getBroadcast(
            application,
            15,
            Intent(application.packageName).putExtra(
                "control",
                14
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
    ).build()
    private val filledLikeAction = NotificationCompat.Action.Builder(
        IconCompat.createWithResource(application, R.mipmap.ic_launcher),
        "Unlike",
        PendingIntent.getBroadcast(
            application,
            17,
            Intent(application.packageName).putExtra(
                "control",
                15
            ),
            PendingIntent.FLAG_IMMUTABLE
        )
    ).build()

    fun getPlayerNotification(
        session: MediaSessionCompat
    ): Notification {
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setMediaSession(session.sessionToken)
        return NotificationCompat.Builder(application, PlayerService.PLAYER_SERVICE).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Now Playing")
            setContentText("")
            setOngoing(false)
            priority = NotificationCompat.PRIORITY_MAX
            setSilent(true)
            setStyle(mediaStyle)
//            addAction(if (isLiked) filledLikeAction else outlinedLikeAction)
            addAction(filledLikeAction)
            addAction(previousAction)
            addAction(playAction)
            addAction(nextAction)
            addAction(cancelAction)
            setContentIntent(activityIntent) // use with android:launchMode="singleTask" in manifest
        }.build()
    }
}
