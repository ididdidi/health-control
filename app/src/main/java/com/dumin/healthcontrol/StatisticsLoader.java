package com.dumin.healthcontrol;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by operator on 16.01.2018.
 */

public class StatisticsLoader extends AsyncTaskLoader<StatisticsData> {

    public static final String COLUMN_MAX = "min";
    public static final String COLUMN_MIN = "max";
    public static final String COLUMN_AVG = "avg";
    public static final String COLUMN_MAX_TIME = "min_time";
    public static final String COLUMN_MIN_TIME = "max_time";
    public static final String COLUMN_AVG_TIME = "avg_time";

    private Context context;
    private Database database;

    public StatisticsLoader(Context context, @NonNull Database db) {

        super(context);
        this.context = context;
        this.database = db;
    }

    @Override
    public StatisticsData loadInBackground() {

        SPrefManager appPref = new SPrefManager(context);
        Cursor cursor = database.getStatistics(appPref.loadPreferences(SPrefManager.MEASUREMENT));

        if (cursor != null) {
            String max = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAX));
            String min = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MIN));
            String avg = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AVG));
            long maxT = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MAX_TIME));
            long minT = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_MIN_TIME));
            long avgT = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_AVG_TIME));
            return new StatisticsData(max, maxT, min, minT, avg, avgT);
        }

        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged())
            forceLoad();
    }
}

