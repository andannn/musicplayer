package com.andanana.musicplayer.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.andanana.musicplayer.core.database.Tables
import com.andanana.musicplayer.core.database.entity.AlbumColumns
import com.andanana.musicplayer.core.database.entity.AlbumEntity
import com.andanana.musicplayer.core.database.entity.ArtistColumns
import com.andanana.musicplayer.core.database.entity.ArtistEntity
import com.andanana.musicplayer.core.database.entity.MusicColumns
import com.andanana.musicplayer.core.database.entity.MusicEntity
import com.andanana.musicplayer.core.database.entity.PlayListColumns
import com.andanana.musicplayer.core.database.entity.PlayListWithMusics
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreMusicEntities(entities: List<MusicEntity>)

    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.id} = :id")
    suspend fun getMusicById(id: Int): MusicEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreAlbumEntities(entities: List<AlbumEntity>)

    @Query("SELECT * FROM ${Tables.album} WHERE ${AlbumColumns.id} = :id")
    suspend fun getAlbumById(id: Int): AlbumEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreArtistEntities(entities: List<ArtistEntity>)

    @Query("SELECT * FROM ${Tables.artist} WHERE ${ArtistColumns.id} = :id")
    suspend fun getArtistById(id: Int): ArtistEntity

    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.albumId} = :albumId")
    suspend fun getMusicsInAlbum(albumId: Int): List<MusicEntity>

    @Query("SELECT * FROM ${Tables.music} WHERE ${MusicColumns.artistId} = :artistId")
    suspend fun getMusicsInArtist(artistId: Int): List<MusicEntity>

    @Query("SELECT * FROM ${Tables.artist}")
    suspend fun getAllArtists(): List<ArtistEntity>

    @Query("SELECT * FROM ${Tables.album}")
    suspend fun getAllAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM ${Tables.artist}")
    suspend fun getAllArtistsFlow(): Flow<ArtistEntity>

    @Query("SELECT * FROM ${Tables.album}")
    suspend fun getAllAlbumsFlow(): Flow<AlbumEntity>

    @Transaction
    @Query("SELECT * FROM ${Tables.playList} WHERE ${PlayListColumns.id} = :playListId")
    fun getPlayListWithMusicsFlow(playListId: Long): Flow<PlayListWithMusics?>

    @Transaction
    @Query("SELECT * FROM ${Tables.playList} WHERE ${PlayListColumns.id} = :playListId")
    suspend fun getPlayListWithMusics(playListId: Long): PlayListWithMusics?
}
