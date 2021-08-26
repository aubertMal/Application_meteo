package com.malekaubert.android.meteo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ApiCallBack {

  private TextView mTextViewCityName;
  private TextView mTextViewCityTemp;
  private TextView mTextViewWeatherDescription;
  private ImageView mImageViewWeatherIcon;
  private RelativeLayout mRelativeLayoutMeteo;
  private TextView mTextViewDeconnexion;
  private EditText mEditTextSaisie;
  private OkHttpClient mOkHttpClient;
  private Handler mHandler;
  private City mCurrentCity;

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
    mEditTextSaisie = (EditText) findViewById(R.id.edit_text_saisie);
    mOkHttpClient = new OkHttpClient();
    mHandler = new Handler();

    ConnectivityManager connMgr =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    if (networkInfo != null && networkInfo.isConnected()) {
      Log.d("TAG", "Oui je suis connectée");
      mTextViewDeconnexion.setVisibility(View.GONE);
      mRelativeLayoutMeteo.setVisibility(View.VISIBLE);
      Utils.callAPIFromLongitudeAndLatitude(0.684452,47.390445,this);
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
            intent.putExtra(Constants.EDIT_TEXT_KEY, mEditTextSaisie.getText().toString());
            startActivity(intent);
          }
        });
  }

  public void renderCurrentWeather(String stringJson) throws JSONException {
      mCurrentCity = new City(stringJson);
      mTextViewCityName.setText(mCurrentCity.mName);
      mTextViewCityTemp.setText(mCurrentCity.mTemperature+" °C");
      mTextViewWeatherDescription.setText(mCurrentCity.mDescription);
      Picasso.get().setLoggingEnabled(true);
      Log.d("TAG","https://openweathermap.org/img/wn/"+mCurrentCity.mWeatherResIconWhite+"@2x.png");
      Picasso.get().load("https://openweathermap.org/img/wn/"+mCurrentCity.mWeatherResIconWhite+"@2x.png").into(mImageViewWeatherIcon);
  }

    @Override
    public void callBack(String strJson) {
        try {
            renderCurrentWeather(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
