package com.andanana.musicplayer.core.data.model

const val ROOT_ID = "[rootID]"

const val ALL_MUSIC_ID = "[all_musicID]"
const val ALBUM_ID = "[albumID]"
const val GENRE_ID = "[genreID]"
const val ARTIST_ID = "[artistID]"
const val MINE_PLAYLIST_ID = "[mine_play_list_ID]"

private const val ALBUM_PREFIX = "album_"
private const val GENRE_PREFIX = "genre_"
private const val ARTIST_PREFIX = "artist_"
private const val MINE_PLAYLIST_PREFIX = "playlist_"

const val PLAYABLE_MEDIA_ITEM_PREFIX = "media_item_"

enum class LibraryRootCategory(
    val mediaId: String,
    val childrenPrefix: String,
    val childrenMatchRegex: String
) {
    ALL_MUSIC(
        mediaId = ALL_MUSIC_ID,
        childrenPrefix = PLAYABLE_MEDIA_ITEM_PREFIX,
        childrenMatchRegex = "$PLAYABLE_MEDIA_ITEM_PREFIX\\d+"
    ),
    ALBUM(
        mediaId = ALBUM_ID,
        childrenPrefix = ALBUM_PREFIX,
        childrenMatchRegex = "$ALBUM_PREFIX\\d+"
    ),
    ARTIST(
        mediaId = ARTIST_ID,
        childrenPrefix = ARTIST_PREFIX,
        childrenMatchRegex = "$ARTIST_PREFIX\\d+"
    ),
    MINE_PLAYLIST(
        mediaId = MINE_PLAYLIST_ID,
        childrenPrefix = MINE_PLAYLIST_PREFIX,
        childrenMatchRegex = "$MINE_PLAYLIST_PREFIX\\d+"
    );

    companion object {
        fun getMatchedChildTypeAndId(mediaId: String): Pair<LibraryRootCategory, Long>? {
            val category = values().find { mediaId.matches(Regex(it.childrenMatchRegex)) }
                ?: return null
            val id = mediaId.substringAfter(category.childrenPrefix).toLongOrNull() ?: return null

            return Pair(category, id)
        }
    }
}