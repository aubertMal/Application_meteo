package com.malekaubert.android.meteo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewCityName;
    private RelativeLayout mRelativeLayoutMeteo;
    private TextView mTextViewDeconnexion;
    private Button mButtonBouton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewCityName = (TextView) findViewById(R.id.text_view_city_name);
        mTextViewCityName.setText(R.string.city_name);
        mRelativeLayoutMeteo = (RelativeLayout) findViewById(R.id.relative_layout_meteo);
        mTextViewDeconnexion = (TextView) findViewById(R.id.text_view_deconnexion);
        mButtonBouton2 = (Button) findViewById(R.id.button2);

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

        mButtonBouton2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Clic sur le bouton 2", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .show();
                    }
                }
        );
    }

    public void buttonClicked(View view){
        switch (view.getId()){
            case R.id.button1:
                Toast.makeText(this, "Clic sur Bouton 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                Toast.makeText(this, "Clic sur Bouton 3", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Tu ne devrais pas être ici!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}