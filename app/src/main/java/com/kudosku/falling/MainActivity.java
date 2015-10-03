package com.kudosku.falling;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Runnable {

    Thread thread;
    Boolean Running = false;
    Boolean retry = false;
    MenuItem svion;
    MenuItem svioff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAutoOn = sharedPref.getBoolean("auto_service", true);

        if (!(isServiceRunning(AppService.class)) && isAutoOn) {
            Intent svi = new Intent(MainActivity.this, AppService.class);
            startService(svi);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        retry = true;
        Running = false;
        while(retry) {
            try{
                retry = false;
                thread.join();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public boolean isServiceRunning (Class<?> serviceclass) {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceclass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {

            case KeyEvent.KEYCODE_BACK:

                String exitTitle = getResources().getString(R.string.app_name);
                String exitMsg = getResources().getString(R.string.exit_summary);
                String exitbtnYes = getResources().getString(R.string.yes);
                String exitbtnNo = getResources().getString(R.string.no);

                new AlertDialog.Builder(MainActivity.this)
                    .setTitle(exitTitle)
                    .setMessage(exitMsg)
                    .setNegativeButton(exitbtnYes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int whith) {
                            moveTaskToBack(true);
                            finish();
                        }
                    })
                    .setPositiveButton(exitbtnNo, null)
                    .show();
                    return true;
            case KeyEvent.KEYCODE_MENU:
                return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        getMenuInflater().inflate(R.menu.serviceoff, menu);
        getMenuInflater().inflate(R.menu.serviceon, menu);

        svioff = menu.findItem(R.id.serviceoff);
        svion = menu.findItem(R.id.serviceon);

        if (isServiceRunning(AppService.class)) {
            svion.setVisible(false);
            svioff.setVisible(true);
        } else if (!isServiceRunning(AppService.class)) {
            svioff.setVisible(false);
            svion.setVisible(true);
        }

        if(Running = false) {
            Running = true;
            thread = new Thread();
            thread.start();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    Intent svi = new Intent(MainActivity.this, AppService.class);

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.settings:
                Intent itn2 = new Intent(MainActivity.this, Setting.class);
                startActivity(itn2);
                return true;
            case R.id.serviceoff:
                stopService(svi);
                invalidateOptionsMenu();
                return true;
            case R.id.serviceon:
                startService(svi);
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (isServiceRunning(AppService.class)) {
            svion.setVisible(false);
            svioff.setVisible(true);
        } else if (!isServiceRunning(AppService.class)) {
            svioff.setVisible(false);
            svion.setVisible(true);
        }

        return true;
    }
    @Override
    public void run() {
        if (Running) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("sviStat", getApplicationContext().MODE_ENABLE_WRITE_AHEAD_LOGGING);

                    if (sharedPref.getBoolean("sviStat", false) == true) {
                        svion.setVisible(false);
                        svioff.setVisible(true);
                    } else {
                        svioff.setVisible(false);
                        svion.setVisible(true);
                    }
                }
            });
        }
    }
}
