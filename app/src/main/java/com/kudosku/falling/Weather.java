package com.kudosku.falling;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Weather {

    final static String openWeatherURL = "http://api.openweathermap.org/data/2.5/weather";
    public WeatherInit getWeather(Double lat,Double lon){
        WeatherInit w = new WeatherInit();
        String urlString = openWeatherURL + "?lat="+lat+"&lon="+lon;

        System.out.println(urlString);

        try {

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject json = new JSONObject(getStringFromInputStream(in));

            w = parseJSON(json);
            w.setLon(lon);
            w.setLat(lat);

        }catch(MalformedURLException e){
            e.printStackTrace();
            return null;

        }catch(JSONException e) {
            e.printStackTrace();
            return null;

        }catch(IOException e){
            e.printStackTrace();
            return null;

        }

        return w;

    }




    private WeatherInit parseJSON(JSONObject json) throws JSONException {

        WeatherInit w = new WeatherInit();
        w.setTemperature(json.getJSONObject("main").getDouble("temp"));
        w.setWeather(json.getJSONArray("weather").getJSONObject(0).getString("main"));
        w.setCity(json.getString("name"));
        w.setCloudy(json.getJSONObject("clouds").getInt("all"));
        if (json.has("snow")) {
            w.setSnow(json.getJSONArray("snow").getJSONObject(0).getInt("3h"));
        }
        if (json.has("rain")) {
            w.setRain(json.getJSONArray("rain").getJSONObject(0).getInt("3h"));
        }
        return w;

    }


    private static String getStringFromInputStream(InputStream is) {




        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
