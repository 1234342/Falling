package com.kudosku.falling;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AppService extends Service {

    private Notification mNoti;
    SurfaceView surview;
    static Location lastKnownLocation = null;
    static int a;
    LocationManager locationManager;
    LocationListener locationListener;
    Boolean isGPSAlive;
    boolean isNETAlive;
    Boolean isGPSuse;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        SharedPreferences sharedPref = this.getSharedPreferences(getDefaultSharedPreferencesName(this), this.MODE_PRIVATE);

        lastKnownLocation = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        isNETAlive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isGPSAlive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isGPSuse = sharedPref.getBoolean("Gps_use", true);

        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!isServiceRunning(AppService.class)) {
                    Intent svi = new Intent(AppService.this, AppService.class);
                    startService(svi);
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                if (isGPSAlive && isGPSuse) {
                    StringBuffer lat = new StringBuffer();
                    lat.append(lastKnownLocation.getLatitude());
                    StringBuffer lon = new StringBuffer();
                    lon.append(lastKnownLocation.getLongitude());
                    String lat_ = lat.toString();
                    String lon_ = lon.toString();
                    Toast.makeText(getApplicationContext(), "GPS로 연결되었습니다.", Toast.LENGTH_LONG).show();

                    WeatherTask t = new WeatherTask();

                    try {

                        WeatherInit w = t.execute(lat_,lon_).get();

                        String weather = w.getWeather();

                        push(w, weather);

                        a = (int) Math.round(((w.getTemprature() - 273.15) * 1000));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                } else if (isNETAlive) {
                    StringBuffer lat = new StringBuffer();
                    lat.append(lastKnownLocation.getLatitude());
                    StringBuffer lon = new StringBuffer();
                    lon.append(lastKnownLocation.getLongitude());
                    String lat_ = lat.toString();
                    String lon_ = lon.toString();
                    Toast.makeText(getApplicationContext(), "네트워크로 연결되었습니다.", Toast.LENGTH_LONG).show();

                    WeatherTask t = new WeatherTask();

                    try {

                        WeatherInit w = t.execute(lat_,lon_).get();

                        String weather = w.getWeather();

                        push(w, weather);

                        a = (int) Math.round(((w.getTemprature() - 273.15) * 1000));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else if (!isNETAlive && !isGPSAlive) {
                    Toast.makeText(getApplicationContext(), "모든 수신장치가 연결되어 있지 않습니다!", Toast.LENGTH_LONG).show();
                }
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, locationListener);

        if (isGPSAlive && isGPSuse) {
            StringBuffer lat = new StringBuffer();
            lat.append(lastKnownLocation.getLatitude());
            StringBuffer lon = new StringBuffer();
            lon.append(lastKnownLocation.getLongitude());
            String lat_ = lat.toString();
            String lon_ = lon.toString();
            Toast.makeText(getBaseContext(), "GPS로 연결되었습니다.", Toast.LENGTH_LONG).show();

            WeatherTask t = new WeatherTask();

            try {

                WeatherInit w = t.execute(lat_,lon_).get();

                PendingIntent mPI = PendingIntent.getActivity(
                        getApplicationContext(), 0, new Intent(getApplicationContext(),MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager mNty = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNoti = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(getResources().getString(R.string.app_touch))
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

                push(w, weather);

                a = (int) Math.round(((w.getTemprature() - 273.15) * 1000));

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } else if (isNETAlive) {
            StringBuffer lat = new StringBuffer();
            lat.append(lastKnownLocation.getLatitude());
            StringBuffer lon = new StringBuffer();
            lon.append(lastKnownLocation.getLongitude());
            String lat_ = lat.toString();
            String lon_ = lon.toString();
            Toast.makeText(getBaseContext(), "네트워크로 연결되었습니다.", Toast.LENGTH_LONG).show();

            WeatherTask t = new WeatherTask();

            try {

                WeatherInit w = t.execute(lat_,lon_).get();

                PendingIntent mPI = PendingIntent.getActivity(
                        getApplicationContext(), 0, new Intent(getApplicationContext(),MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager mNty = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNoti = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(getResources().getString(R.string.app_touch))
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
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
                );

                surview = new Surface(this);

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                wm.addView(surview, params2);

                String weather = w.getWeather();

                push(w, weather);

                a = (int) Math.round(((w.getTemprature() - 273.15) * 1000));

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else if (!isNETAlive && !isGPSAlive) {
            Toast.makeText(getApplicationContext(), "모든 수신장치가 연결되어 있지 않습니다!", Toast.LENGTH_LONG).show();
            stopSelf();
        }

        return Service.START_STICKY;
    }

    public void onDestroy(){

        locationManager.removeUpdates(locationListener);

        WindowManager wm = ((WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE));
        wm.removeView(surview);

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

    public void push (WeatherInit w, String weather) {
        if (Objects.equals(weather, "Haze")) {
            System.out.println("상태: " + getString(R.string.haze) + "\n기온: " + Math.round(((w.getTemprature() - 273.15) * 1000))/1000.0 + "°C" +
                    "\n 지역: " + w.getCity() + "\n 구름: " + w.getCloudy() + "%");

            Toast.makeText(getApplicationContext(), "상태: " + getString(R.string.haze) + "\n기온: " + Math.round(((w.getTemprature() - 273.15) * 1000))/1000.0 + "°C" +
                    "\n 지역: " + w.getCity() + "\n 구름: " + w.getCloudy() + "%", Toast.LENGTH_LONG).show();
        }
        if (Objects.equals(weather, "Mist")) {
            System.out.println("상태: " + getString(R.string.mist) + "\n기온: " + Math.round(((w.getTemprature() - 273.15) * 1000))/1000.0 + "°C" +
                    "\n 지역: " + w.getCity() + "\n 구름: " + w.getCloudy() + "%");

            Toast.makeText(getApplicationContext(), "상태: " + getString(R.string.mist) + "\n기온: " + Math.round(((w.getTemprature() - 273.15) * 1000))/1000.0 + "°C" +
                    "\n 지역: " + w.getCity() + "\n 구름: " + w.getCloudy() + "%", Toast.LENGTH_LONG).show();
        }
        if (Objects.equals(weather, "Cloud")) {
            System.out.println("상태: " + getString(R.string.cloud) + "\n기온: " + Math.round(((w.getTemprature() - 273.15) * 1000))/1000.0 + "°C" +
                    "\n 지역: " + w.getCity() + "\n 구름: " + w.getCloudy() + "%");

            Toast.makeText(getApplicationContext(), "상태: " + getString(R.string.haze) + "\n기온: " + Math.round(((w.getTemprature() - 273.15) * 1000))/1000.0 + "°C" +
                    "\n 지역: " + w.getCity() + "\n 구름: " + w.getCloudy() + "%", Toast.LENGTH_LONG).show();
        }
        if (Objects.equals(weather, "Clear")) {
            System.out.println("상태: " + getString(R.string.clear) + "\n기온: " + Math.round(((w.getTemprature() - 273.15) * 1000))/1000.0 + "°C" +
                    "\n 지역: " + w.getCity() + "\n 구름: " + w.getCloudy() + "%");

            Toast.makeText(getApplicationContext(), "상태: " + getString(R.string.clear) + "\n기온: " + Math.round(((w.getTemprature() - 273.15) * 1000))/1000.0 + "°C" +
                    "\n 지역: " + w.getCity() + "\n 구름: " + w.getCloudy() + "%", Toast.LENGTH_LONG).show();
            return;
        }
    }
}
