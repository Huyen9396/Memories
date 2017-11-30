package com.apps.huyenpham.memories.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.activity.GetDataActivity;
import com.apps.huyenpham.memories.activity.GoogleMapActivity;
import com.apps.huyenpham.memories.model.UserLocation;
import com.apps.huyenpham.memories.model.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.apps.huyenpham.memories.model.Utils.COL_CONTENT;
import static com.apps.huyenpham.memories.model.Utils.COL_DATE;
import static com.apps.huyenpham.memories.model.Utils.COL_ID;
import static com.apps.huyenpham.memories.model.Utils.COL_LATI;
import static com.apps.huyenpham.memories.model.Utils.COL_LONG;
import static com.apps.huyenpham.memories.model.Utils.COL_TIME;
import static com.apps.huyenpham.memories.model.Utils.COL_TITLE;
import static com.apps.huyenpham.memories.model.Utils.LOCATION_KEY;
import static com.apps.huyenpham.memories.model.Utils.TABLE_NAME;
import static com.apps.huyenpham.memories.model.Utils.database;
import static com.apps.huyenpham.memories.model.Utils.idData;

/**
 * Created by huyen on 05-Oct-17.
 */

public class FragmentNote extends Fragment {
    private TextView textDate, textTime, textTitle, textContent, textAddress;
    private FloatingActionButton fabEdit;
    private Geocoder geocoder;
    private List<Address> addressList;
    private UserLocation userLocation;
    private final String GET_NOTE = "SELECT " + COL_TITLE + ", " +
                                                COL_CONTENT + ", " +
                                                COL_DATE + ", " +
                                                COL_TIME + ", " +
                                                COL_LATI + ", " +
                                                COL_LONG +
                                    " FROM " + TABLE_NAME + " WHERE " + COL_ID + " = '"+ idData +"'";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        initView(view);
        supportView();

        Cursor cursor = database.getData(GET_NOTE);
        while (cursor.moveToNext()) {
            textDate.setText(cursor.getString(2));
            textTime.setText(cursor.getString(3));

            if (cursor.getString(0).equals("")) {
                textTitle.setText("Nothing to show");
                textTitle.setTextColor(Color.GRAY);
            } else {
                textTitle.setText(cursor.getString(0));
            }

            if (cursor.getString(1).equals("")) {
                textContent.setText("Nothing to show");
                textContent.setTextColor(Color.GRAY);
            } else {
                textContent.setText(cursor.getString(1));
            }

            if (cursor.getDouble(4) != 0 && cursor.getDouble(5) != 0) {
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    addressList = geocoder.getFromLocation(cursor.getDouble(4), cursor.getDouble(5), 1);
                    String address = addressList.get(0).getAddressLine(0);
                    Log.d("aaa", "address: " + address);
                    userLocation = new UserLocation(address, cursor.getDouble(4), cursor.getDouble(5));
                    textAddress.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        textAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
                intent.putExtra(LOCATION_KEY, userLocation);
                startActivity(intent);
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.checkPressEditFab = true;
                startActivity(new Intent(getActivity(), GetDataActivity.class));
            }
        });

        return view;
    }

    private void initView(View view) {
        textDate = (TextView) view.findViewById(R.id.date_show);
        textTime = (TextView) view.findViewById(R.id.time_show);
        textAddress = (TextView) view.findViewById(R.id.address_show);
        textTitle = (TextView) view.findViewById(R.id.title_show);
        textContent = (TextView) view.findViewById(R.id.content_show);
        fabEdit = (FloatingActionButton) view.findViewById(R.id.fab_edit);
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
        fabEdit.setLayoutParams(layoutParams);

        float dateSize = height/65;
        textDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateSize);
        textTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateSize);

        float addressSize = height/55;
        textAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, addressSize);

        FrameLayout.LayoutParams layoutTitle = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginVer = height/100;
        int marginHorContent = width/70;
        layoutTitle.setMargins(marginHorContent, marginVer, marginHorContent, marginVer);
        float titleSize = height/50;
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        textContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleSize);
        textContent.setLayoutParams(layoutTitle);
        textTitle.setLayoutParams(layoutTitle);
    }
}
