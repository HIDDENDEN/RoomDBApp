package com.example.m2w2b4_roomdb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;

import com.example.m2w2b4_roomdb.Database.Album;
import com.example.m2w2b4_roomdb.Database.AlbumSong;
import com.example.m2w2b4_roomdb.Database.MusicDao;
import com.example.m2w2b4_roomdb.Database.MusicDatabase;
import com.example.m2w2b4_roomdb.Database.Song;

public class MusicProvider extends ContentProvider {


    public static final String TAG = MusicProvider.class.getSimpleName();

    //constant for Authority
    private static final String Authority = "com.example.m2w2b4_roomdb_musicprovider";

    //must be the same name as name of our class Album.java
    private static final String TABLE_ALBUM = "album";
    private static final String TABLE_SONG = "song";
    private static final String TABLE_ALBUMSONG = "albumsong";

    //to know do we want to take whole table or an element of that table
    //we'll do it using URI_Matcher
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    //this code mean that we want to grab whole Album table
    private static final int ALBUM_TABLE_CODE = 100;
    private static final int SONG_TABLE_CODE = 200;
    private static final int ALBUMSONG_TABLE_CODE = 300;
    //this code mean that we want to grab row from Album table
    private static final int ALBUM_ROW_CODE = 101;
    private static final int SONG_ROW_CODE = 201;
    private static final int ALBUMSONG_ROW_CODE = 301;

    static {//add to situations
        URI_MATCHER.addURI(Authority, TABLE_ALBUM, ALBUM_TABLE_CODE);
        URI_MATCHER.addURI(Authority, TABLE_ALBUM + "/*", ALBUM_ROW_CODE);//'*' - any int number

        URI_MATCHER.addURI(Authority, TABLE_SONG, SONG_TABLE_CODE);
        URI_MATCHER.addURI(Authority, TABLE_SONG + "/*", SONG_ROW_CODE);//'*' - any int number

        URI_MATCHER.addURI(Authority, TABLE_ALBUMSONG, ALBUMSONG_TABLE_CODE);
        URI_MATCHER.addURI(Authority, TABLE_ALBUMSONG + "/*", ALBUMSONG_ROW_CODE);//'*' - any int number
    }

    private MusicDao mMusicDao;

    public MusicProvider() {
    }


    @Override
    public boolean onCreate() {
        // get access to our database Dao
        if (getContext() != null) {
            //creating database
            mMusicDao = Room.databaseBuilder(getContext().getApplicationContext(), MusicDatabase.class, "music_database")
                    .build()
                    .getMusicDao();
            return true;
        }
        return false;
    }

