package com.apps.huyenpham.memories.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.apps.huyenpham.memories.R;

import static com.apps.huyenpham.memories.activity.PhotosActivity.photoArray;
import static com.apps.huyenpham.memories.adapter.PhotoAdapter.POSITION;

public class PhotoSlideActivity extends AppCompatActivity {
    private ViewPager pager;
    private PagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slide);
        Intent intent = getIntent();
        int positionItem = intent.getIntExtra(POSITION, 0);

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new com.apps.huyenpham.memories.adapter.PagerAdapter(this, photoArray);
        pager.setAdapter(adapter);

        pager.setCurrentItem(positionItem, true);

    }
}
