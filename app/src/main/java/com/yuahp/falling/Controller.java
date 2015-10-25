package com.yuahp.falling;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Controller extends AppCompatActivity implements View.OnClickListener,Switch.OnCheckedChangeListener {

    Switch s1;
    Button b1,b2,b3;
    SurfaceView testsurview;
    WindowManager wm;
    static int snow = 0;
    static int rain = 0;
    static int cloudy = 0;
    static int month = 0;
    static int temp = 0;
    static String snow_setting;
    static int snow_Set = R.drawable.snow_1_16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);

        SharedPreferences sharedPref = this.getSharedPreferences(getDefaultSharedPreferencesName(this), this.MODE_PRIVATE);

        snow_setting = sharedPref.getString("snow_select_image", "0");

        if(snow_setting.equals("0")) {
            snow_Set = R.drawable.snow_1_16;
        }
        if(snow_setting.equals("1")) {
            snow_Set = R.drawable.snow_2_16;
        }
        if(snow_setting.equals("2")) {
            snow_Set = R.drawable.snow_3_16;
        }

        snow_setting = sharedPref.getString("snow_select_image", "0");

        if(snow_setting.equals("0")) {
            snow_Set = R.drawable.snow_1_16;
        }
        if(snow_setting.equals("1")) {
            snow_Set = R.drawable.snow_2_16;
        }
        if(snow_setting.equals("2")) {
            snow_Set = R.drawable.snow_3_16;
        }

        s1 = (Switch) findViewById(R.id.onoffswitch);
        b1 = (Button)findViewById(R.id.snowbutton);
        b2 = (Button)findViewById(R.id.rainbutton);
        b3 = (Button)findViewById(R.id.reset);

        s1.setOnCheckedChangeListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snowbutton:
                cloudy = 0;
                temp = 10;
                snow = 30;
                rain = 0;
                Toast.makeText(Controller.this, "snow", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rainbutton:
                cloudy = 0;
                temp = 20;
                rain = 30;
                snow = 0;
                Toast.makeText(Controller.this, "rain", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reset:
                snow = 0;
                rain = 0;
                cloudy = 0;
                temp = 0;
                Toast.makeText(Controller.this, "reset", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.onoffswitch:
                if(isChecked) {
                        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        );

                    testsurview = new TestSurface(this);
                    wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
                    wm.addView(testsurview, params);

                    Toast.makeText(Controller.this, "start", Toast.LENGTH_SHORT).show();
                } else {
                    if (testsurview != null) {
                        wm.removeView(testsurview);
                        testsurview = null;
                        Toast.makeText(Controller.this, "end", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (testsurview != null) {
            wm.removeView(testsurview);
            testsurview = null;
        }
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();

        if (testsurview != null) {
            wm.removeView(testsurview);
        }
    } */
}
