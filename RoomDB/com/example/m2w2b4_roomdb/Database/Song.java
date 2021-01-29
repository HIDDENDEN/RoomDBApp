package com.example.m2w2b4_roomdb.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "name")
    public String mName;

    @ColumnInfo(name = "duration")
    public String mDuration;


    public Song() {
    }

    public Song(int mId, String mName, String mDuration) {
        this.mId = mId;
        this.mName = mName;
        this.mDuration = mDuration;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

//    @Override
//    public String toString() {
//        return "Song{" +
//                "mId=" + mId +
//                ", mName='" + mName  +
//                ", mDuration='" + mDuration +
//                '}';
//    }

    @Override
    public String toString() {
        return "SName "+mName;
    }
}
