package com.kudosku.falling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver {

    @Override

    public void onReceive(Context context, Intent intent) {
        String action= intent.getAction();

        if( action.equals("android.intent.action.BOOT_COMPLETED") ){
            Intent i= new Intent(context, AppService.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}
