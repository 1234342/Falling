package com.kudosku.falling;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class AppService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(1, new Notification());
        Toast.makeText(getApplicationContext(), "서비스를 시작합니다.", Toast.LENGTH_LONG).show();

        return Service.START_STICKY;
    }

    public void onDestroy(){
        stopForeground(true);
        Toast.makeText(getApplicationContext(), "서비스를 중지합니다.", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
