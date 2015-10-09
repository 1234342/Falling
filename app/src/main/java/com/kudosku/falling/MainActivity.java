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

public class MainActivity extends AppCompatActivity {

    Menu mMenu;

    public Context setContext() {
        Context context = this.getBaseContext();
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(sharedPref.getBoolean("first",true)) {
            editor.putBoolean("first",false);
            editor.commit();

            Intent itn = new Intent(MainActivity.this, FirstGuide.class);
            startActivity(itn);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        boolean isAutoOn = sharedPref.getBoolean("auto_service", true);

        if (!(isServiceRunning(AppService.class)) && isAutoOn) {
            Intent svi = new Intent(MainActivity.this, AppService.class);
            startService(svi);
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

        mMenu = menu;

        MenuItem svioff = mMenu.findItem(R.id.serviceoff);
        MenuItem svion = mMenu.findItem(R.id.serviceon);

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

        MenuItem svioff = menu.findItem(R.id.serviceoff);
        MenuItem svion = menu.findItem(R.id.serviceon);

        if (isServiceRunning(AppService.class)) {
            svion.setVisible(false);
            svioff.setVisible(true);
        } else if (!isServiceRunning(AppService.class)) {
            svioff.setVisible(false);
            svion.setVisible(true);
        }

        return true;
    }
}
