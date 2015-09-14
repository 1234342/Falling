package com.kudosku.falling;

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
        Toast.makeText(getApplicationContext(), "서비스를 시작", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    }

    public void onDestroy(){
        Toast.makeText(getApplicationContext(), "서비스를 중지", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
