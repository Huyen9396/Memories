package com.apps.huyenpham.memories.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.adapter.PhotoAdapter;

import java.util.ArrayList;

import static com.apps.huyenpham.memories.model.Utils.COL_ID;
import static com.apps.huyenpham.memories.model.Utils.COL_PHOTO;
import static com.apps.huyenpham.memories.model.Utils.TABLE_NAME;
import static com.apps.huyenpham.memories.model.Utils.database;

public class PhotosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static ArrayList<byte[]> photoArray;
    private PhotoAdapter adapter;
    private final String GET_PHOTOS = "SELECT " + COL_PHOTO + " FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        initView();

        Cursor cursor = database.getData(GET_PHOTOS);
        while (cursor.moveToNext()) {
            photoArray.add(cursor.getBlob(0));
        }
        adapter.notifyDataSetChanged();

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.all_photo);
        photoArray = new ArrayList<>();
        adapter = new PhotoAdapter(this, photoArray);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(adapter);
    }
}
