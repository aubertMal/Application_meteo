package com.malekaubert.android.meteo.models;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class City {
    public String mName;
    public String mDescription;
    public String mTemperature;
    public int mWeatherIcon;
    public int mIdCity;
    public double mLatitude;
    public double mLongitude;
    public String mWeatherResIconWhite;
    public String mStringJson;

    public City(String mName, String mDescription, String mTemperature, int mWeatherIcon) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mTemperature = mTemperature;
        this.mWeatherIcon = mWeatherIcon;
    }

    public City(String stringJson) throws JSONException {
        JSONObject json = new JSONObject(stringJson);
        mStringJson = stringJson;
        mName = json.getString("name");
        mDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");
        mTemperature = StringUtils.substringBefore(json.getJSONObject("main").getString("temp"),".");
        mWeatherResIconWhite = json.getJSONArray("weather").getJSONObject(0).getString("icon");
    }
}
