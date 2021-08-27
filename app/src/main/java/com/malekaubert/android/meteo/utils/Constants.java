package com.malekaubert.android.meteo.utils;

public class Constants {
    public static final String DATABASE_NAME = "meteo_db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CITY = "City";
    public static final String KEY_ID = "id";
    public static final String KEY_ID_CITY = "id_city";
    public static final String KEY_NAME = "name";
    public static final String KEY_TEMP = "temperature";
    public static final String KEY_DESC = "description";
    public static final String KEY_RES_ICON = "code_icone";
    public static final String KEY_LAT = "lattitude";
    public static final String KEY_LNG = "longitude";


    public static final String EDIT_TEXT_KEY = "message_saisi";
    public static final String OPEN_WEATHER_KEY = "252bb1313d94f4714db8f8f4624a4d1a";
    public static final String PREFS_NAME = "preferences";
    public static final String PREFS_FAVORITE_CITIES = "favoriteCities";
    public static String urlByCityName = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=Metric&appid="+OPEN_WEATHER_KEY;
    public static String urlByCityLongAndLatt = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=Metric&appid="+OPEN_WEATHER_KEY;

    public static final int REQUEST_CODE = 1;

}
