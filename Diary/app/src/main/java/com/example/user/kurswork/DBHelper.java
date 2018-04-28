package com.example.user.kurswork;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public DBHelper(Context context) {

        super(context, "myDb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table myDb ("
                + "id integer primary key autoincrement,"
                + "value text,"
                + "page text,"
                + "date text,"
                + "task text,"
                + "time text" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}