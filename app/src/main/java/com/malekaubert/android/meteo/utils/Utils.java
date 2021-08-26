package com.malekaubert.android.meteo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.malekaubert.android.meteo.activities.MainActivity;
import com.malekaubert.android.meteo.models.City;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {
    public static final String EDIT_TEXT_KEY = "message_saisi";
    public static final String OPEN_WEATHER_KEY = "252bb1313d94f4714db8f8f4624a4d1a";
    public static final String PREFS_NAME = "preferences";
    public static final String PREFS_FAVORITE_CITIES = "favoriteCities";
    public static String urlByCityName = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=Metric&appid="+OPEN_WEATHER_KEY;
    public static String urlByCityLongAndLatt = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=Metric&appid="+OPEN_WEATHER_KEY;


    public static void saveFavoriteCities(Context context, ArrayList<City> cities){
        JSONArray jsonArrayCities = new JSONArray();
        for (int i = 0; i < cities.size(); i++) {
          jsonArrayCities.put(cities.get(i).mStringJson);
        }
        SharedPreferences preferences = context.getSharedPreferences(Utils.PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Utils.PREFS_FAVORITE_CITIES,jsonArrayCities.toString());
        editor.apply();
    }

    public static ArrayList<City> initFavoriteCities(Context context){
        ArrayList<City> cities = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences(Utils.PREFS_NAME,Context.MODE_PRIVATE);
        try{
            JSONArray jsonArray = new JSONArray(preferences.getString(Utils.PREFS_FAVORITE_CITIES,""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCity = new JSONObject(jsonArray.getString(i));
                City city = new City(jsonObjectCity.toString());
                cities.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public static void callAPIFromCityName(String cityName,ApiCallBack activity){
        String url = String.format(urlByCityName,cityName);
        callAPI(url, activity);
    }

    public static void callAPIFromLongitudeAndLatitude(double longitude,double latitude, ApiCallBack activity){
        String url = String.format(urlByCityLongAndLatt,latitude,longitude);
        callAPI(url, activity);
    }
    private static void callAPI(String url, ApiCallBack activity){
        OkHttpClient okHttpClient = new OkHttpClient();
        Handler handler = new Handler();

        Request request =
                new Request.Builder()
                        .url(url)
                        .build();
        okHttpClient
                .newCall(request)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {}

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response)
                                    throws IOException {
                                if (response.isSuccessful()) {
                                    final String stringJson = response.body().string();
                                    Log.d("TAG", stringJson);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            activity.callBack(stringJson);
                                        }
                                    });
                                }
                            }
                        });
    }
}
