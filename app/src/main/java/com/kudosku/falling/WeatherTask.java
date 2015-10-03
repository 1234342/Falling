package com.kudosku.falling;

import android.hardware.camera2.CaptureRequest;
import android.os.AsyncTask;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class WeatherTask extends AsyncTask<Double, Void, WeatherInit> {

    @Override

    public WeatherInit doInBackground(Double... params) {

        Weather client = new Weather();

        Double lat = params[0];
        Double lon = params[1];

        WeatherInit w = client.getWeather(lat, lon);

        return w;

    }



}
