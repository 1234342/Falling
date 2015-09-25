package com.kudosku.falling;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Surface extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private Bitmap snow16;
    private Bitmap snow8;
    private Bitmap snow2_16;
    private Bitmap snow2_8;
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
    private int ranx = 0;
    private int ro = 0;
    private int x,y = 0;
    private int sx,sy;
    Matrix matrix = new Matrix();
    private List<SurfaceInit> list = new ArrayList<SurfaceInit>();

    public Surface(Context context) {
        super(context);
        context_ = context;
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        snow16 = BitmapFactory.decodeResource(getResources(), R.drawable.snow_16);
        snow8 = BitmapFactory.decodeResource(getResources(), R.drawable.snow_8);
        snow2_16 = BitmapFactory.decodeResource(getResources(), R.drawable.snow_2_16);
        snow2_8 = BitmapFactory.decodeResource(getResources(), R.drawable.snow_2_8);
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

                    int i;

                    for(i = 0; i < list.size() / 6; i++) {
                        SurfaceInit Init = list.get(i);
                        matrix.reset();
                        matrix.postRotate(ro, snow16.getWidth() / 2, snow16.getHeight() / 2);
                        matrix.postTranslate(Init.x, Init.y);
                        canvas.drawBitmap(snow16, matrix, null);
                    }
                    for(; i < list.size() / 6 * 3; i++) {
                        SurfaceInit Init = list.get(i);
                        matrix.reset();
                        matrix.postRotate(ro, snow8.getWidth() / 2, snow8.getHeight() / 2);
                        matrix.postTranslate(Init.x, Init.y);
                        canvas.drawBitmap(snow8, matrix, null);
                    }
                    for(; i < list.size() / 6 * 4; i++) {
                        SurfaceInit Init = list.get(i);
                        matrix.reset();
                        matrix.postRotate(ro, snow2_16.getWidth() / 2, snow2_16.getHeight() / 2);
                        matrix.postTranslate(Init.x, Init.y);
                        canvas.drawBitmap(snow2_16, matrix, null);
                    }
                    for(; i < list.size(); i++) {
                        SurfaceInit Init = list.get(i);
                        matrix.reset();
                        matrix.postRotate(ro, snow2_8.getWidth() / 2, snow2_8.getHeight() / 2);
                        matrix.postTranslate(Init.x, Init.y);
                        canvas.drawBitmap(snow2_8, matrix, null);
                    }

                    canvas.drawText("개수:" + list.size(), 100, 200, paint);

                    make();
                    move();

                    ro++;

                    if (ro >= 360) {
                        ro = 0;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    /*public void doDraw(Canvas c, int y){
        c.drawBitmap(img, matrix, null);


        //c.drawBitmap(img, ranx2, y, null);
        //c.drawBitmap(img, ranx3, y, null);
        //c.drawBitmap(img, (int)(Math.random() * dvcw + 1), y, null);
    }*/

    public void make() {
        int x = (int)(Math.random() * dvcw + 1);
        int y = -30;
        int speedX = (int)(Math.random() * 20 + 1);
        int speedY = (int)(Math.random() * 20 + 1);

        SurfaceInit Init = new SurfaceInit(x, y, speedX, speedY);

        if (list.size() <= AppService.temp) {
            list.add(Init);
        }
    }

    public void move() {
        for(SurfaceInit Init : list) {
            //Init.x += Init.speedX;
            Init.y += Init.speedY;

            if(Init.y >= dvch +20 ) {
                Init.x = (int)(Math.random() * dvcw + 1);
                Init.speedY = (int)(Math.random() * 20 + 1);
                Init.y = 0;
            }
        }
    }

}
