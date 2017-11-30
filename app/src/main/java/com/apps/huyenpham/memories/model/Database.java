package com.apps.huyenpham.memories.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by huyen on 04-Oct-17.
 */

public class Database extends SQLiteOpenHelper {


    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void insertData(byte[] photo, String title, String content, String date, String time, double latitude, double longitude) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Memories VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindBlob(1, photo);
        statement.bindString(2, title);
        statement.bindString(3, content);
        statement.bindString(4, date);
        statement.bindString(5, time);
        statement.bindDouble(6, latitude);
        statement.bindDouble(7, longitude);
        statement.executeInsert();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
