package com.example.secondappforcontentprovidertest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Spinner spinnerTable;
    private Spinner spinnerAction;
    private Button mExecuteButton;
    private EditText etMainId;
    private EditText etUserId;
    private EditText etUserName;
    private EditText etUserRelease;

    private boolean deleteBlockFlag = false;
    private boolean updateBlockFlag = false;

    View.OnClickListener onExecuteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (etMainId.getText().toString().isEmpty()) {
                blockDeleteAction();
                blockUpdateAction();
            } else {
                unlockDeleteAction();
                unlockUpdateAction();
            }

            //========== Getting values from EDITTEXT
            String mainId = etMainId.getText().toString();
//            String userId = etUserId.getText().toString();
            String userName = etUserName.getText().toString();
            String userRelease = etUserRelease.getText().toString();

            ContentValues contentValues = new ContentValues();

            Cursor cursor = null;
            String tableName = spinnerTable.getSelectedItem().toString().toLowerCase();

            switch (spinnerAction.getSelectedItem().toString().toLowerCase()) {
                case "query":

                    cursor = contentResolverQuery(tableName, mainId);
                    if (cursor == null) return;

                    StringBuilder stringBuilder = new StringBuilder();

                    if (cursor.moveToFirst()) {
                        do {
                            if (tableName.equals("albumsong")) {
                                stringBuilder.append(cursor.getString(cursor.getColumnIndex("album_id"))).append(" ").append(cursor.getString(cursor.getColumnIndex("song_id"))).append("\n");

                            } else {
                                if (tableName.equals("album")) {
                                    stringBuilder.append(cursor.getString(cursor.getColumnIndex("name"))).append(" ").append(cursor.getString(cursor.getColumnIndex("release"))).append("\n");
                                } else {
                                    stringBuilder.append(cursor.getString(cursor.getColumnIndex("name"))).append(" ").append(cursor.getString(cursor.getColumnIndex("duration"))).append("\n");
                                }
                            }
                        }
                            while (cursor.moveToNext()) ;
                        }


                        Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();

                        break;
                        case "insert":
                            contentResolverInsert(tableName, mainId, userName, userRelease);

                            break;
                        case "update":
                            if (mainId.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Id field can't be empty", Toast.LENGTH_SHORT).show();
                            } else {
                                //todo
                                //need to check are written values correct
                                int id = Integer.parseInt(mainId);
                                contentResolverUpdate(tableName, id, userName, userRelease);

                            }

                            break;
                        case "delete":
                            if (mainId.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Id field can't be empty", Toast.LENGTH_SHORT).show();
                            } else {

                                int id = Integer.parseInt(mainId);
                                contentResolverDelete(tableName, id);
                            }


                            break;

//                default: throw new UnsupportedOperationException("Error in default switch/case for actions");
                    }


                    //=========++EXAMPLE++
