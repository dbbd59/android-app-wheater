package com.bolzoni.davide.miometeo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Davide on 15/05/2016.
 */
public class InfoMeteo {
    private int[] codIcone = new int[8];
    private String[] forecast = new String[8];


    private String città;
    private String temperatura;
    private String condizione;
    private String data;
    private int codiceIcona;

    private JSONObject objItem;
    private JSONObject objCittà;
    private JSONObject objCondition;
    private JSONArray objForecast;


    public String getCittà() {
        città = objCittà.optString("city");
        città += ", " + objCittà.optString("country");
        return città;
    }


    public String getCondizione() {
        condizione = objCondition.optString("text");
        return condizione;
    }


    public String getData() throws JSONException {
        JSONObject temp = objForecast.getJSONObject(0);
        data = temp.optString("date");
        return data;
    }


    public int getCodiceIcona() {
        codiceIcona = objCondition.optInt("code");
        return codiceIcona;
    }


    public String getTemperatura() {
        temperatura = objCondition.optString("temp") + "°C";
        return temperatura;
    }

    public String[] getForecast() throws JSONException {
        for (int i = 0; i < forecast.length; i++) {
            JSONObject temp = objForecast.getJSONObject(i+1);
            forecast[i] = temp.optString("date") + ",  " + temp.optString("day") + "\n" +
                    "Min: " + temp.optString("low") + "°C / Max: " + temp.optString("high") + "°C" + "\n" +
                    temp.optString("text");
        }
        return forecast;
    }

    public int[] getCodIcone() throws JSONException {
        for (int i = 0; i < codIcone.length; i++) {
            JSONObject temp = objForecast.getJSONObject(i+1);
            codIcone[i] = temp.optInt("code");
        }
        return codIcone;
    }

    public void populate(JSONObject data) {
        try {
            objItem = data.optJSONObject("item");
            objCondition = objItem.optJSONObject("condition");
            objCittà = data.optJSONObject("location");
            objForecast = objItem.getJSONArray("forecast");
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}