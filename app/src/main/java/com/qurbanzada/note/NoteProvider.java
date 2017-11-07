package com.qurbanzada.note;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by aqn3130 on 04/07/2017.
 */

public class NoteProvider extends ContentProvider {
    private static final String AUTHORITY = "com.qurbanzada.note.noteprovider";
    private static final String BASE_PATH = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String CONTENT_ITEM_TYPE = "Note";

    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH,NOTES);
        uriMatcher.addURI(AUTHORITY,BASE_PATH+"/#",NOTES_ID);
    }

    private SQLiteDatabase database;
    @Override
    public boolean onCreate() {
        DBOpen open = new DBOpen(getContext());

        database = open.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        if(uriMatcher.match(uri) == NOTES_ID){
            selection = DBOpen.NOTE_ID+"="+uri.getLastPathSegment();
        }
        return database.query(DBOpen.TABLE_NOTES,DBOpen.All_COLUMNS,selection,null,null,null,DBOpen.NOTE_CREATED);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = database.insert(DBOpen.TABLE_NOTES,null,contentValues);
        return Uri.parse(BASE_PATH+"/"+id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DBOpen.TABLE_NOTES,selection,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DBOpen.TABLE_NOTES,contentValues,selection,selectionArgs);
    }
}
