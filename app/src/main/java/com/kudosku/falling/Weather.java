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
    public WeatherInit getWeather(String lat,String lon){
        WeatherInit w = new WeatherInit();
        String urlString = openWeatherURL + "?lat="+lat+"&lon="+lon;

        System.out.println(urlString);

        try {

            // call API by using HTTPURLConnection
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject json = new JSONObject(getStringFromInputStream(in));

            // parse JSON
            w = parseJSON(json);
            w.setIon(lon);
            w.setLat(lat);

        }catch(MalformedURLException e){
            System.err.println("Malformed URL");
            e.printStackTrace();
            return null;

        }catch(JSONException e) {
            System.err.println("JSON parsing error");
            e.printStackTrace();
            return null;

        }catch(IOException e){
            System.err.println("URL Connection failed");
            e.printStackTrace();
            return null;

        }

        // set Weather Object
        return w;

    }




    private WeatherInit parseJSON(JSONObject json) throws JSONException {

        WeatherInit w = new WeatherInit();
        w.setTemperature((float) json.getJSONObject("main").getDouble("temp"));
        w.setWeather(json.getJSONArray("weather").getJSONObject(0).getString("main"));
        w.setCity(json.getString("name"));
        w.setCloudy(json.getJSONObject("clouds").getString("all"));
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
