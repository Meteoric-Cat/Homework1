package com.meteor.homework4.db;

import android.content.ContentProvider;
import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.HashMap;

public class CustomContentProvider extends ContentProvider {
    //URI
    private static final String PROVIDER_NAME = "com.meteor.homework4.MeteoricCat";
    private static final String URL = "content://" + PROVIDER_NAME + "/people";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    //table columns
    public static final String PRIMARY_KEY = "ID";
    public static final String NAME = "Name";
    public static final String CONTACT_INFO = "Contact_info";

    private static HashMap<String, String> PROJECTION_MAP;

    private static final int PEOPLE_CODE = 1;
    private static final String PEOPLE_PATH = "people";
    private static final String PERSON_PATH = "person/#";
    private static final int PERSON_CODE = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, PEOPLE_PATH, PEOPLE_CODE);
        uriMatcher.addURI(PROVIDER_NAME, PERSON_PATH, PERSON_CODE);
    }

    private SQLiteDatabase sqLiteDatabase;
    private static final String DATABASE_NAME = "PEOPLE_INFOMATION";
    private static final String PEOPLE_TABLE_NAME = "people";
    private static final int DATABASE_VERSION = 1;
    private static final String PEOPLE_CREATE_QUERY = "CREATE TABLE " + PEOPLE_TABLE_NAME + "(" +
            PRIMARY_KEY+ " PRIMARY KEY AUTOINCREMENT, " +
            NAME +" TEXT NOT NULL, " +
            CONTACT_INFO+" TEXT NOT NULL " +
            ");";
    private static final String PEOPLE_DROP_QUERY = "DROP TABLE IF "+PEOPLE_TABLE_NAME;

    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context, String databaseName, int databaseVersion){
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

}
