package com.yuahp.falling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Adapter extends BaseAdapter {

    static ArrayList<Serializable> array = new ArrayList<Serializable>();
    static ArrayList<Integer> array2 = new ArrayList<Integer>();
    LayoutInflater inflater;

    public Adapter(Context context) {

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(convertView==null){

            view = inflater.inflate(R.layout.stat, parent, false);

        }else{

            view = convertView;

        }

        TextView text = (TextView)view.findViewById(R.id.stat_text);
        ImageView img = (ImageView)view.findViewById(R.id.stat_img);

        if(position == 0) {
            if(array.get(0).toString().equals("Haze")) {
                text.setText(R.string.haze);
            }
            if(array.get(0).toString().equals("Clear")) {
                text.setText(R.string.clear);
            }
            if(array.get(0).toString().equals("Rain")) {
                text.setText(R.string.rain);
            }
            if(array.get(0).toString().equals("Snow")) {
                text.setText(R.string.snow);
            }
            if(array.get(0).toString().equals("Thunderstorm")) {
                text.setText(R.string.Thunderstorm);
            }
            if(array.get(0).toString().equals("Mist")) {
                text.setText(R.string.mist);
            }
            img.setBackgroundResource(array2.get(0));
        } else if(position == 1) {
            img.setBackgroundResource(array2.get(1));
            text.setText(array.get(1).toString() + " %");
        } else if(position == 2) {
            img.setBackgroundResource(array2.get(2));
            text.setText(array.get(2).toString() + " °C");
        } else if(position == 3) {
            img.setBackgroundResource(array2.get(3));
            text.setText(array.get(3).toString());
        }

        return view;
    }
}
