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

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.malekaubert.android.meteo.R;
import com.malekaubert.android.meteo.adapters.FavoriteAdapter;
import com.malekaubert.android.meteo.models.City;
import com.malekaubert.android.meteo.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FavoriteActivity extends AppCompatActivity {

    private ArrayList<City> mCities = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;
    private Context mContext;
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            FavoriteAdapter.ViewHolder viewHolderSwipped= (FavoriteAdapter.ViewHolder) viewHolder;
            City cityToRemove = mCities.get(viewHolderSwipped.position);
            boolean cityExists = false;
            mCities.remove(viewHolderSwipped.position);
            mAdapter.notifyDataSetChanged();
            for (City city : mCities) {
                if (StringUtils.equals(city.mName,cityToRemove.mName)){
                    cityExists = true;
                }
            }
            if (!cityExists){

                Snackbar.make(findViewById(R.id.my_coordinator_layout),cityToRemove.mName + " va être supprimée",Snackbar.LENGTH_LONG)
                        .setAction("Annuler", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCities.add(viewHolderSwipped.position,cityToRemove);
                                Log.d("TAG","Suppression annulée");
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_favorite);
        Bundle extras = getIntent().getExtras();

        mRecyclerView = findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new FavoriteAdapter(this,mCities);
        mRecyclerView.setAdapter(mAdapter);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

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

                            OkHttpClient okHttpClient = new OkHttpClient();
                            Handler handler = new Handler();
                            Request request =
                                    new Request.Builder()
                                            .url(
                                                    "https://api.openweathermap.org/data/2.5/weather?q="+editTextCityName.getText()+"&units=Metric&appid="+Utils.OPEN_WEATHER_KEY)
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
                                                                try {
                                                                    mCities.add(new City(stringJson));
                                                                    mAdapter.notifyDataSetChanged();
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                        }
                    }
                });
                builder.setNegativeButton("Annuler", null);
                builder.create().show();
            }
        });
    }
}