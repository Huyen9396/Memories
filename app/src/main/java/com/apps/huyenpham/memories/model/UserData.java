package com.apps.huyenpham.memories.model;

/**
 * Created by huyen on 04-Oct-17.
 */

public class UserData {
    private int id;
    private byte[] photo;
    private String title;
    private String content;
    private String date;
    private String time;
    private double latitude;
    private double longitude;

    public UserData(int id, byte[] photo, String title, String content, String date, String time, double latitude, double longitude) {
        this.id = id;
        this.photo = photo;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
