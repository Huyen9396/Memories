package com.apps.huyenpham.memories.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.model.UserLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.apps.huyenpham.memories.model.Utils.LOCATION_KEY;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private double latitude, longitude;
    private UserLocation userLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        Intent intent = getIntent();
        userLocation = (UserLocation) intent.getSerializableExtra(LOCATION_KEY);
        latitude = userLocation.getLatitude();
        longitude = userLocation.getLongitude();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        map.addMarker(new MarkerOptions()
                .title(userLocation.getAddress())
                .position(latLng));
    }
}
