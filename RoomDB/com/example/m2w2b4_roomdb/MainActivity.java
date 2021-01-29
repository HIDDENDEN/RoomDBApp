package com.example.m2w2b4_roomdb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.m2w2b4_roomdb.Database.Album;
import com.example.m2w2b4_roomdb.Database.AlbumSong;
import com.example.m2w2b4_roomdb.Database.MusicDao;
import com.example.m2w2b4_roomdb.Database.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mAddButton;
    private Button mGetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create exemplyar of database in Application
        //get access to music Dao using getmMusicDatabase method from AppDelegate application
        MusicDao musicDao = ((AppDelegate) getApplicationContext()).getmMusicDatabase().getMusicDao();

        //adding some data in table
        mAddButton = findViewById(R.id.add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumListStore = createAlbums();
                songListStore = createSongs();
                musicDao.insertAlbums(albumListStore);
                musicDao.insertSongs(songListStore);
                musicDao.setLinksAlbumSongs(createAlbumSongs(albumListStore,songListStore));
            }
        });

        mGetButton = findViewById(R.id.get);
        mGetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(musicDao.getAlbums(), musicDao.getSongs(), musicDao.getAlbumSongs());
            }
        });
    }

    private List<Album> albumListStore = new ArrayList<>();
    private List<Song> songListStore = new ArrayList<>();

    private List<Album> createAlbums() {
        List<Album> albumList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            albumList.add(new Album(i
                    , "album " + i
                    , "release " + System.currentTimeMillis()
            ));
        }
        return albumList;
    }

    private List<Song> createSongs() {
        List<Song> songList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            songList.add(new Song(i
                    , "song " + i
                    , "duration " + System.currentTimeMillis()
            ));
        }
        return songList;
    }

    private List<AlbumSong> createAlbumSongs(List<Album> albums,List<Song> songs) {
        List<AlbumSong> albumSongList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            AlbumSong albumSong = new AlbumSong(
                    albums.get(i).mId,
                    songs.get(i).mId);

            albumSongList.add(albumSong);

        }
        return albumSongList;
    }

    private void showToast(List<Album> albums, List<Song> songs, List<AlbumSong> albumSongs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, size = albums.size(); i < size; i++) {
            String curAlbum = albums.get(i).toString();
            stringBuilder.append(curAlbum).append('\n');
        }
        Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();

//        for (int i = 0, size = albums.size(); i < size; i++) {
//            String curAlbum = albums.get(i).toString();
//            String curSong = songs.get(i).toString();
//            String curAlbumSong = albumSongs.get(i).toString();
//            stringBuilder.append(curAlbum).append(curSong).append(curAlbumSong).append('\n');
//        }
//        Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
    }
}