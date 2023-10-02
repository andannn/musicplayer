package com.andanana.musicplayer.core.data.repository

import android.util.Log
import com.andanana.musicplayer.core.data.data.MediaStoreSource
import com.andanana.musicplayer.core.data.model.AlbumModel
import com.andanana.musicplayer.core.data.model.ArtistModel
import com.andanana.musicplayer.core.data.model.MusicModel
import com.andanana.musicplayer.core.data.model.toAlbumModels
import com.andanana.musicplayer.core.data.model.toArtistModels
import com.andanana.musicplayer.core.data.model.toMusicModels
import com.andanana.musicplayer.core.database.SmpDataBase
import com.andanana.musicplayer.core.database.entity.asEntity
import com.andanana.musicplayer.core.data.model.toAlbumModel
import com.andanana.musicplayer.core.data.model.toArtistModel
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
}