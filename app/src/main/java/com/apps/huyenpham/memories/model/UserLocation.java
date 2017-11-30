package com.apps.huyenpham.memories.model;

import java.io.Serializable;

/**
 * Created by huyen on 10-Oct-17.
 */

public class UserLocation implements Serializable {
    private String address;
    private double latitude;
    private double longitude;

    public UserLocation(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
