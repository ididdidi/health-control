package com.dumin.healthcontrol;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to store app settings
 */

public class Settings {
    public enum APP{
        PREFERENCES,        // The name of the Preferences file
        PARAMETER_CHANGES,  // Key from Preferences to select the type of parameter changes
        BLOOD_PRESSURE      // Type of parameter_changes
    }

    private Context context;
    private final int MODE_PRIVATE = 0;

    public Settings(Context ctx){
        context = ctx;
    }

    public void SavePreferences(APP key, APP value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Settings.APP.PREFERENCES.toString(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key.toString(), value.toString());
        editor.apply();
    }

    public String LoadPreferences(APP key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Settings.APP.PREFERENCES.toString(), MODE_PRIVATE);
        switch (key) {
            case PARAMETER_CHANGES:
                return sharedPreferences.getString(key.toString(),
                        APP.BLOOD_PRESSURE.toString());
            default:
                return new String("LoadPreferences no correct");
        }
    }
}
