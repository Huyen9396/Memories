package com.apps.huyenpham.memories.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.activity.PhotoSlideActivity;
import com.apps.huyenpham.memories.activity.PhotosActivity;

import java.util.List;

/**
 * Created by huyen on 10-Oct-17.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.itemHolder> {
    private PhotosActivity context;
    private List<byte[]> photoLists;
    public static final String POSITION = "position";

    public PhotoAdapter(PhotosActivity context, List<byte[]> photoLists) {
        this.context = context;
        this.photoLists = photoLists;
    }

    @Override
    public itemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_row, null);
        itemHolder holder = new itemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(itemHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(photoLists.get(position), 0, photoLists.get(position).length);
        holder.photo.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return photoLists.size();
    }

    public class itemHolder extends RecyclerView.ViewHolder {
        private ImageView photo;

        private itemHolder(final View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            supportPhoto();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotoSlideActivity.class);
                    intent.putExtra(POSITION, getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }

        private void supportPhoto() {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            int marginHor = width/150;
            int marginVer = height/230;
            int photoHeight = height/4;
            LinearLayout.LayoutParams layoutPhoto = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, photoHeight);
            layoutPhoto.setMargins(marginHor, marginVer, marginHor, marginVer);
            photo.setLayoutParams(layoutPhoto);
        }
    }
}
