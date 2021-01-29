package com.example.m2w2b4_roomdb.Database;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;



@Dao
public interface MusicDao {

    //============ SINGLE INSERTS =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbum(Album albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSong(Song songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumSong(AlbumSong albumsongs);

    //============ MULTI INSERTS =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setLinksAlbumSongs(List<AlbumSong> albumSongs);

    //============ QUERY FOR ALBUM =================

    @Query("select * from album")
    List<Album> getAlbums();

    //method which returns cursor (if we want to get the whole table)
    @Query("select * from album")
    Cursor getAlbumsCursor();

    //method which returns cursor (if we want to get the row from table)
    @Query("select * from album where id = :albumId")
    Cursor getAlbumWithIdCursor(int albumId);

    //============ QUERY FOR SONG =================

    @Query("select * from song")
    List<Song> getSongs();

    //method which returns cursor (if we want to get the whole table)
    @Query("select * from song")
    Cursor getSongsCursor();

    //method which returns cursor (if we want to get the row from table)
    @Query("select * from song where id = :songId")
    Cursor getSongWithIdCursor(int songId);

    //============ QUERY FOR ALBUMSONG =================

    @Query("select * from albumsong")
    List<AlbumSong> getAlbumSongs();

    //method which returns cursor (if we want to get the whole table)
    @Query("select * from albumsong")
    Cursor getAlbumSongsCursor();

    //method which returns cursor (if we want to get the row from table)
    @Query("select * from albumsong where id = :albumSongId")
    Cursor getAlbumSongWithIdCursor(int albumSongId);

    //============ DELETE ALBUM =================
    @Delete
    void deleteAlbum(Album album);

    //delete album by ID
    @Query("DELETE FROM album where id = :albumId")
    int deleteAlbumById(int albumId);

    //============ DELETE SONG =================
    @Delete
    void deleteSong(Song song);
    //delete song by ID
    @Query("DELETE FROM song where id = :songId")
    int deleteSongById(int songId);

    //============ DELETE ALBUMSONG =================
    @Delete
    void deleteAlbumSong(AlbumSong albumSong);
    //delete AlbumSong by ID
    @Query("DELETE FROM albumsong where id = :albumSongId")
    int deleteAlbumSongById(int albumSongId);

    //============ UPDATE ALBUM =================

    //update info about album
    @Update
    int updateAlbumInfo(Album album);

    //============ UPDATE SONG =================

    //update info about song
    @Update
    int updateSongInfo(Song song);

    //============ UPDATE ALBUMSONG =================

    //update info about albumsong
    @Update
    int updateAlbumSongInfo(AlbumSong albumsong);



    // get list of songs for given album with id
    @Query("select * from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumId")
    List<Song> getSongsFromAlbum(int albumId);
}
