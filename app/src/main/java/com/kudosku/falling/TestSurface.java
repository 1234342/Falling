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

public class TestSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable {

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
    private Random random = new Random();
    private Paint paint;
    private int ranx = 0;
    private int ro = 0;
    private int x,y = 0;
    private int sx,sy;
    Matrix matrix = new Matrix();
    List<SurfaceInit> list = new ArrayList<SurfaceInit>();

    public TestSurface(Context context) {
        super(context);
        context_ = context;
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        img2 = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        dvch = display.getHeight();
        dvcw = display.getWidth();
        list = new ArrayList<SurfaceInit>();
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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        retry = true;
        setRunning(false);
        while(retry) {
            try{
                List<SurfaceInit> list = null;
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

                    for(int i=0; i<list.size(); i++) {
                        SurfaceInit Init = list.get(i);
                        if(Init.weather == 3) { // leave
                            matrix.reset();
                            matrix.postRotate(ro, Init.imgbit.getWidth() / 2, Init.imgbit.getHeight() / 2);
                            matrix.postTranslate(Init.x, Init.y);
                            canvas.drawBitmap(Init.imgbit, matrix, null);
                        }
                        if(Init.weather == 1 || Init.weather == 2) { // rain , cherry
                            canvas.drawBitmap(Init.imgbit, Init.x, Init.y, null);
                        }
                        if(Init.weather == 4) { // snow
                            matrix.reset();
                            matrix.postRotate(ro);
                            matrix.postTranslate(Init.x, Init.y);
                            canvas.drawBitmap(Init.imgbit, matrix, null);
                        }
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

    public void make() {
        int x = random.nextInt(dvcw);
        int y = -30;
        int speedX = 0;
        int speedY = 0;
        Bitmap imgbit = null;
        int cloudy = Controller.cloudy;
        int snow = Controller.snow;
        int rain = Controller.rain;
        int month = Controller.month;
        int temp = Controller.temp;
        int weather = 0;

        if(cloudy <= 20 && snow == 0 && rain == 0 && temp != 0) {
            if(month == 11) {
                imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
                speedX = 0;
                speedY = random.nextInt(20) + 1;
                weather = 4;
                if (temp *4 - list.size() > 0) {
                    SurfaceInit Init = new SurfaceInit(x, y, speedX, speedY, imgbit, cloudy, snow, rain, weather);

                    list.add(Init);
                }
            } else if(month >= 9){
                imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.fallen_leaves_1_16);
                speedY = random.nextInt(10) + 1;

                if (random.nextInt(2) + 1 == 1) {
                    speedX = random.nextInt(3) + 1;
                } else {
                    speedX = -random.nextInt(3) + -1;
                }
                weather = 3;
                if (list.size() <= temp *2) {
                    SurfaceInit Init = new SurfaceInit(x, y, speedX, speedY, imgbit, cloudy, snow, rain, weather);

                    list.add(Init);
                }
            } else if(month >= 7) {
                imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
                speedX = 0;
                speedY = random.nextInt(20) + 1;
                weather = 2;
                if (list.size() <= temp *2) {
                    SurfaceInit Init = new SurfaceInit(x, y, speedX, speedY, imgbit, cloudy, snow, rain, weather);

                    list.add(Init);
                }
            } else if(month <= 3) {
                imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.cherry_blossom_1_16);
                speedY = random.nextInt(10) + 1;

                if (random.nextInt(2) + 1 == 1) {
                    speedX = random.nextInt(3) + 1;
                } else {
                    speedX = -random.nextInt(3) + -1;
                }
                weather = 1;
                if (list.size() <= temp *2) {
                    SurfaceInit Init = new SurfaceInit(x, y, speedX, speedY, imgbit, cloudy, snow, rain, weather);

                    list.add(Init);
                }
            }
            //season image
        } else if (cloudy > 20 && temp != 0) {
            //cloud image
        }

        if (list.size() <= temp *2 && snow == 0) {
            if(rain >= 1) {
                int imgran = random.nextInt(9) + 1;
                if(imgran == 1) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.rain_1_64);
                }
                if(imgran == 2) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.rain_2_64);
                }
                if(imgran == 3) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.rain_3_64);
                }
                speedY = random.nextInt(30) + 1;
                weather = 2;
                System.out.print("rain");

                SurfaceInit Init = new SurfaceInit(x, y, speedX, speedY, imgbit, cloudy, snow, rain, weather);

                list.add(Init);
            }
        } else if (temp *4 - list.size() > 0 && snow >= 1) {
            if(snow >= 1) {
                int imgran = random.nextInt(6) + 1;
                if (imgran == 1) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.snow_1_16);
                }
                if (imgran == 2) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.snow_1_8);
                }
                if (imgran == 3) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.snow_2_16);
                }
                if (imgran == 4) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.snow_2_8);
                }
                if (imgran == 5) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.snow_3_16);
                }
                if (imgran == 6) {
                    imgbit = BitmapFactory.decodeResource(getResources(), R.drawable.snow_3_8);
                }
                speedY = random.nextInt(20) + 1;
                weather = 4;
                System.out.print("snow");

                SurfaceInit Init = new SurfaceInit(x, y, speedX, speedY, imgbit, cloudy, snow, rain, weather);

                list.add(Init);
            }
        }
    }

    public void move() {
        for(int i=0; i<list.size(); i++) {
            SurfaceInit Init = list.get(i);
            if (Init.speedX != 0) {
                Init.x += Init.speedX;
            }
            Init.y += Init.speedY;

            if(Init.y >= dvch +20 ) {
                Init.imgbit.recycle();
                list.remove(Init);
            }
            if(Init.x >= dvcw +20 ) {
                Init.imgbit.recycle();
                list.remove(Init);
            }
            if(Init.x <= -20 ) {
                Init.imgbit.recycle();
                list.remove(Init);
            }
        }

    }

}