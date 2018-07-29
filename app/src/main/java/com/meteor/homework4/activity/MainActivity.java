package com.meteor.homework4.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.meteor.homework4.adapter.CustomRvAdapter;
import com.meteor.homework4.db.CustomContentProvider;
import com.meteor.homework4.db.CustomCursorLoader;
import com.meteor.homework4.fragment.InputDialog1;
import com.meteor.homework4.fragment.InputDialog2;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
        implements InputDialog1.ClickHandler, InputDialog2.ClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private static final long requireHoldTime = 1000;
    private static final int INVALID_KEY = -1;

    private Button btn_add, btn_linear, btn_grid, btnDelete, btnCancel;

    private RecyclerView rv_info;
    private RecyclerView.LayoutManager rv_infoLiLayoutManager, rv_infoGrLayoutManager;
    private CustomRvAdapter rv_infoAdapter;

    private RelativeLayout rlChildLayout1, rlChildLayout2;                                          //fragments...

    private InputDialog1 inputDialog1;
    //private InputDialog2 inputDialog2;

    private int clickedChild;

    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildUIViews();
        buildUIListeners();
    }

    private void buildUIViews() {
        btn_add = findViewById(R.id.btn_add);
        btn_grid = findViewById(R.id.btn_grid);
        btn_linear = findViewById(R.id.btn_linear);
        btnDelete = findViewById(R.id.btn_mainDelete);
        btnCancel = findViewById(R.id.btn_mainCancel);

        inputDialog1 = new InputDialog1();
        //inputDialog2 = new InputDialog2();

        rlChildLayout1 = findViewById(R.id.rl_childLayout1);
        rlChildLayout2 = findViewById(R.id.rl_childLayout2);

        //build RecyclerView
        rv_info = findViewById(R.id.rv_info);

        rv_infoGrLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.GridLayout_ColumnAmount));
        rv_infoLiLayoutManager = new LinearLayoutManager(this);
        rv_info.setLayoutManager(rv_infoLiLayoutManager);

        rv_infoAdapter = new CustomRvAdapter();
        rv_info.setAdapter(rv_infoAdapter);

        buildInitialRVItems();
    }

    private void buildInitialRVItems() {
        loaderManager = getSupportLoaderManager();
        try {
            //getContentResolver().delete(CustomContentProvider.CONTENT_URI,null, null);

            Cursor initialCursor = getContentResolver().query(CustomContentProvider.CONTENT_URI,
                    null, null, null, null);

            if ((initialCursor != null) && (initialCursor.getCount() > 0)) {
                String name = null, contact_info = null;

                while (initialCursor.moveToNext()) {
                    name = initialCursor.getString(initialCursor.getColumnIndex(CustomContentProvider.NAME));
                    contact_info = initialCursor.getString(initialCursor.getColumnIndex(CustomContentProvider.CONTACT_INFO));
                    this.rv_infoAdapter.addItem(name, contact_info);
                }
            }
            initialCursor.close();

            //loaderManager.initLoader(CustomCursorLoader.INITIAL_TYPE, null, this);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void buildUIListeners() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_add:
                        rv_infoAdapter.hideCheckBoxes();
                        rlChildLayout2.setVisibility(View.GONE);
                        rlChildLayout1.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().popBackStack();
                        inputDialog1.show(getSupportFragmentManager().beginTransaction(), getString(R.string.dg_input1tag));
                        break;
                    case R.id.btn_grid: {
                        rv_info.setLayoutManager(rv_infoGrLayoutManager);
                        break;
                    }
                    case R.id.btn_linear: {
                        rv_info.setLayoutManager(rv_infoLiLayoutManager);
                        break;
                    }
                    case R.id.btn_mainDelete: {
                        handleDelete2();
                        break;
                    }
                    case R.id.btn_mainCancel: {
                        rlChildLayout2.setVisibility(View.GONE);
                        rlChildLayout1.setVisibility(View.VISIBLE);
                        rv_infoAdapter.hideCheckBoxes();
                        break;
                    }
                }
            }
        };

        btn_add.setOnClickListener(clickListener);
        btn_linear.setOnClickListener(clickListener);
        btn_grid.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
        btnDelete.setOnClickListener(clickListener);

        rv_info.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null) {
                    CustomRvAdapter.CustomViewHolder viewHolder = (CustomRvAdapter.CustomViewHolder) recyclerView.findContainingViewHolder(child);
                    rv_infoAdapter.checkboxListener.clickedDataID = viewHolder.dataID;

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if ((motionEvent.getEventTime() - motionEvent.getDownTime()) > requireHoldTime) {
                            rv_infoAdapter.displayCheckBoxes();
                            rlChildLayout1.setVisibility(View.GONE);
                            rlChildLayout2.setVisibility(View.VISIBLE);
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {
            }
        });

    }

    private void displayInitialData(Cursor cursor) {
        String name = null, contactInfo = null;
        if ((cursor != null) && (cursor.getCount() > 0)) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(CustomContentProvider.NAME));
                contactInfo = cursor.getString(cursor.getColumnIndex(CustomContentProvider.CONTACT_INFO));
                this.rv_infoAdapter.addItem(name, contactInfo);
            }
        }
    }

    @Override
    public void handleSave1(String name, String contactInfo) {
        try {
 /*           ContentValues contentValues = new ContentValues();
            contentValues.put(CustomContentProvider.NAME, name);
            contentValues.put(CustomContentProvider.CONTACT_INFO, contactInfo);

            getContentResolver().insert(CustomContentProvider.CONTENT_URI, contentValues);
*/

            Bundle bundle = new Bundle();
            bundle.putInt(CustomContentProvider.PRIMARY_KEY, INVALID_KEY);
            bundle.putString(CustomContentProvider.NAME, name);
            bundle.putString(CustomContentProvider.CONTACT_INFO, contactInfo);

            this.loaderManager.restartLoader(CustomCursorLoader.INSERT_TYPE, bundle, this).forceLoad();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        rv_infoAdapter.addItem(name, contactInfo);
    }

    @Override
    public void handleSave2(String name, String contactInfo) {
        rv_infoAdapter.updateItem(clickedChild, name, contactInfo);
    }

    @Override
    public void handleDelete2() {
        LinkedList<Integer> list = this.rv_infoAdapter.getChosenList();
        //String selection = CustomContentProvider.PRIMARY_KEY + " = ?";
        int deletedAmount = 0;

        for (Integer iter : list) {
            try {
                int count = getContentResolver().delete(
                        ContentUris.withAppendedId(CustomContentProvider.CONTENT_URI, iter.longValue() + 1 - deletedAmount), null, null);
                deletedAmount++;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                continue;
            }
            rv_infoAdapter.deleteItem(iter + 1 - deletedAmount);
            //can delete all before updating database again
        }
    }

    private CustomCursorLoader createInsertLoader(Bundle bundle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CustomContentProvider.NAME, bundle.getString(CustomContentProvider.NAME));
        contentValues.put(CustomContentProvider.CONTACT_INFO, bundle.getString(CustomContentProvider.CONTACT_INFO));

        //selection, selectionArgs ...

        int key = bundle.getInt(CustomContentProvider.PRIMARY_KEY);
        Uri uri = (key == INVALID_KEY) ? CustomContentProvider.CONTENT_URI :
                ContentUris.withAppendedId(CustomContentProvider.CONTENT_URI, key);
        Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_SHORT).show();

        return new CustomCursorLoader(this, CustomCursorLoader.INSERT_TYPE,
                uri, contentValues, null, null);
    }

    private CustomCursorLoader createQueryLoader(Bundle bundle) {
        //...
        return null;
    }

    private CustomCursorLoader createDeleteLoader(Bundle bundle) {
        int key = bundle.getInt(CustomContentProvider.PRIMARY_KEY);
        Uri uri = (key == INVALID_KEY) ? CustomContentProvider.CONTENT_URI :
                ContentUris.withAppendedId(CustomContentProvider.CONTENT_URI, key);
        return new CustomCursorLoader(this, CustomCursorLoader.DELETE_TYPE, uri,
                null, null, null);
    }

    private CustomCursorLoader createUpdateLoader(Bundle bundle) {
        //...
        return null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        CustomCursorLoader loader = null;
        switch (id % 10) {
            case CustomCursorLoader.INITIAL_TYPE:
                loader = new CustomCursorLoader(this, CustomCursorLoader.INITIAL_TYPE, CustomContentProvider.CONTENT_URI);
                break;

            case CustomCursorLoader.INSERT_TYPE: {
                loader = this.createInsertLoader(bundle);
                break;
            }

            case CustomCursorLoader.QUERY_TYPE: {
                loader = this.createQueryLoader(bundle);
                break;
            }

            case CustomCursorLoader.DELETE_TYPE: {
                loader = this.createDeleteLoader(bundle);
                break;
            }

            case CustomCursorLoader.UPDATE_TYPE: {
                loader = this.createUpdateLoader(bundle);
                break;
            }
        }
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        CustomCursorLoader temp = (CustomCursorLoader) loader;
        switch (temp.getType()) {
            case CustomCursorLoader.INITIAL_TYPE:
                this.displayInitialData(cursor);
                break;
            case CustomCursorLoader.INSERT_TYPE:
                Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_SHORT).show();
                break;
            case CustomCursorLoader.DELETE_TYPE:
                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}

