package com.apps.huyenpham.memories.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apps.huyenpham.memories.R;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = (ImageView) findViewById(R.id.icon_splash);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int iconHeight = height/5;
        int iconWidth = width/2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(iconWidth, iconHeight);
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);

        imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_splash));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                overridePendingTransition(R.anim.anim_splash_in, R.anim.anim_splash_out);
            }
        }, 7000);
    }
}
