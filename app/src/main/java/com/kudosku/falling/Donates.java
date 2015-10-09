package com.kudosku.falling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Donates extends AppCompatActivity {

    ArrayList<String> credits = new ArrayList<String>();
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);

        credits.add("Programming : YuahP");
        credits.add("Designing : KDPark");

    ArrayAdapter adapter= new ArrayAdapter(Donates.this, android.R.layout.simple_list_item_1, credits);

        listview = (ListView)findViewById(R.id.credits_list);
        listview.setAdapter(adapter);

    }
}
