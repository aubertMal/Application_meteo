package com.malekaubert.android.meteo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.malekaubert.android.meteo.activities.MainActivity;
import com.malekaubert.android.meteo.database.DataBaseHelper;
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

  public static long insertCityIntoDb(City city, Context context) {
    // Ouvrir la DB
    DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
    SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

    // Créer une nouvelle ligne à insérer
    ContentValues newCityValues = new ContentValues();

    // Valoriser chaque colonne
    newCityValues.put(Constants.KEY_NAME, city.mName);
    newCityValues.put(Constants.KEY_TEMP, city.mTemperature);
    newCityValues.put(Constants.KEY_DESC, city.mDescription);
    newCityValues.put(Constants.KEY_LAT, city.mLatitude);
    newCityValues.put(Constants.KEY_LNG, city.mLongitude);
    newCityValues.put(Constants.KEY_RES_ICON, city.mWeatherResIconWhite);

    // Insérer la ligne
    long rowId = sqLiteDatabase.insert(Constants.TABLE_CITY, null, newCityValues);

    dataBaseHelper.close();
    return rowId;
  }

  public static ArrayList<City> selectCitiesInDb(Context context) {

    ArrayList<City> citiesInDB = new ArrayList<>();
    // Ouvrir la DB
    DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
    SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

    Cursor cursor =
        sqLiteDatabase.query(
            Constants.TABLE_CITY,
            new String[] {
              Constants.KEY_NAME, Constants.KEY_TEMP, Constants.KEY_DESC, Constants.KEY_RES_ICON
            },
            null,
            null,
            null,
            null,
            null,
            "100");
    if (cursor.moveToFirst()){
      int count = 0;
      do{
        City cityInDb = new City(cursor.getString(cursor.getColumnIndex(Constants.KEY_NAME)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_DESC)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_TEMP)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_RES_ICON)));
        citiesInDB.add(cityInDb);
        count++;
      }while (cursor.moveToNext());
    }
    dataBaseHelper.close();
    return citiesInDB;
  }

  public static int removeCityFromDb(Context context, City city){
    // Ouvrir la DB
    DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
    SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

    int rowID = sqLiteDatabase.delete(Constants.TABLE_CITY,Constants.KEY_NAME + "='"+city.mName+"'",null);
    dataBaseHelper.close();

    return rowID;
  }
  public static void saveFavoriteCities(Context context, ArrayList<City> cities) {
    JSONArray jsonArrayCities = new JSONArray();
    for (int i = 0; i < cities.size(); i++) {
      jsonArrayCities.put(cities.get(i).mStringJson);
    }
    SharedPreferences preferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(Constants.PREFS_FAVORITE_CITIES, jsonArrayCities.toString());
    editor.apply();
  }

  public static ArrayList<City> initFavoriteCities(Context context) {
    ArrayList<City> cities = new ArrayList<>();
    SharedPreferences preferences =
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    try {
      JSONArray jsonArray =
          new JSONArray(preferences.getString(Constants.PREFS_FAVORITE_CITIES, ""));
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

  public static void callAPIFromCityName(String cityName, ApiCallBack activity) {
    String url = String.format(Constants.urlByCityName, cityName);
    callAPI(url, activity);
  }

  public static void callAPIFromLongitudeAndLatitude(
      double longitude, double latitude, ApiCallBack activity) {
    String url = String.format(Constants.urlByCityLongAndLatt, latitude, longitude);
    callAPI(url, activity);
  }

  private static void callAPI(String url, ApiCallBack activity) {
    OkHttpClient okHttpClient = new OkHttpClient();
    Handler handler = new Handler();

    Request request = new Request.Builder().url(url).build();
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
                  handler.post(
                      new Runnable() {
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
