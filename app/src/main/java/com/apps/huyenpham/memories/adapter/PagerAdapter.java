package com.apps.huyenpham.memories.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apps.huyenpham.memories.R;

import java.util.List;

/**
 * Created by huyen on 10-Oct-17.
 */

public class PagerAdapter extends android.support.v4.view.PagerAdapter {
    private Context context;
    private List<byte[]> photo;

    public PagerAdapter(Context context, List<byte[]> photo) {
        this.context = context;
        this.photo = photo;
    }

    @Override
    public int getCount() {
        return photo.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.photo_slide);
        Bitmap bitmap = BitmapFactory.decodeByteArray(photo.get(position), 0, photo.get(position).length);
        imageView.setImageBitmap(bitmap);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
