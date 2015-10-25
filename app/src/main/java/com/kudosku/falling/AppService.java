package com.kudosku.falling;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class AppService extends Service {

    private Notification mNoti;
    SurfaceView surview;
    static Location lastKnownLocation = null;
    LocationManager locationManager;
    LocationListener locationListener;
    Boolean isGPSAlive;
    Boolean isNETAlive;
    Boolean isGPSuse;
    Boolean isEffectuse;
    String Timerpref_weather;
    String Timerpref_location;
    double lat;
    double lon;
    int timerdelay_weather = 0;
    int timerdelay_location = 0;
    static WeatherInit w;
    int weather = R.string.clear;
    Thread thread;
    Boolean running = false;
    Boolean retry = false;
    SharedPreferences sharedPref;
    FileOutputStream fileOutputStream;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        sharedPref = this.getSharedPreferences(getDefaultSharedPreferencesName(this), Context.MODE_PRIVATE);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        lastKnownLocation = locationManager
                .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        isNETAlive = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isGPSAlive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isGPSuse = sharedPref.getBoolean("Gps_use", true);
        isEffectuse = sharedPref.getBoolean("effect_use", true);
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

                            JSONObject obj = new JSONObject();
                            obj.put("temp", w.getTemperature());
                            obj.put("weather", w.getWeather());
                            obj.put("cloudy", w.getCloudy());
                            obj.put("snow", w.getSnow());
                            obj.put("rain", w.getRain());
                            obj.put("city", w.getCity());

                            String string = obj.toString();

                            fileOutputStream = openFileOutput("weather.json", Context.MODE_PRIVATE);
                            fileOutputStream.write(string.getBytes());
                            fileOutputStream.close();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
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

                            JSONObject obj = new JSONObject();
                            obj.put("temp", w.getTemperature());
                            obj.put("weather", w.getWeather());
                            obj.put("cloudy", w.getCloudy());
                            obj.put("snow", w.getSnow());
                            obj.put("rain", w.getRain());
                            obj.put("city", w.getCity());

                            String string = obj.toString();

                            fileOutputStream = openFileOutput("weather.json", Context.MODE_PRIVATE);
                            fileOutputStream.write(string.getBytes());
                            fileOutputStream.close();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (!isNETAlive && !isGPSAlive) {
                    Toast.makeText(getApplicationContext(), "모든 수신장치가 연결되어 있지 않습니다!", Toast.LENGTH_LONG).show();
                }
            }
        };

        if (Timerpref_location.equals("0")) {
            timerdelay_location = 60;
        }
        if (Timerpref_location.equals("1")) {
            timerdelay_location = 60 * 10; // sec X minute
        }
        if (Timerpref_location.equals("2")) {
            timerdelay_location = 60 * 15;
        }
        if (Timerpref_location.equals("3")) {
            timerdelay_location = 60 * 30;
        }
        if (Timerpref_location.equals("4")) {
            timerdelay_location = 60 * 60;
        }

        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, timerdelay_location * 1000, 250, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, timerdelay_location * 1000, 250, locationListener);

        if (isGPSAlive && isGPSuse) {
            if (lastKnownLocation != null) {
                lat = lastKnownLocation.getLatitude();
                lon = lastKnownLocation.getLongitude();

                WeatherTask t = new WeatherTask();

                try {

                    w = t.execute(lat, lon).get();

                    JSONObject obj = new JSONObject();
                    obj.put("temp", w.getTemperature());
                    obj.put("weather", w.getWeather());
                    obj.put("cloudy", w.getCloudy());
                    obj.put("snow", w.getSnow());
                    obj.put("rain", w.getRain());
                    obj.put("city", w.getCity());

                    String string = obj.toString();

                    fileOutputStream = openFileOutput("weather.json", Context.MODE_PRIVATE);
                    fileOutputStream.write(string.getBytes());
                    fileOutputStream.close();

                    PendingIntent mPI = PendingIntent.getActivity(
                            getApplicationContext(), 0, new Intent(getApplicationContext(),MainActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    if(obj.getString("weather").equals("Haze")) {
                        weather = R.string.haze;
                    }
                    if(obj.getString("weather").equals("Clear")) {
                        weather = R.string.clear;
                    }
                    if(obj.getString("weather").equals("Rain")) {
                        weather = R.string.rain;
                    }
                    if(obj.getString("weather").equals("Snow")) {
                        weather = R.string.rain;
                    }
                    if(obj.getString("weather").equals("Thunderstorm")) {
                        weather = R.string.Thunderstorm;
                    }
                    if(obj.getString("weather").equals("Mist")) {
                        weather = R.string.mist;
                    }

                    NotificationManager mNty = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNoti = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setContentText(getResources().getString(R.string.weather) + ": " + getResources().getString(weather) + "," +
                                    getResources().getString(R.string.clouds) + ": " + obj.getInt("cloudy") + "%," +
                                    getResources().getString(R.string.temp) + Math.round((((obj.getDouble("temp")) - 273.15) * 1000)) / 1000.0 + "°C")
                            .setSmallIcon(R.drawable.snow_1_16)
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

                    handler.sendEmptyMessage(0);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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

                    w = t.execute(lat, lon).get();

                    JSONObject obj = new JSONObject();
                    obj.put("temp", w.getTemperature());
                    obj.put("weather", w.getWeather());
                    obj.put("cloudy", w.getCloudy());
                    obj.put("snow", w.getSnow());
                    obj.put("rain", w.getRain());
                    obj.put("city", w.getCity());

                    String string = obj.toString();

                    fileOutputStream = openFileOutput("weather.json", Context.MODE_PRIVATE);
                    fileOutputStream.write(string.getBytes());
                    fileOutputStream.close();

                    PendingIntent mPI = PendingIntent.getActivity(
                            getApplicationContext(), 0, new Intent(getApplicationContext(),MainActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    if(obj.getString("weather").equals("Haze")) {
                        weather = R.string.haze;
                    }
                    if(obj.getString("weather").equals("Clear")) {
                        weather = R.string.clear;
                    }
                    if(obj.getString("weather").equals("Rain")) {
                        weather = R.string.rain;
                    }
                    if(obj.getString("weather").equals("Snow")) {
                        weather = R.string.rain;
                    }
                    if(obj.getString("weather").equals("Thunderstorm")) {
                        weather = R.string.Thunderstorm;
                    }
                    if(obj.getString("weather").equals("Mist")) {
                        weather = R.string.mist;
                    }

                    NotificationManager mNty = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNoti = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setContentText(getResources().getString(R.string.weather) + ": " + getResources().getString(weather) + "," +
                                    getResources().getString(R.string.clouds) + ": " + obj.getInt("cloudy") + "%," +
                                    getResources().getString(R.string.temp) + Math.round((((obj.getDouble("temp")) - 273.15) * 1000)) / 1000.0 + "°C")
                            .setSmallIcon(R.drawable.snow_1_16)
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

                    handler.sendEmptyMessage(0);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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

        return Service.START_STICKY;
    }

    public void onDestroy(){
        super.onDestroy();

        handler.removeMessages(0);

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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {

                Timerpref_weather = sharedPref.getString("weather_delay", "2");
                if (Timerpref_weather.equals("0")) {
                    timerdelay_weather = 60;
                }
                if (Timerpref_weather.equals("1")) {
                    timerdelay_weather = 60 * 10;
                }
                if (Timerpref_weather.equals("2")) {
                    timerdelay_weather = 60 * 15; // sec X minute
                }
                if (Timerpref_weather.equals("3")) {
                    timerdelay_weather = 60 * 30;
                }
                if (Timerpref_weather.equals("4")) {
                    timerdelay_weather = 60 * 60;
                }

                WeatherTask t = new WeatherTask();

                try {

                    w = t.execute(lat, lon).get();

                    JSONObject obj = new JSONObject();
                    obj.put("temp", w.getTemperature());
                    obj.put("weather", w.getWeather());
                    obj.put("cloudy", w.getCloudy());
                    obj.put("snow", w.getSnow());
                    obj.put("rain", w.getRain());
                    obj.put("city", w.getCity());

                    String string = obj.toString();

                    fileOutputStream = openFileOutput("weather.json", Context.MODE_PRIVATE);
                    fileOutputStream.write(string.getBytes());
                    fileOutputStream.close();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessageDelayed(0, 1000 * timerdelay_weather);
            }
        }
    };
}
