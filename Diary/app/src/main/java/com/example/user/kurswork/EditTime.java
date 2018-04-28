package com.example.user.kurswork;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.AppCompatEditText;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

public class EditTime extends AppCompatEditText implements View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener {
    private Integer value;
    private Integer count;
    private String strDate;
    private DBHelper dbHelper;
    public EditTime(Context context,  Integer value1, Integer count1, String date, DBHelper dbHelper1) {
        super(context);
        value = value1;
        count = count1;
        strDate = date;
        dbHelper = dbHelper1;
        this.setOnCreateContextMenuListener(this);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    android.view.ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        menu.add(0, 1, 0, "Очистить");
        menu.getItem(0).setOnMenuItemClickListener(this);
    }

    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1: {
                this.setText("");
                updateSQL();
                break;
            }
            case 2: {
                this.setText("");
                updateSQL();
                break;
            }
            default:break;
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        onContextItemSelected(item);
        return true;
    }
    private  void updateSQL() {
        ContentValues cv = new ContentValues();
        cv.put("time", "");
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.update("myDb", cv,  "value=? AND page=? AND date=?", new String[] {String.valueOf(value),String.valueOf(count), strDate});
    }
}

