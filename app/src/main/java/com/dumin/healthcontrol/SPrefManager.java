package com.dumin.healthcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Creates and manages display settings.
 */

public class SPrefManager {

    public static final String APP_PREFERENCES = "app_preferences";
    public static final String MEASUREMENT = "measurement";
    public static final String BLOOD_PRESSURE = "SIS / DIA  PUL";
    public static final String GLUCOSE = "Glucose";
    public static final String TEMPERATURE = "Temperature";
    public static final String TIME_FRAME = "Time frame";
    public static final String WEEK = "Week";
    public static final String MONTH = "Month";
    public static final String YEAR = "Year";
    public static final String All_TIME = "All time";

    private SharedPreferences appPref;

    public SPrefManager(Context context) {
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
                return appPref.getString(key, BLOOD_PRESSURE);
            case TIME_FRAME:
                return appPref.getString(key, WEEK);
            default:
                return "";
        }
    }
}
