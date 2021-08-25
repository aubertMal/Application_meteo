package com.malekaubert.android.meteo.models;

public class City {
    public String mName;
    public String mDescription;
    public String mTemperature;
    public int mWeatherIcon;

    public City(String mName, String mDescription, String mTemperature, int mWeatherIcon) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mTemperature = mTemperature;
        this.mWeatherIcon = mWeatherIcon;
    }
}
