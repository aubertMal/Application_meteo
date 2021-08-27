package com.malekaubert.android.meteo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.malekaubert.android.meteo.R;
import com.malekaubert.android.meteo.models.City;
import com.malekaubert.android.meteo.utils.ApiCallBack;
import com.malekaubert.android.meteo.utils.Constants;
import com.malekaubert.android.meteo.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements ApiCallBack {

  private TextView mTextViewCityName;
  private TextView mTextViewCityTemp;
  private TextView mTextViewWeatherDescription;
  private ImageView mImageViewWeatherIcon;
  private RelativeLayout mRelativeLayoutMeteo;
  private TextView mTextViewDeconnexion;

  private City mCurrentCity;
  private LocationManager mLocationManager;
  private LocationListener mLocationListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    mTextViewCityName = (TextView) findViewById(R.id.text_view_city_name);
    mTextViewCityTemp = (TextView) findViewById(R.id.text_view_city_temp);
    mTextViewWeatherDescription = (TextView) findViewById(R.id.text_view_weather_description);
    mImageViewWeatherIcon = (ImageView) findViewById(R.id.image_view_weather_icon);
    mRelativeLayoutMeteo = (RelativeLayout) findViewById(R.id.relative_layout_meteo);
    mTextViewDeconnexion = (TextView) findViewById(R.id.text_view_deconnexion);

    ConnectivityManager connMgr =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    if (networkInfo != null && networkInfo.isConnected()) {
      Log.d("TAG", "Oui je suis connectée");
      mTextViewDeconnexion.setVisibility(View.GONE);
      mRelativeLayoutMeteo.setVisibility(View.VISIBLE);
      updateLocation();
    } else {
      Log.d("TAG", "NON, je ne suis pas connectée");
      mRelativeLayoutMeteo.setVisibility(View.GONE);
      mTextViewDeconnexion.setVisibility(View.VISIBLE);
    }

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), FavoriteActivity.class);
            startActivity(intent);
          }
        });
  }

  public void renderCurrentWeather(String stringJson) throws JSONException {
    mCurrentCity = new City(stringJson);
    mTextViewCityName.setText(mCurrentCity.mName);
    mTextViewCityTemp.setText(mCurrentCity.mTemperature + " °C");
    mTextViewWeatherDescription.setText(mCurrentCity.mDescription);
    Picasso.get().setLoggingEnabled(true);
    Log.d("TAG", "https://openweathermap.org/img/wn/" + mCurrentCity.mWeatherIcon + "@2x.png");
    Picasso.get()
        .load("https://openweathermap.org/img/wn/" + mCurrentCity.mWeatherIcon + "@2x.png")
        .into(mImageViewWeatherIcon);
  }

  @Override
  public void callBack(String strJson) {
    try {
      renderCurrentWeather(strJson);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case Constants.REQUEST_CODE:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          updateLocation();
        } else {
          Toast.makeText(this, "Permissions manquantes", Toast.LENGTH_SHORT);
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  public void updateLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(
          this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE);
    } else {
      mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      mLocationListener =
          new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
              Utils.callAPIFromLongitudeAndLatitude(
                  location.getLongitude(), location.getLatitude(), MainActivity.this);
              mLocationManager.removeUpdates(mLocationListener);
            }
          };
    }
    mLocationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
  }
}