//            contentValues.put("id", 0);
//            contentValues.put("name", "new Name");
//            contentValues.put("release", "tomorrow");
//            getContentResolver().update(Uri.parse("content://com.elegion.roomdatabase.musicprovider/album/1"), contentValues, null, null);
            }
        }

        ;

        private Cursor contentResolverQuery(String tableName, String possibleId) {
            if (possibleId.isEmpty()) {
                return getContentResolver().query(Uri.parse("content://"
                                + Authority
                                + "/" + tableName)
                        , null
                        , null
                        , null
                        , null
                        , null
                );
            } else {
                //take query by id
                try {
                    return getContentResolver().query(Uri.parse("content://"
                            + Authority
                            + "/" + tableName + "/" + possibleId), null, null, null, null, null);

                } catch (Exception e) {
                    //what type of exception will be?
                    Toast.makeText(getApplicationContext(), "Looks like there is no such id in database", Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        }

        private void contentResolverDelete(String tableName, int deletionId) {
            try {


                getContentResolver().delete(Uri.parse("content://"
                        + Authority
                        + "/" + tableName + "/" + deletionId), null, null);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Such id doesn't excist or already've been removed", Toast.LENGTH_LONG).show();
            }
        }

        private void contentResolverUpdate(String tableName, int updatingId, String userName, String userRelease) {

            ContentValues contentValues = new ContentValues();
            switch (tableName) {
                case "album":
                    contentValues.put("id", updatingId);
                    contentValues.put("name", userName);
                    contentValues.put("release", userRelease);
                    break;
                case "song":
                    contentValues.put("id", updatingId);
                    contentValues.put("name", userName);
                    contentValues.put("duration", userRelease);
                    break;
                case "albumsong":
                    contentValues.put("id", updatingId);
                    contentValues.put("album_id", Integer.parseInt(userName));
                    contentValues.put("song_id", Integer.parseInt(userRelease));
                    //in AlbumSong userName and userValues will be int
                    break;
                default:
                    return;
            }

            //AlbumSong can't have alibum_id or song_id that actually dousn't exist in database

            try {


                getContentResolver().update(Uri.parse("content://"
                                + Authority
                                + "/" + tableName + "/" + updatingId)
                        , contentValues
                        , null
                        , null
                );
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Such id doesn't excist", Toast.LENGTH_LONG).show();
            }

            return;
        }


        private Uri contentResolverInsert(String tableName, String wishingId, String wishingName, String wishingRelease) {

            ContentValues contentValues = new ContentValues();
            int curIdsNumber;
            switch (tableName) {
                case "album":

                    curIdsNumber = countAlbumId(tableName);
                    if (wishingId.isEmpty()) contentValues.put("id", curIdsNumber + 1);
                    else contentValues.put("id", wishingId);
                    contentValues.put("name", wishingName);
                    contentValues.put("release", wishingRelease);
//                contentValues.put("name", "INSERTED ALBUM");
//                contentValues.put("release", "tomorrow");
                    try {


                        return getContentResolver().insert(Uri.parse("content://"
                                        + Authority
                                        + "/" + tableName)
                                , contentValues);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Looks like we row with such Id already excists. You can try use update functionality", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                case "song":
                    curIdsNumber = countSongId(tableName);
                    if (wishingId.isEmpty()) contentValues.put("id", curIdsNumber + 1);
                    else contentValues.put("id", wishingId);
                    contentValues.put("name", wishingName);
                    contentValues.put("duration", wishingRelease);
//                contentValues.put("name", "INSERTED SONG");
//                contentValues.put("duration", "ENDLESS");
                    try {
                        return getContentResolver().insert(Uri.parse("content://"
                                        + Authority
                                        + "/" + tableName)
                                , contentValues);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Looks like we row with such Id already excists. You can try use update functionality", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                case "albumsong":
                    // ±±± We can add only if such album and song are exist!
                    if (wishingId.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "For AlbumSong it is necessary to provide an id", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    contentValues.put("id", wishingId);
                    if (wishingName.isEmpty() || wishingRelease.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "For albumsong passing id's can't be empty", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    contentValues.put("album_id", Integer.parseInt(wishingName));
                    contentValues.put("song_id", Integer.parseInt(wishingRelease));

                    try {


                        return getContentResolver().insert(Uri.parse("content://"
                                        + Authority
                                        + "/" + tableName)
                                , contentValues);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Can't insert such dependency, because one of the id for song or album doesn't excist", Toast.LENGTH_LONG).show();
                    }

                default:
                    break;

            }
            //todo delete return null
            return null;
        }


        private void unlockUpdateAction() {
            updateBlockFlag = false;
        }

        private void unlockDeleteAction() {
            deleteBlockFlag = false;
        }

        private void blockUpdateAction() {
            updateBlockFlag = true;
        }

        private void blockDeleteAction() {
            deleteBlockFlag = true;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mExecuteButton = findViewById(R.id.btn_action);
            etMainId = findViewById(R.id.et_id);
//        etUserId = findViewById(R.id.et_new_id);
            etUserName = findViewById(R.id.et_new_name);
            etUserRelease = findViewById(R.id.et_new_release);

            spinnerTable = findViewById(R.id.spinner_tables);
            spinnerAction = findViewById(R.id.spinner_actions);
//        String selectedTable = spinnerTable.getSelectedItem().toString();
//        String selectedAction = spinnerTable.getSelectedItem().toString();
//        Toast.makeText(this, selectedTable, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, selectedAction, Toast.LENGTH_SHORT).show();
            spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected = parent.getSelectedItem().toString();
                    Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mExecuteButton.setOnClickListener(onExecuteClickListener);

//        LoaderManager.getInstance(this).initLoader(123, null, this);
        }

        private static final String Authority = "com.example.m2w2b4_roomdb_musicprovider";

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new CursorLoader(this, Uri.parse("content://"
                    + Authority
                    + "/album"//name of the table
            ), null
                    , null
                    , null
                    , null
            );
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data != null
                    && data.moveToFirst()//means table is not empty
            ) {
                StringBuilder stringBuilder = new StringBuilder();

                do {
                    stringBuilder.append(data.getString(data.getColumnIndex("name"))).append("\n");
                } while (data.moveToNext());

                Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }

        private int countAlbumId(String tableName) {
            Cursor cursor = getContentResolver().query(Uri.parse("content://"
                            + Authority
                            + "/" + tableName)
                    , null
                    , null
                    , null
                    , null
                    , null
            );
            int retCnt = 0;
            if (cursor.moveToFirst()) {
                do {
                    retCnt++;
                } while (cursor.moveToNext());

            }
            return retCnt;
        }

        private int countSongId(String tableName) {
            Cursor cursor = getContentResolver().query(Uri.parse("content://"
                            + Authority
                            + "/" + tableName)
                    , null
                    , null
                    , null
                    , null
                    , null
            );
            int retCnt = 0;
            if (cursor.moveToFirst()) {
                do {
                    retCnt++;
                } while (cursor.moveToNext());

            }
            return retCnt;
        }
    }