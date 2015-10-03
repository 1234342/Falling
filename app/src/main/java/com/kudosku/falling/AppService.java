package com.kudosku.falling;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class AppService extends Service {

    private Notification mNoti;
    SurfaceView surview;
    static Location lastKnownLocation = null;
    static int temp;
    LocationManager locationManager;
    LocationListener locationListener;
    Boolean isGPSAlive;
    Boolean isNETAlive;
    Boolean isGPSuse;
    Boolean isnotity_use;
    String Timerpref_weather;
    String Timerpref_location;
    double lat;
    double lon;
    Timer timer = new Timer();
    SharedPreferences sharedPref;
    int timerdelay_weather = 0;
    int timerdelay_location = 0;
    TimerTask timertask;
    static WeatherInit w;
    int weather = R.string.clear;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences sharedPref = this.getSharedPreferences(getDefaultSharedPreferencesName(this), this.MODE_PRIVATE);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        lastKnownLocation = locationManager
                .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        isNETAlive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isGPSAlive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isGPSuse = sharedPref.getBoolean("Gps_use", true);
        Timerpref_location = sharedPref.getString("location", "2");
        Timerpref_weather = sharedPref.getString("weather", "2");

        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                if (isGPSAlive && isGPSuse) {
                    if (lastKnownLocation != null) {
                        lat = lastKnownLocation.getLatitude();
                        lon = lastKnownLocation.getLongitude();

                        WeatherTask t = new WeatherTask();

                        try {

                            w = t.execute(lat, lon).get();

                            String weather = w.getWeather();

                            temp = (int) Math.round(w.getTemperature() - 273.15);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (isNETAlive) {
                    if (lastKnownLocation != null) {
                        lat = lastKnownLocation.getLatitude();
                        lon = lastKnownLocation.getLongitude();

                        WeatherTask t = new WeatherTask();

                        try {

                            w = t.execute(lat, lon).get();

                            String weather = w.getWeather();

                            temp = (int) Math.round(w.getTemperature() - 273.15);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (!isNETAlive && !isGPSAlive) {
                    Toast.makeText(getApplicationContext(), "모든 수신장치가 연결되어 있지 않습니다!", Toast.LENGTH_LONG).show();
                }
            }
        };

        if (Objects.equals(Timerpref_location, "0")) {
            timerdelay_location = 60;
        }
        if (Objects.equals(Timerpref_location, "1")) {
            timerdelay_location = 60 * 10; // sec X minute
        }
        if (Objects.equals(Timerpref_location, "2")) {
            timerdelay_location = 60 * 15;
        }
        if (Objects.equals(Timerpref_location, "3")) {
            timerdelay_location = 60 * 30;
        }
        if (Objects.equals(Timerpref_location, "4")) {
            timerdelay_location = 60 * 60;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timerdelay_location * 1000, 250, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timerdelay_location * 1000, 250, locationListener);

        if (isGPSAlive && isGPSuse) {
            if (lastKnownLocation != null) {
                lat = lastKnownLocation.getLatitude();
                lon = lastKnownLocation.getLongitude();

                WeatherTask t = new WeatherTask();

                try {

                    w = t.execute(lat,lon).get();

                    PendingIntent mPI = PendingIntent.getActivity(
                            getApplicationContext(), 0, new Intent(getApplicationContext(),MainActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationManager mNty = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Objects.equals(w.getWeather(), "Haze")) {
                        weather = R.string.haze;
                    }
                    if (Objects.equals(w.getWeather(), "Mist")) {
                        weather = R.string.mist;
                    }
                    if (Objects.equals(w.getWeather(), "Clouds")) {
                        weather = R.string.cloud;
                    }
                    if (Objects.equals(w.getWeather(), "Clear")) {
                        weather = R.string.clear;
                    }
                    mNoti = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setContentText(getResources().getString(R.string.city)+ ": " + w.getCity() + "," +
                                    getResources().getString(R.string.weather) + ": " + getResources().getString(weather) + ","+
                                    getResources().getString(R.string.clouds) + ": " + w. getCloudy() + "%," +
                                    getResources().getString(R.string.temp)+ Math.round(((w.getTemperature() - 273.15) * 1000)) / 1000.0 + "°C" )
                            .setSmallIcon(R.drawable.splash)
                            .setTicker(getResources().getString(R.string.app_alive))
                            .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setAutoCancel(false)
                            .setContentIntent(mPI)
                            .build();

                    //mNty.notify(3939, mNoti);
                    startForeground(3939, mNoti);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_start), Toast.LENGTH_LONG).show();

                    WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    );

                    surview = new Surface(this);

                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    wm.addView(surview, params2);

                    String weather = w.getWeather();

                    temp = (int) Math.round(w.getTemperature() - 273.15);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "위치 정보를 받을 수 없습니다. 몇 초후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                stopSelf();
            }

        } else if (isNETAlive) {
            if (lastKnownLocation != null) {
                lat = lastKnownLocation.getLatitude();
                lon = lastKnownLocation.getLongitude();

                WeatherTask t = new WeatherTask();

                try {

                    WeatherInit w = t.execute(lat, lon).get();

                    PendingIntent mPI = PendingIntent.getActivity(
                            getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationManager mNty = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Objects.equals(w.getWeather(), "Haze")) {
                        weather = R.string.haze;
                    }
                    if (Objects.equals(w.getWeather(), "Mist")) {
                        weather = R.string.mist;
                    }
                    if (Objects.equals(w.getWeather(), "Clouds")) {
                        weather = R.string.cloud;
                    }
                    if (Objects.equals(w.getWeather(), "Clear")) {
                        weather = R.string.clear;
                    }
                    mNoti = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setContentText(getResources().getString(R.string.city) + ": " + w.getCity() + "," +
                                    getResources().getString(R.string.weather) + ": " + getResources().getString(weather) + "," +
                                    getResources().getString(R.string.clouds) + ": " + w.getCloudy() + "%," +
                                    getResources().getString(R.string.temp) + Math.round(((w.getTemperature() - 273.15) * 1000)) / 1000.0 + "°C")
                            .setSmallIcon(R.drawable.splash)
                            .setTicker(getResources().getString(R.string.app_alive))
                            .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setAutoCancel(false)
                            .setContentIntent(mPI)
                            .build();


                    //mNty.notify(3939, mNoti);
                    startForeground(3939, mNoti);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_start), Toast.LENGTH_LONG).show();

                    WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    );

                    surview = new Surface(this);

                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    wm.addView(surview, params2);

                    String weather = w.getWeather();

                    temp = (int) Math.round(w.getTemperature() - 273.15);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "위치 정보를 받을 수 없습니다. 몇 초후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                stopSelf();
            }
        } else if (!isNETAlive && !isGPSAlive) {
            Toast.makeText(getApplicationContext(), "모든 수신장치가 연결되어 있지 않습니다!", Toast.LENGTH_LONG).show();
            stopSelf();
        }
        if (Objects.equals(Timerpref_weather, "0")) {
            timerdelay_weather = 60 * 10;
        }
        if (Objects.equals(Timerpref_weather, "1")) {
            timerdelay_weather = 60 * 15; // sec X minute
        }
        if (Objects.equals(Timerpref_weather, "2")) {
            timerdelay_weather = 60 * 30;
        }
        if (Objects.equals(Timerpref_weather, "3")) {
            timerdelay_weather = 60 * 60;
        }

        timertask = new TimerTask() {

            @Override
            public void run() {
                lat = lastKnownLocation.getLatitude();
                lon = lastKnownLocation.getLongitude();

                WeatherTask t = new WeatherTask();

                try {

                    w = t.execute(lat, lon).get();

                    temp = (int) Math.round(w.getTemperature() - 273.15);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(timertask, 1000, timerdelay_weather * 1000);

        return Service.START_STICKY;
    }

    public void onDestroy(){

        timer.cancel();
        timertask = null;

        locationManager.removeUpdates(locationListener);

        if(surview != null) {
            WindowManager wm = ((WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE));
            wm.removeView(surview);
        }

        stopForeground(true);
        Toast.makeText(getBaseContext(), getResources().getString(R.string.service_stop), Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    public boolean isServiceRunning (Class<?> serviceclass) {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : am.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceclass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    public void notify(WeatherInit w, String weather) {
    }
}
