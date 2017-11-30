package com.apps.huyenpham.memories.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.activity.ShareActivity;
import com.apps.huyenpham.memories.model.Utils;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

import static com.apps.huyenpham.memories.model.Utils.COL_PHOTO;
import static com.apps.huyenpham.memories.model.Utils.TABLE_NAME;
import static com.apps.huyenpham.memories.model.Utils.database;
import static com.apps.huyenpham.memories.model.Utils.login;

/**
 * Created by huyen on 05-Oct-17.
 */

public class FragmentPhoto extends Fragment {
    private ImageView photoShow;
    private ArrayList<byte[]> photo;
    public static Bitmap bitmapPhoto;
    private FloatingActionButton fabShare;
    private ShareDialog shareDialog;
    private boolean showFab = false;
    private final String GET_PHOTO = "SELECT " + COL_PHOTO + " FROM " + TABLE_NAME + " WHERE id = '"+ Utils.idData +"'";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        shareDialog = new ShareDialog(getActivity());
        photoShow = (ImageView) view.findViewById(R.id.photo_show);
        fabShare = (FloatingActionButton) view.findViewById(R.id.fab_share);
        supportView();
        photo = new ArrayList<>();

        Cursor cursor = database.getData(GET_PHOTO);
        photo.clear();
        while (cursor.moveToNext()) {
            photo.add(cursor.getBlob(0));
        }

        bitmapPhoto = BitmapFactory.decodeByteArray(photo.get(0), 0, photo.get(0).length);
        photoShow.setImageBitmap(bitmapPhoto);

        photoShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showFab == false) {
                    fabShare.setVisibility(View.VISIBLE);
                    showFab = true;
                } else {
                    fabShare.setVisibility(View.INVISIBLE);
                    showFab = false;
                }
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login) {
                    Log.d("aaa", "onClick: true");
                    SharePhoto sharePhoto = new SharePhoto.Builder()
                            .setBitmap(FragmentPhoto.bitmapPhoto)
                            .build();
                    SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                            .addPhoto(sharePhoto)
                            .build();
                    shareDialog.show(sharePhotoContent);
                } else {
                    Log.d("aaa", "onClick: false");
                    startActivity(new Intent(getActivity(), ShareActivity.class));
                }

            }
        });

        return view;
    }

    private void supportView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM|Gravity.END;
        int marginBottom = height/100;
        int marginHor = width/70;
        layoutParams.setMargins(marginHor, 0, marginHor, marginBottom);
        fabShare.setLayoutParams(layoutParams);
    }
}
