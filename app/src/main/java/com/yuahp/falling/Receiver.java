package com.yuahp.falling;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences(getDefaultSharedPreferencesName(context), context.MODE_PRIVATE);
        Boolean isBootStart = sharedPref.getBoolean("boot_start", true);
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && isBootStart) {
            Intent i= new Intent(context, AppService.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(i);
        }
        if(intent.getAction().equals("service_check")){
            Intent i= new Intent(context, Adapter.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }
}
