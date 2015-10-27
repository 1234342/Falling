package com.yuahp.falling;

import android.graphics.Bitmap;

public class SurfaceInit {
    int x;
    int y;
    int speedX;
    int speedY;
    Bitmap imgbit;
    int weather;

    public SurfaceInit(int x, int y, int speedX, int speedY, Bitmap imgbit, int weather) {

        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.imgbit = imgbit;
        this.weather = weather;
    }
}
