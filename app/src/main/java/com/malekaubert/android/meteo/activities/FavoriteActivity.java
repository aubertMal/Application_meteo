package com.malekaubert.android.meteo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.malekaubert.android.meteo.R;
import com.malekaubert.android.meteo.adapters.FavoriteAdapter;
import com.malekaubert.android.meteo.models.City;
import com.malekaubert.android.meteo.utils.Utils;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private ArrayList<City> mCities;
    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_favorite);
        Bundle extras = getIntent().getExtras();
        initCities();

        mRecyclerView = findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FavoriteAdapter(this,mCities);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite,null);
                final EditText editTextCityName = (EditText) v.findViewById(R.id.edit_text_add_favorite);

                builder.setView(v);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editTextCityName.getText().length()!=0){
                            Log.d("TAG","La ville ajoutée est " + editTextCityName.getText());
                            mCities.add(new City(editTextCityName.getText().toString(),"Ensoleillé","27° C",R.drawable.weather_sunny_white));
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Annuler", null);
                builder.create().show();
            }
        });
    }

    void initCities(){
        mCities = new ArrayList<>();
        City city1 = new City("Montréal", "Légères pluies", "22°C", R.drawable.weather_rainy_grey);
        City city2 = new City("New York", "Ensoleillé", "22°C", R.drawable.weather_sunny_grey);
        City city3 = new City("Paris", "Nuageux", "24°C", R.drawable.weather_foggy_grey);
        City city4 = new City("Toulouse", "Pluies modérées", "20°C", R.drawable.weather_rainy_grey);
        mCities.add(city1);
        mCities.add(city2);
        mCities.add(city3);
        mCities.add(city4);
    }
}