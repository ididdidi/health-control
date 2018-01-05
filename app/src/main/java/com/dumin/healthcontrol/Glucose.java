package com.dumin.healthcontrol;

import android.util.Log;

import java.sql.Time;

/**
 * Created by operator on 05.01.2018.
 */

public class Glucose extends Entry {

    double glucose;

    public Glucose(Long longtime, double glc){
        super(longtime);
        glucose = glc;


        final String LOG_TAG = "myLogs";
        Log.d(LOG_TAG, "new Glucose = " + glucose);
    }

    @Override
    public Time getTime() {
        return super.getTime();
    }

    public double getGlucose() {
        return glucose;
    }

}