    @Override
    public String getType(Uri uri) {

        switch (URI_MATCHER.match(uri)) {
            case ALBUM_TABLE_CODE:
                return "vnd.android.cursor.dir/" + Authority + "." + TABLE_ALBUM;
            case ALBUM_ROW_CODE:
                return "vnd.android.cursor.item/" + Authority + "." + TABLE_ALBUM;

            case SONG_TABLE_CODE:
                return "vnd.android.cursor.dir/" + Authority + "." + TABLE_SONG;
            case SONG_ROW_CODE:
                return "vnd.android.cursor.item/" + Authority + "." + TABLE_SONG;

            case ALBUMSONG_TABLE_CODE:
                return "vnd.android.cursor.dir/" + Authority + "." + TABLE_ALBUMSONG;
            case ALBUMSONG_ROW_CODE:
                return "vnd.android.cursor.item/" + Authority + "." + TABLE_ALBUMSONG;
            default:
                throw new UnsupportedOperationException("Not yet emplemented");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int code = URI_MATCHER.match(uri);
        System.out.println("QUERRING");

        if (code != ALBUM_ROW_CODE && code != SONG_ROW_CODE && code != ALBUMSONG_ROW_CODE && code != ALBUM_TABLE_CODE && code != SONG_TABLE_CODE && code != ALBUMSONG_TABLE_CODE)
            return null;

        Cursor cursor;

        switch (code) {
            case ALBUM_TABLE_CODE:
                cursor = mMusicDao.getAlbumsCursor();
                break;

            case SONG_TABLE_CODE:
                cursor = mMusicDao.getSongsCursor();
                break;

            case ALBUMSONG_TABLE_CODE:
                cursor = mMusicDao.getAlbumSongsCursor();
                break;

            case ALBUM_ROW_CODE:
                cursor = mMusicDao.getAlbumWithIdCursor(
                        (int) ContentUris.parseId(uri)//take albumId from uri
                );
                break;

            case SONG_ROW_CODE:
                cursor = mMusicDao.getSongWithIdCursor(
                        (int) ContentUris.parseId(uri)
                );
                break;

            case ALBUMSONG_ROW_CODE:
                cursor = mMusicDao.getAlbumSongWithIdCursor(
                        (int) ContentUris.parseId(uri)
                );
                break;

            default:
                cursor = null;
                break;

        }


        return cursor;


    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {


        int code = URI_MATCHER.match(uri);
        switch (code){
            case ALBUM_TABLE_CODE:
                if (isAlbumValuesValid(values)){
                    Album album = new Album();
                    Integer id = values.getAsInteger("id");

                    album.setmId(id);
                    album.setmName(values.getAsString("name"));
                    album.setmReleaseData(values.getAsString("release"));

                    mMusicDao.insertAlbum(album);

                    return ContentUris.withAppendedId(uri, id);
                }
                else {throw new IllegalArgumentException("ContentValues for Album are incorrect");}


            case SONG_TABLE_CODE:
                if (isSongValuesValid(values)){
                    Song song = new Song();
                    Integer id = values.getAsInteger("id");

                    song.setmId(id);
                    song.setmName(values.getAsString("name"));
                    song.setmDuration(values.getAsString("duration"));

                    mMusicDao.insertSong(song);

                    return ContentUris.withAppendedId(uri,id);
                }else{throw new IllegalArgumentException("ContentValues for Song are incorrect");}

            case ALBUMSONG_TABLE_CODE:

                if (isAlbumSongValuesValid(values)){
                    AlbumSong albumSong = new AlbumSong();
                    Integer id = values.getAsInteger("id");

                    albumSong.setmId(id);
                    albumSong.setmAlbumId(values.getAsInteger("album_id"));
                    albumSong.setmSongId(values.getAsInteger("song_id"));

                    mMusicDao.insertAlbumSong(albumSong);

                    return ContentUris.withAppendedId(uri,id);
                }else{throw new IllegalArgumentException("ContentValues for AlbumSong are incorrect");}

            default:
                return null;
        }

    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int code = URI_MATCHER.match(uri);
        switch (code){
            case ALBUM_ROW_CODE:
                if (isAlbumValuesValid(values)){
                    Album album = new Album();
                    int id = (int) ContentUris.parseId(uri);

                    album.setmId(id);
                    album.setmName(values.getAsString("name"));
                    album.setmReleaseData(values.getAsString("release"));
                    System.out.println("UPDATING");
                    int updatedRows = mMusicDao.updateAlbumInfo(album);

                    return updatedRows;
                }
                else {throw new IllegalArgumentException("ContentValues for Album are incorrect");}

            case SONG_ROW_CODE:
                if (isSongValuesValid(values)){
                    Song song = new Song();
                    int id = (int) ContentUris.parseId(uri);

                    song.setmId(id);
                    song.setmName(values.getAsString("name"));
                    song.setmDuration(values.getAsString("duration"));

                    int updatedRows = mMusicDao.updateSongInfo(song);

                    return updatedRows;
                }
                else{throw new IllegalArgumentException("ContentValues for Song are incorrect");}

            case ALBUMSONG_ROW_CODE:

                if (isAlbumSongValuesValid(values)){
                    AlbumSong albumSong = new AlbumSong();
                    int id = (int) ContentUris.parseId(uri);

                    albumSong.setmId(id);
                    albumSong.setmAlbumId(values.getAsInteger("album_id"));
                    albumSong.setmSongId(values.getAsInteger("song_id"));

                    int updatedRows = mMusicDao.updateAlbumSongInfo(albumSong);

                    return updatedRows;
                }
                else{throw new IllegalArgumentException("ContentValues for AlbumSong are incorrect");}

            default:
                return 0;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int code = URI_MATCHER.match(uri);
        int id;
        switch (code){
            case ALBUM_ROW_CODE:
                id = (int) ContentUris.parseId(uri);
                return mMusicDao.deleteAlbumById(id);
            case SONG_ROW_CODE:
                id = (int) ContentUris.parseId(uri);
                return mMusicDao.deleteSongById(id);
            case ALBUMSONG_ROW_CODE:
                id = (int) ContentUris.parseId(uri);
                return mMusicDao.deleteAlbumSongById(id);
            default:
                return 0;
        }
    }

    //check what ConentValues contents
    private boolean isAlbumValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("name") && values.containsKey("release");
    }

    private boolean isSongValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("name") && values.containsKey("duration");
    }

    private boolean isAlbumSongValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("album_id") && values.containsKey("song_id");
    }
}