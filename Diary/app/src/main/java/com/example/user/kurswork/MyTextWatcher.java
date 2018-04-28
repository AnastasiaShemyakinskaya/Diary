package com.example.user.kurswork;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;

class MyTextWatcher implements View.OnFocusChangeListener {
    private Context context;
    int mHour;
    int mMinute;
    Integer value;
    Integer count;
    String strDate;
    private final Calendar cal = Calendar.getInstance();
    String time="";
    DBHelper dbHelper;
    MyTextWatcher(Context context1, Integer value1, Integer count1, String date, DBHelper dbHelper1) {
        context = context1;
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        value = value1;
        count = count1;
        strDate = date;
        dbHelper = dbHelper1;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final EditText editText = (EditText) v;
        if (hasFocus) {
            callTimePicker(editText);
            editText.clearFocus();
        }
    }

    private void callTimePicker(final EditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editText.setText(String.format("%02d:%02d", hourOfDay, minute));
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                addToSQLtime(editText);
                            }
                        };
                        new Thread(runnable).start();
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
    private void addToSQLtime(EditText editText) {
        String time = editText.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put("time", time);
        cv.put("page", count);
        cv.put("date", strDate);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("myDb", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if ((cursor.getString(cursor.getColumnIndexOrThrow("value")).equals(String.valueOf(value))) &&
                    (cursor.getString(cursor.getColumnIndexOrThrow("page")).equals(String.valueOf(count))) &&
                    (cursor.getString(cursor.getColumnIndexOrThrow("date")).equals(String.valueOf(strDate)))) {
                database.update("myDb", cv, "value=? AND page=? AND date=?", new String[]{String.valueOf(value), String.valueOf(count), strDate});
                break;
            }
        }

    }
}

