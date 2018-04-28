package com.example.user.kurswork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DateRecieve extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String today = formatter.format(date);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        String currDate = String.valueOf(gregorianCalendar.get(Calendar.DATE)) + "." + String.valueOf(gregorianCalendar.get(Calendar.MONTH) + 1) + "." +
                String.valueOf(gregorianCalendar.get(Calendar.YEAR));
        Intent intent1 = new Intent(context, MyService.class);
        intent1.putExtra("currDate", currDate);
        intent1.putExtra("time", today);
        context.startService(intent1);
    }
}
