package com.apps.huyenpham.memories.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.model.Database;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

import static com.apps.huyenpham.memories.R.id.btn_menu_home;
import static com.apps.huyenpham.memories.model.Utils.COL_CONTENT;
import static com.apps.huyenpham.memories.model.Utils.COL_DATE;
import static com.apps.huyenpham.memories.model.Utils.COL_ID;
import static com.apps.huyenpham.memories.model.Utils.COL_LATI;
import static com.apps.huyenpham.memories.model.Utils.COL_LONG;
import static com.apps.huyenpham.memories.model.Utils.COL_PHOTO;
import static com.apps.huyenpham.memories.model.Utils.COL_TIME;
import static com.apps.huyenpham.memories.model.Utils.COL_TITLE;
import static com.apps.huyenpham.memories.model.Utils.LIMIT_PHOTO;
import static com.apps.huyenpham.memories.model.Utils.TABLE_NAME;
import static com.apps.huyenpham.memories.model.Utils.database;
import static com.apps.huyenpham.memories.model.Utils.login;

public class HomeActivity extends AppCompatActivity {
    private ViewFlipper photoFlipper, titleFlipper;
    private ImageButton btnMenu, btnAdd, btnAlbum, btnPhoto;
    private ArrayList<byte[]> photoList;
    private ArrayList<String> titleList;
    private boolean checkPressFab = false;
    private int height, width;
    private LinearLayout menuNav;
    private boolean doubleBackToExitPressedOnce = false;

    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                                               COL_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                               COL_PHOTO   + " BLOB, " +
                                               COL_TITLE   + " VARCHAR(150), " +
                                               COL_CONTENT + " VARCHAR(255), " +
                                               COL_DATE    + " VARCHAR(15), " +
                                               COL_TIME    + " VARCHAR(10), " +
                                               COL_LATI    + " DOUBLE(150), " +
                                               COL_LONG    + " DOUBLE(150))";

    private final String GET_PHOTO_FLIPPER = "SELECT " + COL_PHOTO + ", " + COL_TITLE + " FROM " +
                                              TABLE_NAME + " ORDER BY " + COL_ID + " DESC LIMIT " + LIMIT_PHOTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        supportView();

        photoList = new ArrayList<>();
        titleList = new ArrayList<>();
        database = new Database(this, "memories.sql", null, 1);
        database.queryData(CREATE_TABLE);

        Cursor cursor = database.getData(GET_PHOTO_FLIPPER);
        while (cursor.moveToNext()) {
            photoList.add(cursor.getBlob(0));
            titleList.add(cursor.getString(1));
        }

        for (int i = 0; i < photoList.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(photoList.get(i), 0, photoList.get(i).length);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            photoFlipper.addView(imageView);
        }

        for (int i = 0; i < titleList.size(); i++) {
            TextView title = new TextView(this);
            title.setText(titleList.get(i));
            title.setTextColor(Color.GREEN);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, height/50);
            titleFlipper.setPadding(width/50, height/800, width/50, 0);
            titleFlipper.addView(title);
        }

        titleFlipper.setFlipInterval(7000);
        titleFlipper.setAutoStart(true);
        titleFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.photo_slide_in));
        titleFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.photo_slide_out));

        photoFlipper.setFlipInterval(7000);
        photoFlipper.setAutoStart(true);
        photoFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.photo_slide_in));
        photoFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.photo_slide_out));

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPressFab == false) {
                    menuNav.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_anim_enter));
                    menuNav.setVisibility(View.VISIBLE);
                    checkPressFab = true;
                } else {
                    menuNav.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_anim_exit));
                    menuNav.setVisibility(View.INVISIBLE);
                    checkPressFab = false;
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GetDataActivity.class));
                finish();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PhotosActivity.class));
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDataListActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        login = false;
        LoginManager.getInstance().logOut();
    }

    private void initView() {
        photoFlipper = (ViewFlipper) findViewById(R.id.photo_slide);
        btnMenu = (ImageButton) findViewById(btn_menu_home);
        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAlbum = (ImageButton) findViewById(R.id.btn_album);
        btnPhoto = (ImageButton) findViewById(R.id.btn_all_photos);
        titleFlipper = (ViewFlipper) findViewById(R.id.title_slide);
        menuNav = (LinearLayout) findViewById(R.id.menu_nav);
    }

    private void supportView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        int marginVer = height/115;
        int marginHor = width/75;
        LinearLayout.LayoutParams layoutMenuNav = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6);
        layoutMenuNav.setMargins(marginHor, marginVer, marginHor, marginVer);
        menuNav.setLayoutParams(layoutMenuNav);

        int fabHeight = height/13;
        int fabWidth = width/8;
        LinearLayout.LayoutParams layoutMenuSize = new LinearLayout.LayoutParams(fabWidth, fabHeight);
        btnAlbum.setLayoutParams(layoutMenuSize);
        btnPhoto.setLayoutParams(layoutMenuSize);
        btnAdd.setLayoutParams(layoutMenuSize);
        btnMenu.setLayoutParams(layoutMenuSize);

        int paddingHor = width/150;
        int paddingVer = height/230;
        btnAlbum.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnPhoto.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnAdd.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        btnMenu.setPadding(paddingHor, paddingVer, paddingHor, paddingVer);

    }
}
