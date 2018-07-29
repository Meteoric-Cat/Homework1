package com.meteor.homework4.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class CustomContentProvider extends ContentProvider {
    //URI
    private static final String PROVIDER_NAME = "com.meteor.homework4.db.CustomContentProvider";
    private static final String PEOPLE_URL = "content://" + PROVIDER_NAME + "/people";
    public static final Uri CONTENT_URI = Uri.parse(PEOPLE_URL);

    //table columns
    public static final String PRIMARY_KEY = "ID";
    public static final String NAME = "Name";
    public static final String CONTACT_INFO = "Contact_info";

    //private static HashMap<String, String> PROJECTION_MAP;

    private static final String PEOPLE_PATH = "people";
    private static final int PEOPLE_CODE = 1;
    private static final String PEOPLE_TYPE = "vnd.android.cursor.dir/vnd.meteor.people";
    private static final String PERSON_PATH = "people/#";
    private static final int PERSON_CODE = 2;
    private static final String PERSON_TYPE = "vnd.android.cursor.item/vnd.meteor.people";

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, PEOPLE_PATH, PEOPLE_CODE);
        uriMatcher.addURI(PROVIDER_NAME, PERSON_PATH, PERSON_CODE);
    }

    private static final int idPostion = 1;

    private SQLiteDatabase sqLiteDatabase;
    private static final String DATABASE_NAME = "PEOPLE_INFOMATION";
    private static final String PEOPLE_TABLE_NAME = "people";
    private static final int DATABASE_VERSION = 1;
    private static final String PEOPLE_CREATE_QUERY = "CREATE TABLE " + PEOPLE_TABLE_NAME + "(" +
            PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT NOT NULL, " +
            CONTACT_INFO + " TEXT NOT NULL " +
            ");";
    private static final String PEOPLE_DROP_QUERY = "DROP TABLE IF EXISTS " + PEOPLE_TABLE_NAME;

    private static final String PEOPLE_INSERT_EXCEPTION = "Can not insert to ";
    private static final String UNKNOWN_URI_EXCEPTION = " is unknown";
    private static final String UNSUPPORTED_URI = " is not supported";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context, String databaseName, int databaseVersion) {
            super(context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(PEOPLE_CREATE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL(PEOPLE_DROP_QUERY);
            this.onCreate(sqLiteDatabase);
        }
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext(), DATABASE_NAME, DATABASE_VERSION);
        this.sqLiteDatabase = databaseHelper.getWritableDatabase();
        return (this.sqLiteDatabase == null) ? false : true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowID = this.sqLiteDatabase.insert(PEOPLE_TABLE_NAME, null, contentValues);
        int invalidID = -1;

        if (rowID > invalidID) {
            Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(resultUri, null);
            return resultUri;
        }
        throw new SQLException(PEOPLE_INSERT_EXCEPTION + uri);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projectMap, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor result = null;

        switch (uriMatcher.match(uri)) {
            case PEOPLE_CODE:
                result = this.sqLiteDatabase.query(PEOPLE_TABLE_NAME, projectMap, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case PERSON_CODE:
                String id = uri.getPathSegments().get(idPostion);
                String realSelection = PRIMARY_KEY + " = " + id + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");

                result = this.sqLiteDatabase.query(PEOPLE_TABLE_NAME, projectMap, realSelection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException(uri + UNKNOWN_URI_EXCEPTION);
        }
        //getContext().getContentResolver().notifyChange(uri, null);
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case PEOPLE_CODE:
                count = this.sqLiteDatabase.delete(PEOPLE_TABLE_NAME, selection, selectionArgs);
                //reset database id sequence
                String update0="UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='"+ PEOPLE_TABLE_NAME+"'";
                this.sqLiteDatabase.execSQL(update0);
                break;

            case PERSON_CODE:
                String id = uri.getPathSegments().get(idPostion);
                String realSelection = PRIMARY_KEY + " = " + id + (TextUtils.isEmpty(selection) ? "" : "AND (" + selection + ")");
                Log.d("selection:",realSelection);

                count = this.sqLiteDatabase.delete(PEOPLE_TABLE_NAME, realSelection, selectionArgs);
                Log.d("count",String.valueOf(count));

                String update1="UPDATE "+PEOPLE_TABLE_NAME+" SET "+PRIMARY_KEY+" = ("+PRIMARY_KEY+"-1) WHERE "+PRIMARY_KEY+" > "+id;
                this.sqLiteDatabase.execSQL(update1);
                String update2="UPDATE SQLITE_SEQUENCE SET SEQ = (SEQ-1) WHERE NAME = '"+PEOPLE_TABLE_NAME+"'";
                this.sqLiteDatabase.execSQL(update2);
                break;

            default:
                throw new IllegalArgumentException(uri + UNKNOWN_URI_EXCEPTION);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case PEOPLE_CODE:
                count = this.sqLiteDatabase.update(PEOPLE_TABLE_NAME, contentValues, selection, selectionArgs);
                break;

            case PERSON_CODE:
                String id = uri.getPathSegments().get(idPostion);
                String realSelection = PRIMARY_KEY + " = " + id + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")");

                count = this.sqLiteDatabase.update(PEOPLE_TABLE_NAME, contentValues, realSelection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException(uri + UNKNOWN_URI_EXCEPTION);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String result = null;
        switch (uriMatcher.match(uri)) {
            case PEOPLE_CODE:
                return PEOPLE_TYPE;
            case PERSON_CODE:
                return PERSON_TYPE;
            default:
                throw new IllegalArgumentException(uri + UNSUPPORTED_URI);
        }
    }
}
