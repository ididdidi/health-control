package com.dumin.healthcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.dumin.healthcontrol.Settings.PRM.PARAMETER_CHANGES;
import static com.dumin.healthcontrol.Settings.PRM.PREFERENCES;

/**
 * Class to store app settings
 */

public class Settings {
    public enum PRM{
        PREFERENCES,        // The name of the Preferences file
        PARAMETER_CHANGES,  // Key from Preferences to select the type of parameter changes
        BLOOD_PRESSURE,      // Type of parameter_changes
        GLUCOSE
    }

    private Context context;
    private final int MODE_PRIVATE = 0;

    public Settings(Context ctx){
        context = ctx;
    }

    public void SavePreferences(PRM key, PRM value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCES.toString(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key.toString(), value.toString());
        editor.apply();
        Log.wtf(getClass().getName(), LoadPreferences(PARAMETER_CHANGES));
    }

    public String LoadPreferences(PRM key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCES.toString(), MODE_PRIVATE);
        switch (key) {
            case PARAMETER_CHANGES:
                return sharedPreferences.getString(key.toString(),
                        PRM.BLOOD_PRESSURE.toString());
            default:
                return new String("LoadPreferences no correct");
        }
    }
}
