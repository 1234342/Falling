package com.kudosku.falling;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

public class AppService extends Service {

    private Notification mNoti;
    SurfaceView surview;
    private GLSurfaceView glSurfaceView;
    int id;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent mPI = PendingIntent.getActivity(
                getApplicationContext(), 0, new Intent(getApplicationContext(),MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager mNty = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNoti = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.app_touch))
                .setSmallIcon(R.drawable.splash)
                .setTicker(getResources().getString(R.string.app_alive))
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(false)
                .setContentIntent(mPI)
                .build();

        //mNty.notify(3939, mNoti);
        startForeground(3939, mNoti);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_start), Toast.LENGTH_LONG).show();

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
                );

        //surview = new Surface(this);
        glSurfaceView = new GLSurfaceView(this);

        if (MainActivity.supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            glSurfaceView.setEGLContextClientVersion(2);

            // Assign our renderer.
            glSurfaceView.setRenderer(new GLRenderer());
        }

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //windowManager.addView(surview, params);
        windowManager.addView(glSurfaceView, params);

        return Service.START_STICKY;
    }

    public void onDestroy(){

        WindowManager windowManager = ((WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE));
        //windowManager.removeView(surview);
        windowManager.removeView(glSurfaceView);

        stopForeground(true);
        Toast.makeText(getBaseContext(), getResources().getString(R.string.service_stop), Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
