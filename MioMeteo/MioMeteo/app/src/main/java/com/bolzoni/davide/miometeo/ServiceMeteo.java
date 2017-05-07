package com.bolzoni.davide.miometeo;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Davide on 15/05/2016.
 */
public class ServiceMeteo {
    private Exception error;
    private String unit = "C";
    private MainActivity activity;
    private Insert_City insert_city;

    public ServiceMeteo(Activity activity) {
        this.activity = (MainActivity) activity;
    }

    public void getMeteo(String location) {

        new AsyncTask<String, Void, InfoMeteo>() {
            @Override
            protected InfoMeteo doInBackground(String[] locations) {

                String location = locations[0];

                InfoMeteo channel = new InfoMeteo();

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='" + unit + "'", location);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject data = new JSONObject(result.toString());

                    JSONObject queryResults = data.optJSONObject("query");

                    int count = queryResults.optInt("count");

                    if (count == 0) {
                        error = new Exception("No weather information found for " + location);
                        return null;
                    }

                    JSONObject channelJSON = queryResults.optJSONObject("results").optJSONObject("channel");
                    channel.populate(channelJSON);

                    return channel;

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }
            @Override
            protected void onPostExecute(InfoMeteo info) {

                if (info == null && error !=null) {
                    activity.serviceFailure(error);
                } else {
                    try {
                        activity.serviceSuccess(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }.execute(location);
    }

}