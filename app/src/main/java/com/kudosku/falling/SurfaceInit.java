package com.kudosku.falling;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SurfaceInit {
    int x;
    int y;
    int speedX;
    int speedY;
    int cloudy;
    int snow;
    int rain;
    int weather;
    Bitmap imgbit;

    public SurfaceInit(int x, int y, int speedX, int speedY, Bitmap imgbit, int cloudy, int snow, int rain, int weather) {

        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.imgbit = imgbit;
        this.cloudy = cloudy;
        this.snow = snow;
        this.rain = rain;
        this.weather = weather;
    }
}
