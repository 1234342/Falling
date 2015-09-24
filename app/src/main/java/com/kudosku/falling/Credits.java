package com.kudosku.falling;

import java.util.ArrayList;

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
            Toast.makeText(Credits.this, credits.get(position), Toast.LENGTH_SHORT).show();
        }


    };
}
