package com.yuahp.falling;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Menu mMenu;
    Switch serviceSwitch;
    LinearLayout servicelinearLayout;
    TextView textview;
    ListView stat;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Adapter adapter;
    FileInputStream fileInputStream;
    Thread thread = new Thread();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = this.getSharedPreferences(getDefaultSharedPreferencesName(this), Context.MODE_MULTI_PROCESS);
        editor = sharedPref.edit();

        if(sharedPref.getBoolean("first", true)) {
            editor.putBoolean("first", false);
            editor.putBoolean("Gps_use", true);
            editor.putString("location", "2");
            editor.putString("weather_delay", "2");
            editor.putString("snow_select_image", "0");
            editor.putBoolean("size", true);
            editor.putBoolean("effect_use", true);
            editor.putBoolean("boot_start", true);
            editor.putBoolean("auto_service", true);
            editor.apply();
            editor.commit();

            Intent itn = new Intent(MainActivity.this, FirstGuide.class);
            startActivity(itn);
        }

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        serviceSwitch = (Switch) findViewById(R.id.serviceonoff);
        servicelinearLayout = (LinearLayout) findViewById(R.id.day_bar);
        textview = (TextView) findViewById(R.id.day_date);
        stat = (ListView) findViewById(R.id.day_stat);

        textview.setText(getResources().getText(doDayOfWeek()));

        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable drawable = wallpaperManager.getDrawable();
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.wallpaper);
        linearLayout.setBackground(drawable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        }

        Button toolbar_button = (Button)findViewById(R.id.toolbar_button);
        toolbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itn2 = new Intent(MainActivity.this, Setting.class);
                startActivityForResult(itn2, 0);
            }
        });


        adapter = new Adapter(this);
        stat.setAdapter(adapter);
        stat.setBackgroundColor(Color.rgb(255, 255, 255));

        boolean isAutoOn = sharedPref.getBoolean("auto_service", true);

        if (!(isServiceRunning(AppService.class)) && isAutoOn) {
            Intent svi = new Intent(MainActivity.this, AppService.class);
            startService(svi);
        }

        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent svi = new Intent(MainActivity.this, AppService.class);
                switch (buttonView.getId()) {
                    case R.id.serviceonoff:
                        if (isChecked) {
                            if (!(isServiceRunning(AppService.class))) {
                                startService(svi);
                            }
                            servicelinearLayout.setBackgroundColor(Color.rgb(26, 188, 156));
                        } else {
                            if ((isServiceRunning(AppService.class))) {
                                stopService(svi);
                            }
                            servicelinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));
                        }
                }
            }
        });

        mHandler.sendEmptyMessage(0);
        mHandler2.sendEmptyMessage(0);
    }

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            if (!(isServiceRunning(AppService.class))) {
                if(serviceSwitch.isChecked()){
                    serviceSwitch.setChecked(false);
                }
                servicelinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));
            } else {
                if(!serviceSwitch.isChecked()){
                    serviceSwitch.setChecked(true);
                }
                servicelinearLayout.setBackgroundColor(Color.rgb(26, 188, 156));
            }
            mHandler.sendEmptyMessageDelayed(0, 5000);
        }
    };

    Handler mHandler2 = new Handler(){
        public void handleMessage(Message msg) {
            adapter.array.clear();
            adapter.array2.clear();

            try {
                fileInputStream = openFileInput("weather.json");
                byte[] buffer = new byte[fileInputStream.available()];
                fileInputStream.read(buffer);
                fileInputStream.close();

                String string = new String(buffer);
                JSONObject obj = new JSONObject(string);

                adapter.array.add(obj.getString("weather"));
                adapter.array.add(obj.getInt("cloudy"));
                adapter.array.add(Math.round(((obj.getDouble("temp") - 273.15) * 1000)) / 1000.0);
                adapter.array.add(obj.getString("city"));

                adapter.array2.add(R.drawable.ic_wb_sunny_24dp);
                adapter.array2.add(R.drawable.ic_wb_cloudy_24dp);
                if(Math.round(obj.getDouble("temp") - 273.15) >= 30) {
                    adapter.array2.add(R.drawable.ic_thermometer_green_24dp);
                } else if( Math.round(obj.getDouble("temp") - 273.15) >= 15) {
                    adapter.array2.add(R.drawable.ic_thermometer_orange_24dp);
                } else if(Math.round(obj.getDouble("temp") - 273.15) >= 0) {
                    adapter.array2.add(R.drawable.ic_thermometer_red_24dp);
                }
                adapter.array2.add(R.drawable.ic_location_24dp);
                adapter.notifyDataSetChanged();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mHandler2.sendEmptyMessageDelayed(0, 5000);
        }
    };

    public void onResume() {
        super.onResume();

        if (!(isServiceRunning(AppService.class))) {
            serviceSwitch.setChecked(false);
            servicelinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));
        } else {
            serviceSwitch.setChecked(true);
            servicelinearLayout.setBackgroundColor(Color.rgb(26, 188, 156));
        }
    }

    public void onDestroy() {
        super.onDestroy();

        mHandler.removeMessages(0);
        mHandler2.removeMessages(0);
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
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

    private int doDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        int strWeek = 0;

        int nWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (nWeek == 1) {
            strWeek = R.string.sunday;
        } else if (nWeek == 2) {
            strWeek = R.string.monday;
        } else if (nWeek == 3) {
            strWeek = R.string.tuesday;
        } else if (nWeek == 4) {
            strWeek = R.string.wednesday;
        } else if (nWeek == 5) {
            strWeek = R.string.thursday;;
        } else if (nWeek == 6) {
            strWeek = R.string.friday;
        } else if (nWeek == 7) {
            strWeek = R.string.saturday;
        }

        return strWeek;
    }

}
