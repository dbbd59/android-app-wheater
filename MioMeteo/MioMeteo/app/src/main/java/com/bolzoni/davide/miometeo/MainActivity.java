package com.bolzoni.davide.miometeo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ListView lv;
    Context context;

    private ListView listView;


    private static final int REQUEST_COARSE_LOCATION = 999;
    private String provider;
    private LocationManager myLocationManager;
    private String cittàGps;


    float x1, x2;
    float y1, y2;

    private TextView cittàTextView;
    private TextView temperaturaTextView;
    private TextView condizioneTextView;
    private TextView dataTextView;
    private ImageView iconaMeteo;

    private ServiceMeteo serviceMeteo;
    private ProgressDialog dialog;

    String città = "x";
    String flag = "x";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;


        Intent intent = getIntent();

        città = intent.getStringExtra("città");
        flag = intent.getStringExtra("flag");


        listView = (ListView) findViewById(R.id.listView);


        cittàTextView = (TextView) findViewById(R.id.cittàTV);
        temperaturaTextView = (TextView) findViewById(R.id.temperaturaTV);


        dataTextView = (TextView) findViewById(R.id.dataTV);


        condizioneTextView = (TextView) findViewById(R.id.condizioneTV);


        iconaMeteo = (ImageView) findViewById(R.id.meteoIconaIV);
        dialog = ProgressDialog.show(this, "Finding Location",
                "loading...", true);

        if (flag != null && flag.equals("noGps")) {

            serviceMeteo = new ServiceMeteo(this);
            serviceMeteo.getMeteo(città);
        }

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (flag != null && flag.equals("siGps")) {

            if (true) {//android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_COARSE_LOCATION);
                    }
                }
            }
            GPSTracker gps = new GPSTracker(this, myLocationManager);
            if (!isConnected) {
                Toast toast = Toast.makeText(getApplicationContext(), "Internet is not working", Toast.LENGTH_LONG);
                toast.show();
                Intent intent2 = new Intent(this, Insert_City.class);
                intent2.putExtra("flag", "a");
                startActivity(intent2);
                finish();
            } else {
                if (gps.isGPSEnabled) {
                    do {
                        gps.getLocation();
                        Log.d("GPS", String.valueOf(gps.getLatitude()));
                        Log.d("GPS", String.valueOf(gps.getLongitude()));

                        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(gps.getLatitude(), gps.getLongitude(), 2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!addresses.isEmpty()) {
                            cittàGps = addresses.get(0).getLocality();
                            Log.d("GPS", cittàGps);
                            serviceMeteo = new ServiceMeteo(this);
                            serviceMeteo.getMeteo(cittàGps);
                        }
                    } while (cittàGps == null);
                    gps.stopUsingGPS();
                } else {

                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    finish();
                    finish();
                }
            }
        }

        if (flag != null && flag.equals("s")) {
            serviceMeteo = new ServiceMeteo(this);
            serviceMeteo.getMeteo(intent.getStringExtra("città"));
        }
    }

    public void serviceFailure(Exception error) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Toast toast = Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_LONG);
        Log.d("ABCD", String.valueOf(error));
        toast.show();
        Intent intent = new Intent(this, Insert_City.class);
        intent.putExtra("flag", "a");
        startActivity(intent);
        finish();
    }

    public void serviceSuccess(InfoMeteo info) throws JSONException {
        Scrivirecord(info.getCittà());
        ArrayList prgmName;
        String[] forecastList = info.getForecast();
        int[] iconList = info.getCodIcone();

        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new CustomAdapter(this, forecastList, iconList));


        if (dialog != null) {
            dialog.dismiss();
        }

        cittàTextView.setText(info.getCittà());
        temperaturaTextView.setText(info.getTemperatura());
        dataTextView.setText(info.getData());
        condizioneTextView.setText(info.getCondizione());


        int icona = info.getCodiceIcona();
        int resourceId = getResources().getIdentifier("drawable/icon" + icona, null, getPackageName());
        Drawable drawable = getResources().getDrawable(resourceId);
        iconaMeteo.setImageDrawable(drawable);


    }

     public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                float ytot = y1 - y2;
                if (ytot < 200 && ytot > -200 && x2 > x1 && x2 - x1 > 400) {
                    Intent intent = new Intent(this, Insert_City.class);
                    intent.putExtra("flag", "a");
                    startActivity(intent);
                    finish();
                }
            }
        }
        return false;
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

    private void Scrivirecord(String citta) {
        try {
            DataBase db = new DataBase(this);
            db.open();

            String strQuery = "INSERT INTO tabella1 (nome) VALUES ";
            strQuery += "('" + citta + "');";

            Log.d("query", strQuery);

            db.Execute(strQuery, DataBase.TipoQuery.Comando);

            db.close();
        } catch (Exception e) {
        }
    }

}
