package com.kudosku.falling;

import android.hardware.camera2.CaptureRequest;
import android.os.AsyncTask;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class WeatherTask extends AsyncTask<Integer, Void, WeatherInit> {

    @Override

    public WeatherInit doInBackground(Integer... params) {

        Weather client = new Weather();

        int lat = params[0];
        int lon = params[1];

        WeatherInit w = client.getWeather(lat, lon);

        return w;

    }



}
