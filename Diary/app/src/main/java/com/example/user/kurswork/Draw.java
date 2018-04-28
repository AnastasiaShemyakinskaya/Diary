package com.example.user.kurswork;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;


class Draw extends View {
    private Paint paint = new Paint();
    private Integer c;
    private Display display;
    public Draw(Context context, Typeface typeface, Integer counter, Display display1) {
        super(context);
        display=display1;
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setTypeface(typeface);
        c=counter;
    }

    public void onDraw(Canvas canvas){
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        float y = metricsB.heightPixels/10+20;
        canvas.drawLine(0, y, metricsB.widthPixels, y, paint);
        y=y+100;
        for (int i=0; i<6; i++) {
            canvas.drawLine(0, y, metricsB.widthPixels, y, paint);
            y=y+metricsB.heightPixels/8;
        }
        canvas.drawLine(metricsB.widthPixels/1.5f, metricsB.heightPixels/10+20, metricsB.widthPixels/1.5f, y-metricsB.heightPixels/8, paint);
        paint.setStrokeWidth(5);
        paint.setTextSize(80);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("ToDo list",metricsB.widthPixels/5.9f,metricsB.heightPixels/6.2f, paint);
        canvas.drawText("Time",metricsB.widthPixels/1.3f, metricsB.heightPixels/6.2f, paint);
    }
}