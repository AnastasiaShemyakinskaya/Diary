package com.example.user.kurswork;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.EditText;


public class MyThread extends AsyncTask<Void, Void, Void> {
    private EditText[]editText;
    private String[] str;
    private DBHelper dbHelper;
    private Integer counter;
    private String strDate;
    MyThread(EditText[]editText1, DBHelper dbHelper1, Integer counter1, String currDate) {
        editText = editText1;
        dbHelper = dbHelper1;
        counter = counter1;
        strDate = currDate;
        str =  new String[editText.length];
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {
        for (int i=0; i<editText.length; i++) {
            if (editText[i] instanceof MyEdittext) {
                str[i] = getTask("task", i);
            } else {
                str[i] =  getTask("time", i);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        for (int i=0; i<editText.length; ++i ) {
            if (!(str[i].equals(""))) {
                editText[i].setText(str[i]);
            } else {
                editText[i].setText("");
            }
        }
    }
    private String getTask(String str, Integer num) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String text = "";
        Cursor c = null;
        if (db != null) {
            try {
                c = db.query("myDb", new String[]{str}, "value=? AND page=? AND date=?"  , new String[]{String.valueOf(num), String.valueOf(counter), strDate}, null, null, null);
                c.moveToFirst();
                text = c.getString(c.getColumnIndexOrThrow(str));
                if (text==null){
                    text = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    c.close();
                }
            }
            db.close();
        }
        return text;
    }
}