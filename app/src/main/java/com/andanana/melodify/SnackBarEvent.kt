package com.andanana.melodify

enum class SnackBarEvent(
    val message: String,
    val actionLabel: String? = null
) {
    SAVED_TO_FAVORITE_COMPLETE(
        message = "Saved to favorite."
    ),
    SAVED_TO_PLAYLIST_COMPLETE(
        message = "Saved to play list."
    ),
    MUSIC_REMOVED(
        message = "Music removed"
    ),
}
