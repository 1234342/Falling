package com.kudosku.falling;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AppService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
