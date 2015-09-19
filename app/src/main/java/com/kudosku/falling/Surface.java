package com.kudosku.falling;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.test.RenamingDelegatingContext;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

public class Surface extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Bitmap img;
    private Bitmap img2;
    private Thread thread;
    int width,height;
    private Context context_;
    boolean retry = true;
    boolean running = false;
    private Display display;
    private int dvch,dvcw;
    private Random random;
    private Paint paint;
    private int ranx,ranx2,ranx3 = 0;

    private int x,y = 0;
    private int sx,sy;

    public Surface(Context context) {
        super(context);
        context_ = context;
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        img = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        img2 = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        dvch = display.getHeight();
        dvcw = display.getWidth();
        paint = new Paint();
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(this);
    }

    public void setRunning(boolean run){
        running = run;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setRunning(true);
        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        dvch = height;
        dvcw = width;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        retry = true;
        setRunning(false);
        while(retry) {
            try{
                retry = false;
                thread.join();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    @Override
    public void run() {
        Canvas canvas;

        while (running) {
            canvas = null;

            try {
                canvas = holder.lockCanvas(null);

                synchronized (holder) {
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                    if (ranx == 0 || ranx2 == 0) {
                        ranx = (int)(Math.random() * dvcw + 1);
                        ranx2 = (int)(Math.random() * dvcw + 1);
                        ranx3 = (int)(Math.random() * dvcw + 1);
                    }

                    for(int i=0 ; i < 5 ; i++) {
                        doDraw(canvas, y);
                        doText(canvas, y);
                    }
                    y++;

                    if (y >= dvch +20 ) {
                        y = 0;
                        ranx = (int)(Math.random() * dvcw + 1);
                        ranx2 = (int)(Math.random() * dvcw + 1);
                        ranx3 = (int)(Math.random() * dvcw + 1);
                    }
                }

            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void doMove(){
        ranx = (int)(Math.random() * dvcw + 1);

    }

    public void doText(Canvas c, int y){
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setTextSize(75);
        c.drawText("뭘 보는가 닝겐?", ranx, y, paint);
    }

    public void doDraw(Canvas c, int y){
        c.drawBitmap(img, ranx, y, null);
        c.drawBitmap(img, ranx2, y, null);
        c.drawBitmap(img, ranx3, y, null);
        c.drawBitmap(img, (int)(Math.random() * dvcw + 1), y, null);
    }
}
