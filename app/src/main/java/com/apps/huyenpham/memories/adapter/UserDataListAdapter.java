package com.apps.huyenpham.memories.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.activity.UserDataListActivity;
import com.apps.huyenpham.memories.model.UserData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by huyen on 04-Oct-17.
 */

public class UserDataListAdapter extends BaseAdapter {
    private UserDataListActivity context;
    private List<UserData> userDataList;
    private Geocoder geocoder;
    private List<Address> addressList;

    public UserDataListAdapter(UserDataListActivity context, List<UserData> userDataList) {
        this.context = context;
        this.userDataList = userDataList;
    }

    @Override
    public int getCount() {
        return userDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class viewHolder {
        TextView textDate, textTime, textTitle, textLocation;
        ImageView imageCaptureList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if (convertView == null) {
            holder = new viewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.user_data_row, null);
            holder.textDate = (TextView) convertView.findViewById(R.id.text_date);
            holder.textTime = (TextView) convertView.findViewById(R.id.text_time);
            holder.textTitle = (TextView) convertView.findViewById(R.id.text_title);
            holder.textLocation = (TextView) convertView.findViewById(R.id.text_location);
            holder.imageCaptureList = (ImageView) convertView.findViewById(R.id.image_capture_list);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        holder.textDate.setText(userDataList.get(position).getDate());
        holder.textTime.setText(userDataList.get(position).getTime());
        holder.textTitle.setEllipsize(TextUtils.TruncateAt.END);
        holder.textTitle.setText(userDataList.get(position).getTitle());

        Bitmap bitmap = BitmapFactory.decodeByteArray(userDataList.get(position).getPhoto(), 0, userDataList.get(position).getPhoto().length);
        holder.imageCaptureList.setImageBitmap(bitmap);

        if (userDataList.get(position).getLatitude() != 0 && userDataList.get(position).getLongitude() != 0) {
            geocoder = new Geocoder(context, Locale.getDefault());
            try {
                addressList = geocoder.getFromLocation(userDataList.get(position).getLatitude(), userDataList.get(position).getLongitude(), 1);
                String address = addressList.get(0).getAddressLine(0);
                holder.textLocation.setText(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        convertView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_list));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        CardView cardView = (CardView) convertView.findViewById(R.id.card_view);
        int marginTopCV = height/100;
        LinearLayout.LayoutParams layoutCardView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutCardView.setMargins(0, marginTopCV, 0, 0);
        cardView.setLayoutParams(layoutCardView);

        float timeSize = height/78;
        holder.textDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeSize);
        holder.textTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeSize);

        LinearLayout.LayoutParams layoutLocation = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        float locationSize = height/60;
        holder.textLocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, locationSize);
        int marginHor = width/110;
        int marginVer = height/220;
        layoutLocation.setMargins(marginHor, marginVer, marginHor, marginVer);
        holder.textLocation.setLayoutParams(layoutLocation);

        int photoHeight = height/2;
        LinearLayout.LayoutParams layoutPhoto = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, photoHeight);
        layoutPhoto.setMargins(marginHor, marginVer, marginHor, marginVer);
        holder.imageCaptureList.setLayoutParams(layoutPhoto);

        LinearLayout.LayoutParams layoutTitle = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int titleSize = height/50;
        holder.textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        layoutTitle.setMargins(marginHor, marginVer, marginHor, marginVer);
        holder.textTitle.setLayoutParams(layoutTitle);

        return convertView;
    }
}
