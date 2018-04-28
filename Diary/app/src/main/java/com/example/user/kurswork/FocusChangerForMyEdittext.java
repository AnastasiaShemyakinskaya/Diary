package com.example.user.kurswork;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;


public class FocusChangerForMyEdittext implements View.OnFocusChangeListener {
    private Integer value;
    private Integer count;
    private String strDate;
    DBHelper dbHelper;
    private EditTime timeEditText;
    FocusChangerForMyEdittext(Integer value1, Integer count1, String date, DBHelper dbHelper1, EditTime editTime){
        value = value1;
        count = count1;
        strDate = date;
        dbHelper = dbHelper1;
        timeEditText = editTime;
    }
    public void onFocusChange(View v, boolean hasFocus) {
        final EditText editText = (EditText) v;
        if (!hasFocus) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    String task;
                    String time = timeEditText.getText().toString();
                    String t = editText.getText().toString();
                    if (!(t.equals(""))) {
                        task = editText.getText().toString();
                        cv.put("value", String.valueOf(value));
                        cv.put("task", task);
                        cv.put("time", time);
                        cv.put("page", count);
                        cv.put("date", strDate);
                        Cursor cursor = database.query("myDb", null, null, null, null, null, null);
                        while (cursor.moveToNext()) {
                            if ((cursor.getString(cursor.getColumnIndexOrThrow("value")).equals(String.valueOf(value))) &&
                                    (cursor.getString(cursor.getColumnIndexOrThrow("page")).equals(String.valueOf(count))) &&
                                    (cursor.getString(cursor.getColumnIndexOrThrow("date")).equals(String.valueOf(strDate)))) {
                                database.delete("myDb", "value=? AND page=? AND date=?", new String[]{String.valueOf(value), String.valueOf(count), strDate});
                                database.insert("myDb", null, cv);
                                break;
                            }
                        }
                        if (cursor.isAfterLast()) {
                            database.insert("myDb", null, cv);
                        }
                    } else {
                        try {
                            Cursor cursor = database.query("myDb", null, null, null, null, null, null);
                            while (cursor.moveToNext()) {
                                if ((cursor.getString(cursor.getColumnIndexOrThrow("value")).equals(String.valueOf(value))) &&
                                        (cursor.getString(cursor.getColumnIndexOrThrow("page")).equals(String.valueOf(count))) &&
                                        (cursor.getString(cursor.getColumnIndexOrThrow("date")).equals(String.valueOf(strDate)))) {
                                    database.delete("myDb", "value=? AND page=? AND date=?", new String[]{String.valueOf(value), String.valueOf(count), strDate});
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            new Thread(runnable).start();
        }
    }
}

