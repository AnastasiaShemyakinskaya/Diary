package com.example.user.kurswork;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyThread myThread;
    private MyEdittext []editTexts = new MyEdittext[5];
    private EditTime []time = new EditTime[5];
    DBHelper dbHelper;
    private Integer counter=1;
    private String currDate;
    private boolean setDate=false;
    private boolean SwipedRight=false;
    private boolean SwipedLeft=false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createGui();
    }
    private void createGui(){
        RelativeLayout relativeLayout = new RelativeLayout(this);
        dbHelper = new DBHelper(this);
        Button button = new Button(this);
        Display display =  getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        if (!setDate){
            currDate=String.valueOf(gregorianCalendar.get(Calendar.DATE))+ "." + String.valueOf(gregorianCalendar.get(Calendar.MONTH)+1)+"."+
                    String.valueOf(gregorianCalendar.get(Calendar.YEAR));
        }
        button.setText(currDate);
        button.setVisibility(View.VISIBLE);
        Typeface typeface=Typeface.create(Typeface.SERIF, Typeface.ITALIC);
        button.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/cac-champagne.ttf"));
        button.setTextSize(50);
        button.setY(20);
        button.setX(metricsB.widthPixels/4);
        button.getBackground().setAlpha(0);
        Button buttonForward =  new Button(this);
        if (counter==1){
            buttonForward.setVisibility(View.VISIBLE);
            buttonForward.setEnabled(true);
        }else{
            buttonForward.setVisibility(View.GONE);
            buttonForward.setEnabled(false);
        }
        buttonForward.setX(metricsB.widthPixels/1.3f);
        buttonForward.setY(40);
        buttonForward.setBackgroundResource(R.drawable.strelka);
        buttonForward.setId(Integer.parseInt("1"));
        buttonForward.setOnClickListener(this);
        Button buttonBack =  new Button(this);
        if (counter==10) {
            buttonBack.setVisibility(View.VISIBLE);
            buttonBack.setEnabled(true);
        }else{
            buttonBack.setVisibility(View.GONE);
            buttonBack.setEnabled(false);
        }
        buttonBack.setX(0);
        buttonBack.setY(40);
        buttonBack.setBackgroundResource(R.drawable.strelka1);
        buttonBack.setOnClickListener(this);
        buttonBack.setId(Integer.parseInt("2"));
        float  p = metricsB.heightPixels/5f;
        for (int i=0; i<time.length; ++i){
            EditTime text = new EditTime(this, i, counter, currDate, dbHelper) ;
            text.setEnabled(true);
            text.setTextColor(Color.BLACK);
            text.setTypeface(typeface);
            text.setTextSize(20);
            text.setText("");
            text.setBackgroundColor(Color.TRANSPARENT);
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text.setHeight(170);
            text.setWidth(220);
            text.setHint("00:00");
            text.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
            text.setInputType(4);
            text.setVisibility(View.VISIBLE);
            text.setY(p);
            text.setX(metricsB.widthPixels/1.45f);
            text.setZ(10);
            time[i]=text;
            time[i].setOnFocusChangeListener(new MyTextWatcher(this, i, counter, currDate, dbHelper));
            p = p+metricsB.heightPixels/8f;
        }
        myThread = new MyThread(time, dbHelper, counter, currDate);
        myThread.execute();
        p = metricsB.heightPixels/5.5f;
        for (int i=0; i<editTexts.length; ++i){
            MyEdittext textView = new MyEdittext(this, time[i], i, counter, currDate, dbHelper) ;
            textView.setEnabled(true);
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(typeface);
            textView.setTextSize(20);
            textView.setBackgroundColor(Color.TRANSPARENT);
            textView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(35) });
            textView.setHeight(metricsB.heightPixels/10+30);
            textView.setWidth((int) ((int) metricsB.widthPixels/1.7f));
            textView.setVisibility(View.VISIBLE);
            textView.setY(p);
            textView.setZ(10);
            editTexts[i]=textView;
            editTexts[i].setOnFocusChangeListener(new FocusChangerForMyEdittext(i, counter, currDate, dbHelper, time[i]));
            p = p+metricsB.heightPixels/8;
        }
        myThread = new MyThread(editTexts, dbHelper, counter, currDate);
        myThread.execute();
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/cac-champagne.ttf");
        Draw draw = new Draw(this, typeface1, counter, display);
        draw.setZ(1);
        button.setOnClickListener(this);
        for (int i=0; i<editTexts.length; ++i){
            relativeLayout.addView(editTexts[i]);
        }
        for (int i=0; i<time.length; ++i){
            relativeLayout.addView(time[i]);
        }
        relativeLayout.addView(button);
        relativeLayout.addView(buttonForward);
        relativeLayout.addView(buttonBack);
        draw.setBackgroundResource(R.drawable.back);
        relativeLayout.addView(draw);
        setContentView(relativeLayout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AlarmManager mgr=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(this, DateRecieve.class);
        PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 2000, pi);

    }
    private void addToSQL(boolean flag, EditText[] editTexts, int count){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        for (int i=0; i<editTexts.length; ++i){
            ContentValues cv = new ContentValues();
            String task;
            String time;
            String t = editTexts[i].getText().toString();
            String strDate = currDate;
            if (!( t.equals(""))) {
                if (flag) {
                    task = editTexts[i].getText().toString();
                    cv.put("value", String.valueOf(i));
                    cv.put("task", task);
                    cv.put("page", count);
                    cv.put("date", strDate );
                    Cursor cursor = database.query("myDb", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        if ((cursor.getString(cursor.getColumnIndexOrThrow("value")).equals(String.valueOf(i))) &&
                                (cursor.getString(cursor.getColumnIndexOrThrow("page")).equals(String.valueOf(count)))&&
                                (cursor.getString(cursor.getColumnIndexOrThrow("date")).equals(String.valueOf(strDate)))){
                            database.delete("myDb", "value=? AND page=? AND date=?", new String[] {String.valueOf(i),String.valueOf(count), strDate});
                            database.insert("myDb", null, cv);
                            break;
                        }
                    }
                    if (cursor.isAfterLast()) {
                        database.insert("myDb", null, cv);
                    } else {
                        continue;
                    }
                } else {
                        time = editTexts[i].getText().toString();
                        cv.put("time", time);
                        cv.put("page", count);
                        cv.put("date", strDate );
                        Cursor cursor = database.query("myDb", null, null, null, null, null, null);
                        while (cursor.moveToNext()) {
                            if ((cursor.getString(cursor.getColumnIndexOrThrow("value")).equals(String.valueOf(i))) &&
                                    (cursor.getString(cursor.getColumnIndexOrThrow("page")).equals(String.valueOf(count)))&&
                                    (cursor.getString(cursor.getColumnIndexOrThrow("date")).equals(String.valueOf(strDate)))){
                                database.update("myDb", cv,  "value=? AND page=? AND date=?", new String[] {String.valueOf(i),String.valueOf(count), strDate});
                                break;
                            }
                        }
                }
            } else{
                if (flag) {
                    try {
                        Cursor cursor = database.query("myDb", null, null, null, null, null, null);
                        while (cursor.moveToNext()) {
                            if ((cursor.getString(cursor.getColumnIndexOrThrow("value")).equals(String.valueOf(i))) &&
                                    (cursor.getString(cursor.getColumnIndexOrThrow("page")).equals(String.valueOf(count)))&&
                                    (cursor.getString(cursor.getColumnIndexOrThrow("date")).equals(String.valueOf(strDate)))){
                                database.delete("myDb",  "value=? AND page=? AND date=?", new String[] {String.valueOf(i),String.valueOf(count), strDate});
                                break;
                            }
                        }
                    }catch (Exception e){
                       continue;
                    }
                }
            }
  //      dbHelper.close();
        }
    }
    @Override
    protected void onDestroy() {
        if ((!SwipedRight)&&(!SwipedLeft)){
            addToSQL(true, editTexts, 1);
            addToSQL(false, time, 1);
        }else if ((SwipedRight)&&(!SwipedLeft)) {
            addToSQL(true, editTexts, 10);
            addToSQL(false, time, 10);
        }
        else{}
        super.onDestroy();

    }
    @Override
    protected void onStop() {
        addToSQL(true, editTexts, counter);
        addToSQL(false, time, counter);
        super.onStop();

    }
    protected void onPause() {
        addToSQL(true, editTexts, counter);
        addToSQL(false, time, counter);
        super.onPause();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 1:{
                SwipeRight();
                break;
            }
            case 2:{
                SwipeLeft();
                break;
            }
            default: {
                GregorianCalendar calendar = new GregorianCalendar();
                int myYear = calendar.get(Calendar.YEAR);
                int myMonth = calendar.get(Calendar.MONTH);
                int myDay = calendar.get(Calendar.DATE);
                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String year = String.valueOf(selectedYear);
                        String month = String.valueOf(selectedMonth + 1);
                        String day = String.valueOf(selectedDay);
                        currDate = (day + "." + month + "." + year);
                        setDate = true;
                        createGui();
                    }
                };
                DatePickerDialog datePicker = new DatePickerDialog(this,
                        R.style.Theme_AppCompat_Light_Dialog_Alert,
                        datePickerListener,
                        myYear,
                        myMonth,
                        myDay);
                datePicker.setTitle("Select the date");
                datePicker.show();
                break;
            }
        }
    }
    private void SwipeLeft() {
        counter = counter / 10;
        if (counter == 1) {
            addToSQL(true, editTexts, 10);
            addToSQL(false, time, 10);
            SwipedLeft = true;
            SwipedRight = false;
            createGui();
        } else {
            counter = 1;
        }

    }
    private void SwipeRight(){
        counter = counter*10;
        if (counter>10){
            counter=10;
        }
        else {
            addToSQL(true, editTexts, 1);
            addToSQL(false, time, 1);
            SwipedRight=true;
            SwipedLeft=false;
            createGui();
        }
    }
    public void onBackPressed() {
        if ((!SwipedRight)&&(!SwipedLeft)){
            addToSQL(true, editTexts, 1);
            addToSQL(false, time, 1);
        }else if ((SwipedRight)&&(!SwipedLeft)) {
            addToSQL(true, editTexts, 10);
            addToSQL(false, time, 10);
        }
        else{}
        moveTaskToBack(true);
    }
}

