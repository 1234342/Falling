package com.kudosku.falling;

import android.hardware.camera2.CaptureRequest;
import android.os.AsyncTask;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class WeatherTask extends AsyncTask<String, Void, WeatherInit> {

    @Override

    public WeatherInit doInBackground(String... params) {

        Weather client = new Weather();

        String lat = params[0];
        String lon = params[1];

        // API 호출
        WeatherInit w = client.getWeather(lat, lon);

        //System.out.println("Weather : "+w.getTemprature());
        // 작업 후 리

        return w;

    }



}
