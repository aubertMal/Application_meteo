package com.malekaubert.android.meteo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.malekaubert.android.meteo.R;
import com.malekaubert.android.meteo.adapters.FavoriteAdapter;
import com.malekaubert.android.meteo.models.City;
import com.malekaubert.android.meteo.utils.ApiCallBack;
import com.malekaubert.android.meteo.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements ApiCallBack {

  private ArrayList<City> mCities = new ArrayList<>();
  private RecyclerView mRecyclerView;
  private FavoriteAdapter mAdapter;
  private Context mContext;
  ItemTouchHelper itemTouchHelper =
      new ItemTouchHelper(
          new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(
                @NonNull RecyclerView recyclerView,
                @NotNull RecyclerView.ViewHolder viewHolder,
                @NotNull RecyclerView.ViewHolder target) {
              return false;
            }

            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
              FavoriteAdapter.ViewHolder viewHolderSwipped =
                  (FavoriteAdapter.ViewHolder) viewHolder;
              City cityToRemove = mCities.get(viewHolderSwipped.position);
              mCities.remove(viewHolderSwipped.position);
              Utils.removeCityFromDb(FavoriteActivity.this,cityToRemove);
              mAdapter.notifyDataSetChanged();

              if (!cityInCities(mCities,cityToRemove.mName)) {

                Snackbar.make(
                        findViewById(R.id.my_coordinator_layout),
                        cityToRemove.mName + " a été supprimée",
                        Snackbar.LENGTH_LONG)
                    .setAction(
                        "Annuler",
                        new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                            mCities.add(viewHolderSwipped.position, cityToRemove);
                            Utils.insertCityIntoDb(cityToRemove,FavoriteActivity.this);
                            Log.d("TAG", "Suppression annulée");
                            mAdapter.notifyDataSetChanged();
                          }
                        })
                    .show();
              }
              //utilisation des SharedPreferences
                // Utils.saveFavoriteCities(FavoriteActivity.this, mCities);
            }
          });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mContext = this;
    setContentView(R.layout.activity_favorite);
    initCities();

    mRecyclerView = findViewById(R.id.my_recycler_view);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
    mRecyclerView.setLayoutManager(layoutManager);
    mAdapter = new FavoriteAdapter(this, mCities);
    mRecyclerView.setAdapter(mAdapter);
    itemTouchHelper.attachToRecyclerView(mRecyclerView);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
    toolBarLayout.setTitle(getTitle());

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite, null);
            final EditText editTextCityName =
                (EditText) v.findViewById(R.id.edit_text_add_favorite);

            builder.setView(v);
            builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    if (editTextCityName.getText().length() != 0) {
                      Utils.callAPIFromCityName(
                          editTextCityName.getText().toString(), FavoriteActivity.this);
                    }
                  }
                });
            builder.setNegativeButton("Annuler", null);
            builder.create().show();
          }
        });
  }

  void initCities(){
      //utilisation des SharedPreferences
      //mCities = Utils.initFavoriteCities(this);

      //utilisation de la BDD
    mCities = Utils.selectCitiesInDb(this);
  }
  @Override
  public void callBack(String strJson) {
    try {
      City newCity = new City(strJson);
      //On vérifie si la ville n'est pas déjà dans les favoris
      if (!cityInCities(mCities, newCity.mName)) {
        mCities.add(newCity);
        // Utils.saveFavoriteCities(this, mCities);
        if (Utils.insertCityIntoDb(newCity, this) == -1) {
          Toast.makeText(this, "Error when adding a city", Toast.LENGTH_SHORT).show();
        }
        mAdapter.notifyDataSetChanged();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  boolean cityInCities(ArrayList<City> cities, String cityName){
      boolean cityExists = false;
      for (City city : mCities) {
          if (StringUtils.equals(city.mName, cityName)) {
              cityExists = true;
          }
      }
      return cityExists;
  }
}
