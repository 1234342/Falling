package com.kudosku.falling;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Credits extends AppCompatActivity {

    ArrayList<String> credits = new ArrayList<String>();
    ListView listview;
    int n = 0;
    android.os.Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        credits.add("Programming : YuahP");
        credits.add("Designing : KDPark");

        ArrayAdapter adapter= new ArrayAdapter(Credits.this, android.R.layout.simple_list_item_1, credits);

        listview = (ListView)findViewById(R.id.credits_list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(listener);

    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0) {

                n++;

                if (n >= 5) {
                    Intent con = new Intent(Credits.this, Controller.class);
                    startActivity(con);
                }

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        n = 0;
                        handler = null;
                    }
                };

                if(handler == null) {
                    handler = new android.os.Handler();
                    handler.postDelayed(runnable, 1000);
                }

            }

            Toast.makeText(Credits.this, credits.get(position), Toast.LENGTH_SHORT).show();
        }


    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);
    }
}
