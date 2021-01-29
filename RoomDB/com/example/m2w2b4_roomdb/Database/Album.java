package com.example.m2w2b4_roomdb.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Album {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "name")
    public String mName;

    @ColumnInfo(name = "release")
    public String mReleaseData;

    public Album() {
    }

    public Album(int mId, String mName, String mReleaseData) {
        this.mId = mId;
        this.mName = mName;
        this.mReleaseData = mReleaseData;
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

    public String getmReleaseData() {
        return mReleaseData;
    }

    public void setmReleaseData(String mReleaseData) {
        this.mReleaseData = mReleaseData;
    }

    //    @Override
//    public String toString() {
//        return "Album{" +
//                "mId=" + mId +
//                ", mName='" + mName  +
//                ", mReleaseData='" + mReleaseData  +
//                '}';
//    }
    @Override
    public String toString() {
        return "AlName "+mName;
    }
}
