package com.kudosku.falling;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);

        if (!(isServiceRunning(AppService.class)) ) {
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
        getMenuInflater().inflate(R.menu.credits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();



         //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.settings:
                Intent itn2 = new Intent(MainActivity.this, Setting.class);
                startActivity(itn2);
                return true;
            case R.id.credits:
                Intent itn3 = new Intent(MainActivity.this, Credits.class);
                startActivity(itn3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
