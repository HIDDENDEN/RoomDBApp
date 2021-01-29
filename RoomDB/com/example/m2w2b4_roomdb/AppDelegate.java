package com.example.m2w2b4_roomdb;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.m2w2b4_roomdb.Database.MusicDatabase;

public class AppDelegate extends Application {

    MusicDatabase mMusicDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        //creating database
        mMusicDatabase= Room.databaseBuilder(getApplicationContext()
                ,MusicDatabase.class,
                "music_database")//name of database
                .allowMainThreadQueries()//to add data to db in main thread(this is bad practice)
                .build();
    }

    public MusicDatabase getmMusicDatabase() {
        return mMusicDatabase;
    }
}
