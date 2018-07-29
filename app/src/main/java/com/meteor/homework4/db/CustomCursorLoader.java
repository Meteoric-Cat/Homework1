package com.meteor.homework4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

public class CustomCursorLoader extends AsyncTaskLoader<Cursor> {
    public static final int INITIAL_TYPE = 0;
    public static final int INSERT_TYPE = 1;
    public static final int QUERY_TYPE = 2;
    public static final int DELETE_TYPE = 3;
    public static final int UPDATE_TYPE = 4;

    private int type;
    private Uri baseUri;
    private ContentValues contentValues;
    private String[] projectMap, selectionArgs;
    private String selection, sortOrder;

    public CustomCursorLoader(Context context, int type, Uri baseUri) {
        super(context);

        this.type = type;
        this.baseUri = baseUri;

        this.contentValues = null;
        this.projectMap = null;
        this.selection = null;
        this.selectionArgs = null;
        this.sortOrder = null;
    }

    public CustomCursorLoader(Context context, int type, Uri baseUri, ContentValues contentValues, String selection, String[] selectionArgs) {
        super(context);

        this.type = type;
        this.baseUri = baseUri;
        this.contentValues = contentValues;

        this.projectMap = null;
        this.selection = null;
        this.selectionArgs = null;
        this.sortOrder = null;
    }

    public CustomCursorLoader(Context context, int type, Uri baseUri, String[] projectMap, String selection, String[] selectionArgs, String sortOrder) {
        super(context);

        this.type = type;
        this.baseUri = baseUri;
        this.projectMap = projectMap;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;

        this.contentValues = null;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor result = null;

        switch (type) {
            case INITIAL_TYPE:
                result = getContext().getContentResolver().query(baseUri,
                        null, null, null, null);
                break;
            case INSERT_TYPE:
                getContext().getContentResolver().insert(baseUri, this.contentValues);
                break;
            case QUERY_TYPE:
                result = getContext().getContentResolver().query(baseUri, projectMap, selection, selectionArgs, sortOrder);
                break;
            case DELETE_TYPE:
                getContext().getContentResolver().delete(baseUri, selection, selectionArgs);
                break;
            case UPDATE_TYPE:
                getContext().getContentResolver().update(baseUri, contentValues, selection, selectionArgs);
                break;
        }
        //Looper.prepare();
        //Toast.makeText(getContext().getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
        //Looper.loop();
        return result;
    }

    public int getType() {
        return this.type;
    }
}
