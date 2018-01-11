package com.dumin.healthcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by operator on 11.01.2018.
 */

public class SPrefManager {

    public final static String APP_PREFERENCES = "app_preferences";
    public final static String MEASUREMENT = "measurement";
    public final static String BLOOD_PRESSURE = "SIS / DIA  PUL";
    public final static String GLUCOSE = "Glucose";
    public final static String TEMPERATURE = "Temperature";

    private SharedPreferences appPref;

    public SPrefManager(Context context){
        appPref = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }
    //Allows you to make settings app
    public void savePreferences(@NonNull String key, @NonNull String value) {

        SharedPreferences.Editor editor = appPref.edit();
        editor.putString(key, value);
        editor.apply();
        Log.wtf(getClass().getName(), loadPreferences(MEASUREMENT));
    }

    //Reads the application settings
    public String loadPreferences(@NonNull String key) {
        switch (key) {
            case MEASUREMENT:
                return appPref.getString(key,
                        BLOOD_PRESSURE);
            default:
                return "LoadPreferences no correct";
        }
    }
}
