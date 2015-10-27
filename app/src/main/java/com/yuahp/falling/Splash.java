package com.yuahp.falling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        };

        handler.sendEmptyMessageDelayed(0, 3000);
    }
}
