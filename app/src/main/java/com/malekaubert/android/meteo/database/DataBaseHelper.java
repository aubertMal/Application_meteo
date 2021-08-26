package com.malekaubert.android.meteo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.malekaubert.android.meteo.utils.Constants;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_CITY = "CREATE TABLE "
            + Constants.TABLE_CITY
            + "("
            + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
            + Constants.KEY_ID_CITY + " INTEGER,"
            + Constants.KEY_NAME + " TEXT,"
            + Constants.KEY_TEMP + " TEXT,"
            + Constants.KEY_DESC + " TEXT,"
            + Constants.KEY_RES_ICON + " INTEGER,"
            + Constants.KEY_LAT + " DECIMAL (3, 10),"
            + Constants.KEY_LNG + " DECIMAL (3, 10)"
            + ")";

    public DataBaseHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CITY);
        onCreate(db);
    }
}
