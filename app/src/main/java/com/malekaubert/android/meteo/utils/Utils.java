package com.malekaubert.android.meteo.utils;

import android.os.Handler;
import android.util.Log;

import com.malekaubert.android.meteo.activities.MainActivity;
import com.malekaubert.android.meteo.models.City;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {
    public static final String EDIT_TEXT_KEY = "message_saisi";
    public static final String OPEN_WEATHER_KEY = "252bb1313d94f4714db8f8f4624a4d1a";

    public static void findCity(String cityName, String requiredBy){
        OkHttpClient okHttpClient = new OkHttpClient();
        Handler handler = new Handler();

        Request request =
                new Request.Builder()
                        .url(
                                "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&units=Metric&appid="+OPEN_WEATHER_KEY)
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

                                        }
                                    });
                                }
                            }
                        });
    }
}
