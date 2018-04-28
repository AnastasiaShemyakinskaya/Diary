package com.example.user.kurswork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MyService extends Service {
    NotificationManager nm;
    DBHelper db;
    ExecutorService es;
    Integer NOTIFICATION_ID = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        es = Executors.newFixedThreadPool(1);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        db = new DBHelper(getApplicationContext());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        es.execute(new MyRunner(intent));
        stopSelf();
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    void sendNotif(Intent intent) {
        String text = "";
        String currDate = intent.getExtras().getString("currDate");
        String today = intent.getExtras().getString("time");
        SQLiteDatabase dbHelper = db.getReadableDatabase();
        Cursor c = null;
        if (dbHelper != null) {
            try {
                c = dbHelper.query("myDb", new String[]{"task"}, "date=? AND time=?", new String[]{currDate, today}, null, null, null);
                c.moveToFirst();
                text = c.getString(c.getColumnIndexOrThrow("task"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    if (c.isLast()) {
                        c.close();
                    }
                    c.close();
                }
            }
            if (text == null || text.equals("")) {
                text = "";
            } else {
                Intent resultIntent = new Intent(this, MainActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Нужно сделать")
                                .setContentText(text)
                                .setContentIntent(resultPendingIntent);
                builder.setAutoCancel(true);
                Notification notification = builder.build();
                ++NOTIFICATION_ID;
                nm.notify(NOTIFICATION_ID, notification);

            }
        }
        db.close();
    }
    class MyRunner implements Runnable{
        Intent intent;
        MyRunner(Intent intent1){
            intent = intent1;
        }
        @Override
        public void run() {
            sendNotif(intent);
        }

    }
}