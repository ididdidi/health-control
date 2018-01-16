package com.dumin.healthcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

/**
 * Created by operator on 11.01.2018.
 */

public class DBLoader extends CursorLoader {

    public static final String COLUMN_VALUE = "Value";
    public static final String COLUMN_OVERALL_HEALTH = "Overall_health";
    public static final String COLUMN_TIME = "Time";

    private Context context;
    private Database db;

    public DBLoader(@NonNull Context context, @NonNull Database db) {
        super(context);
        this.db = db;
        this.context = context;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor;
        SPrefManager appPref = new SPrefManager(context);

        switch (appPref.loadPreferences(SPrefManager.MEASUREMENT)) {
            case SPrefManager.BLOOD_PRESSURE:
                cursor = db.getBloodPressure(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                break;
            case SPrefManager.GLUCOSE:
                cursor = db.getGlucose(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                break;
            case SPrefManager.TEMPERATURE:
                cursor = db.getTemperature(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                break;
            default: cursor = db.getBloodPressure(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
        }
        return cursor;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged())
            forceLoad();
    }
}
