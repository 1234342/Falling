package com.kudosku.falling;

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
    Button b1,b2,b3,b4,b5,b6,b7,b8;
    SurfaceView testsurview;
    static int snow = 0;
    static int rain = 0;
    static int cloudy = 0;
    static int month = 0;
    static int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller);

        s1 = (Switch) findViewById(R.id.onoffswitch);
        b1 = (Button) findViewById(R.id.cherrybutton);
        b2 = (Button) findViewById(R.id.cloudbutton);
        b3 = (Button) findViewById(R.id.heatshimmerbutton);
        b4 = (Button) findViewById(R.id.frostbutton);
        b5 = (Button)findViewById(R.id.leavebutton);
        b6 = (Button)findViewById(R.id.snowbutton);
        b7 = (Button)findViewById(R.id.rainbutton);
        b8 = (Button)findViewById(R.id.reset);

        s1.setOnCheckedChangeListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cherrybutton:
                month = 1;
                cloudy = 0;
                temp = 30;
                snow = 0;
                rain = 0;
                Toast.makeText(Controller.this, "spring", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cloudbutton:
                month = 1;
                cloudy = 50;
                temp = 30;
                snow = 0;
                rain = 0;
                Toast.makeText(Controller.this, "cloud", Toast.LENGTH_SHORT).show();
                break;
            case R.id.heatshimmerbutton:
                month = 8;
                cloudy = 0;
                temp = 30;
                snow = 0;
                rain = 0;
                Toast.makeText(Controller.this, "summer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.frostbutton:
                month = 11;
                cloudy = 0;
                temp = 30;
                snow = 0;
                rain = 0;
                Toast.makeText(Controller.this, "winter", Toast.LENGTH_SHORT).show();
                break;
            case R.id.leavebutton:
                month = 10;
                cloudy = 0;
                temp = 30;
                snow = 0;
                rain = 0;
                Toast.makeText(Controller.this, "leave", Toast.LENGTH_SHORT).show();
                break;
            case R.id.snowbutton:
                month = 11;
                cloudy = 0;
                temp = 10;
                snow = 30;
                rain = 0;
                Toast.makeText(Controller.this, "snow", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rainbutton:
                month = 11;
                cloudy = 0;
                temp = 20;
                rain = 30;
                Toast.makeText(Controller.this, "rain", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reset:
                snow = 0;
                rain = 0;
                cloudy = 0;
                month = 0;
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

                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    wm.addView(testsurview, params);
                    Toast.makeText(Controller.this, "start", Toast.LENGTH_SHORT).show();
                } else {
                    if (testsurview != null) {
                        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                        wm.removeView(testsurview);
                        Toast.makeText(Controller.this, "end", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (testsurview != null) {
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(testsurview);
        }
    }
}
