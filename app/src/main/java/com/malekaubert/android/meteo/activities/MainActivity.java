package com.malekaubert.android.meteo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.malekaubert.android.meteo.R;
import com.malekaubert.android.meteo.activities.FavoriteActivity;
import com.malekaubert.android.meteo.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewCityName;
    private RelativeLayout mRelativeLayoutMeteo;
    private TextView mTextViewDeconnexion;
    private EditText mEditTextSaisie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCityName = (TextView) findViewById(R.id.text_view_city_name);
        mTextViewCityName.setText(R.string.city_name);
        mRelativeLayoutMeteo = (RelativeLayout) findViewById(R.id.relative_layout_meteo);
        mTextViewDeconnexion = (TextView) findViewById(R.id.text_view_deconnexion);
        mEditTextSaisie = (EditText) findViewById(R.id.edit_text_saisie);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnected()){
            Log.d("TAG", "Oui je suis connectée");
            mTextViewDeconnexion.setVisibility(View.GONE);
            mRelativeLayoutMeteo.setVisibility(View.VISIBLE);
        } else {
            Log.d("TAG", "NON, je ne suis pas connectée");
            mRelativeLayoutMeteo.setVisibility(View.GONE);
            mTextViewDeconnexion.setVisibility(View.VISIBLE);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FavoriteActivity.class);
                intent.putExtra(Utils.EDIT_TEXT_KEY,mEditTextSaisie.getText().toString());
                startActivity(intent);
            }
        });
    }
}