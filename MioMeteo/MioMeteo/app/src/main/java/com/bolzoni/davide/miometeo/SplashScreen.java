package com.bolzoni.davide.miometeo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SplashScreen extends AppCompatActivity {
    private ImageView imageView;
    private static int SPLASH_TIME_OUT = 3000;
    private LocationManager myLocationManager;
    private static final int REQUEST_COARSE_LOCATION = 999;

    private String cittàGps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        imageView = (ImageView) findViewById(R.id.imageView5);
        final GPSTracker gps = new GPSTracker(this, myLocationManager);
        final Intent i = new Intent(SplashScreen.this, Insert_City.class);
        final Intent i1 = new Intent(SplashScreen.this, Insert_City.class);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (true) {//android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_COARSE_LOCATION);
                }
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("INTERNET", String.valueOf(isConnected));
                if (!isConnected) {
                    startActivity(i);
                    finish();
                } else {
                    if (gps.isGPSEnabled) {
                        do {
                            gps.getLocation();
                            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = gcd.getFromLocation(gps.getLatitude(), gps.getLongitude(), 2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            cittàGps = addresses.get(0).getLocality();

                        } while (cittàGps == null);
                        Log.d("GPS", cittàGps);
                        i.putExtra("Gps", cittàGps);
                        i.putExtra("flag", "f");
                    }
                    if (cittàGps == null) {
                        gps.stopUsingGPS();
                        startActivity(i1);
                        finish();
                    } else {
                        gps.stopUsingGPS();
                        startActivity(i);
                        finish();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }

        }
    }
}