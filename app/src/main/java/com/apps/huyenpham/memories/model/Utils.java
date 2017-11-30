package com.apps.huyenpham.memories.model;

/**
 * Created by huyen on 06-Oct-17.
 */

public final class Utils {
    public static Database database;
    public static final String TABLE_NAME = "Memories";
    public static final String COL_ID = "id";
    public static final String COL_PHOTO = "photo";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";
    public static final String COL_LATI = "latitude";
    public static final String COL_LONG = "longitude";
    public static final int    LIMIT_PHOTO = 10;
    public static double latitude, longitude;

    public static int idData;
    public static boolean checkPressEditFab = false;
    public static boolean login = false;
    public static final String LOCATION_KEY = "location";
}
