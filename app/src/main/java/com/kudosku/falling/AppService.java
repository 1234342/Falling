package com.kudosku.falling;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AppService extends Service {

    private Notification mNoti;
    SurfaceView surview;
    Location lastKnownLocation = null;
    List<Address> addressList = null;
    Geocoder geocoder;
    LocationManager locationManager;
    LocationListener locationListener;
    int id;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(getApplicationContext(), provider + " " + status, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), "위치 정보 사용 가능. 공급자: " + provider, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(), "위치 정보 사용 불가. 공급자: " + provider, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext());

                try {

                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                } catch (IOException e) {

                    Toast. makeText(getApplicationContext(), "위치 정보 갱신  불가", Toast. LENGTH_SHORT).show();

                    e.printStackTrace();

                }
            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, locationListener);

        lastKnownLocation = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        geocoder = new Geocoder(getApplicationContext(), Locale.KOREA);

        double lng = lastKnownLocation.getLatitude();
        double lat = lastKnownLocation.getLatitude();
        Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);


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

        return Service.START_STICKY;
    }

    String getAddressFromLocation(Location location, Locale locale){

        try {
            addressList = geocoder.getFromLocation(
                    lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude(),
                    1
            );
        } catch (IOException e) {
            Toast.makeText(this, "위치로부터 주소를 인식할 수 없습니다. 네트워크가 연결되어 있는지 확인해 주세요.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (1 > addressList.size()) {

            return "해당 위치에 주소 없음";

        }

        Address address = addressList.get(0);

        StringBuilder addressStringBuilder = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressStringBuilder.append(address.getAddressLine(i));
            if (i < address.getMaxAddressLineIndex())
                addressStringBuilder.append("\n");
        }

        return addressStringBuilder.toString();
    }

    public void onDestroy(){

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        WindowManager wm = ((WindowManager) getSystemService(getApplicationContext().WINDOW_SERVICE));
        wm.removeView(surview);

        stopForeground(true);
        Toast.makeText(getBaseContext(), getResources().getString(R.string.service_stop), Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
