package com.example.m2w2b4_roomdb.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(onDelete = CASCADE,entity = Album.class, parentColumns = "id", childColumns = "album_id"),
        @ForeignKey(onDelete = CASCADE,entity = Song.class, parentColumns = "id", childColumns = "song_id")})

public class AlbumSong {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "album_id")
    public int mAlbumId;

    @ColumnInfo(name = "song_id")
    public int mSongId;

    public AlbumSong() {
    }

    public AlbumSong( int mAlbumId, int mSongId) {
//        this.mId = mId;
        this.mAlbumId = mAlbumId;
        this.mSongId = mSongId;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmAlbumId() {
        return mAlbumId;
    }

    public void setmAlbumId(int mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

    public int getmSongId() {
        return mSongId;
    }

    public void setmSongId(int mSongId) {
        this.mSongId = mSongId;
    }

//    @Override
//    public String toString() {
//        return "AlbumSong{" +
//                "mId=" + mId +
//                ", mAlbumId=" + mAlbumId +
//                ", mSongId=" + mSongId +
//                '}';
//    }


    @Override
    public String toString() {
        return "AlSong "+mId;
    }
}
