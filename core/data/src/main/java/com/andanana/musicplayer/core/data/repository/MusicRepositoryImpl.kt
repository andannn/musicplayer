package com.andanana.musicplayer.core.data.repository

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.andanana.musicplayer.core.data.data.MediaStoreSource
import com.andanana.musicplayer.core.data.model.AlbumModel
import com.andanana.musicplayer.core.data.model.ArtistModel
import com.andanana.musicplayer.core.data.model.LibraryRootCategory
import com.andanana.musicplayer.core.data.model.PLAYABLE_MEDIA_ITEM_PREFIX
import com.andanana.musicplayer.core.data.model.MusicModel
import com.andanana.musicplayer.core.data.model.ROOT_ID
import com.andanana.musicplayer.core.data.model.toAlbumModels
import com.andanana.musicplayer.core.data.model.toArtistModels
import com.andanana.musicplayer.core.data.model.toMusicModels
import com.andanana.musicplayer.core.database.SmpDataBase
import com.andanana.musicplayer.core.database.entity.asEntity
import com.andanana.musicplayer.core.data.model.toAlbumModel
import com.andanana.musicplayer.core.data.model.toArtistModel
import com.andanana.musicplayer.core.data.util.buildMediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.system.measureTimeMillis

private const val TAG = "MusicRepositoryImpl"

class MusicRepositoryImpl @Inject constructor(
    private val dataBase: SmpDataBase,
    private val mediaStoreSource: MediaStoreSource,
) : MusicRepository {

    override suspend fun sync() {
        val syncTime = measureTimeMillis {
            val musicEntities = mediaStoreSource.getAllMusicData().map { it.asEntity() }
            val albumEntities = mediaStoreSource.getAllAlbumData().map { it.asEntity() }
            val artistEntity = mediaStoreSource.getAllArtistData().map { it.asEntity() }

            dataBase.musicDao().clearMusicEntity()
            dataBase.musicDao().insertOrIgnoreMusicEntities(musicEntities)

            dataBase.musicDao().clearAlbumEntity()
            dataBase.musicDao().insertOrIgnoreAlbumEntities(albumEntities)

            dataBase.musicDao().clearArtistEntity()
            dataBase.musicDao().insertOrIgnoreArtistEntities(artistEntity)
        }

        Log.d(TAG, "sync: success. it takes $syncTime ms")
    }

    override fun getAllMusics(): Flow<List<MusicModel>> =
        dataBase.musicDao().getAllMusicsFlow().map { it.toMusicModels() }

    override fun getAllArtists(): Flow<List<ArtistModel>> =
        dataBase.musicDao().getAllArtistsFlow().map { it.toArtistModels() }

    override fun getAllAlbums(): Flow<List<AlbumModel>> =
        dataBase.musicDao().getAllAlbumsFlow().map { it.toAlbumModels() }

    override fun getMusicsInAlbum(albumId: Long): Flow<List<MusicModel>> =
        dataBase.musicDao().getMusicsInAlbumFlow(albumId).map { it.toMusicModels() }

    override fun getMusicsInArtist(artistId: Long): Flow<List<MusicModel>> =
        dataBase.musicDao().getMusicsInArtistFlow(artistId).map { it.toMusicModels() }

    override suspend fun getAlbumById(albumId: Long): AlbumModel =
        dataBase.musicDao().getAlbumById(albumId).toAlbumModel()

    override suspend fun getArtistById(artistId: Long): ArtistModel =
        dataBase.musicDao().getArtistById(artistId).toArtistModel()

    override fun getLibraryRoot(): MediaItem {
        return buildMediaItem(
            title = "Root Folder",
            mediaId = ROOT_ID,
            isPlayable = false,
            isBrowsable = true,
            mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED
        )
    }

    override suspend fun getChildren(mediaId: String): List<MediaItem> {
        return if (mediaId == ROOT_ID) {
            LibraryRootCategory.values().mapNotNull { category ->
                getMediaItem(category.mediaId)
            }
        } else if (mediaId == LibraryRootCategory.ALL_MUSIC.mediaId) {
// TODO:
            emptyList()
        } else if (mediaId == LibraryRootCategory.ALBUM.mediaId) {
            dataBase.musicDao().getAllAlbums().toAlbumModels().map { album ->
                buildAlbumMediaItem(album)
            }
        } else if (mediaId == LibraryRootCategory.ARTIST.mediaId) {
            dataBase.musicDao().getAllArtists().toArtistModels().map { artist ->
                buildMediaItem(
                    title = artist.name,
                    mediaId = LibraryRootCategory.ARTIST.childrenPrefix + artist.artistId,
                    isPlayable = false,
                    isBrowsable = true,
                    mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED
                )
            }
        } else if (mediaId == LibraryRootCategory.MINE_PLAYLIST.mediaId) {
// TODO:
            emptyList()
        } else if (LibraryRootCategory.getMatchedChildTypeAndId(mediaId) != null) {
            val (category, id) = LibraryRootCategory.getMatchedChildTypeAndId(mediaId)!!
            when (category) {
                LibraryRootCategory.ALL_MUSIC -> TODO()
                LibraryRootCategory.ALBUM -> {
                    dataBase.musicDao().getMusicsInAlbum(id).toMusicModels().map { music ->
                        buildMusicMediaItem(music)
                    }
                }

                LibraryRootCategory.ARTIST -> TODO()
                LibraryRootCategory.MINE_PLAYLIST -> TODO()
            }
        } else {
// TODO:
            emptyList()
        }
    }


    override suspend fun getMediaItem(mediaId: String): MediaItem? {
        return if (mediaId == ROOT_ID) {
            buildMediaItem(
                title = "ROOT",
                mediaId = mediaId,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED
            )
        } else if (mediaId in LibraryRootCategory.values().map { it.mediaId }) {
            buildMediaItem(
                title = mediaId,
                mediaId = mediaId,
                isPlayable = false,
                isBrowsable = true,
                mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED
            )
        } else if (LibraryRootCategory.getMatchedChildTypeAndId(mediaId) != null) {
            val (category, id) = LibraryRootCategory.getMatchedChildTypeAndId(mediaId)!!
            when (category) {
                LibraryRootCategory.ALL_MUSIC -> TODO()
                LibraryRootCategory.ALBUM -> {
                    dataBase.musicDao().getAlbumById(id).toAlbumModel().let { album ->
                        buildAlbumMediaItem(album)
                    }
                }

                LibraryRootCategory.ARTIST -> TODO()
                LibraryRootCategory.MINE_PLAYLIST -> TODO()
            }
        } else {
            null
        }
    }

    private fun buildAlbumMediaItem(album: AlbumModel): MediaItem = buildMediaItem(
        title = album.title,
        mediaId = LibraryRootCategory.ALBUM.childrenPrefix + album.albumId,
        imageUri = album.albumUri,
        totalTrackCount = album.trackCount,
        isPlayable = false,
        isBrowsable = true,
        mediaType = MediaMetadata.MEDIA_TYPE_FOLDER_MIXED
    )


    private fun buildMusicMediaItem(music: MusicModel): MediaItem = buildMediaItem(
        title = music.title,
        sourceUri = music.contentUri,
        mediaId = PLAYABLE_MEDIA_ITEM_PREFIX + music.id,
        imageUri = music.albumUri,
        totalTrackCount = music.cdTrackNumber,
        trackNumber = music.discNumberIndex,
        album = music.album,
        artist = music.artist,
        isPlayable = false,
        isBrowsable = true,
        mediaType = MediaMetadata.MEDIA_TYPE_MUSIC
    )
}