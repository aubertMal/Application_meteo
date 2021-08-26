package com.malekaubert.android.meteo.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.malekaubert.android.meteo.R;
import com.malekaubert.android.meteo.models.City;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
  private Context mContext;
  private ArrayList<City> mCities;

  public FavoriteAdapter(Context mContext, ArrayList<City> mCities) {
    this.mContext = mContext;
    this.mCities = mCities;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_city, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
    City city = mCities.get(position);
    holder.mTextViewCityName.setText(city.mName);
    holder.mTextViewWeatherDescription.setText(city.mDescription);
    holder.mTextViewTemperature.setText(city.mTemperature);
    Log.d("TAG", "https://openweathermap.org/img/wn/"+city.mWeatherIcon+"@2x.png");
    Picasso.get().load("https://openweathermap.org/img/wn/"+city.mWeatherIcon+"@2x.png").into(holder.mImageViewWeather);
    holder.position = position;
  }

  @Override
  public int getItemCount() {
    return mCities.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewCityName;
    public TextView mTextViewWeatherDescription;
    public TextView mTextViewTemperature;
    public ImageView mImageViewWeather;
    public int position;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      mTextViewCityName = (TextView) itemView.findViewById(R.id.text_view_city_name);
      mTextViewWeatherDescription =
          (TextView) itemView.findViewById(R.id.text_view_weather_description);
      mTextViewTemperature = (TextView) itemView.findViewById(R.id.text_view_temperature);
      mImageViewWeather = (ImageView) itemView.findViewById(R.id.image_view_weather);
    }
  }
}
