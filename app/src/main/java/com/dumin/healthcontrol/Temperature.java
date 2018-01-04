package com.dumin.healthcontrol;

import android.util.Log;

import java.sql.Time;

/**
 * Created by operator on 05.01.2018.
 */

public class Temperature  extends Entry {

    double temperature;

    public Temperature(Long longtime, double tmprt){
        super(longtime);
        temperature = tmprt;


        final String LOG_TAG = "myLogs";
        Log.d(LOG_TAG, "new Temperature = " + temperature);
    }

    @Override
    public Time getTime() {
        return super.getTime();
    }

    public double getTemperature() {
        return temperature;
    }

}
