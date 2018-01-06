package com.dumin.healthcontrol;

import android.content.Context;

import java.sql.Time;

/**
 * Created by operator on 05.01.2018.
 */

public class Temperature  extends Entry {

    double temperature;

    public Temperature(Long longtime, double tmprt){
        super(longtime);
        temperature = tmprt;
    }

    @Override
    public Time getTime() {
        return super.getTime();
    }

    public double getTemperature() {
        return temperature;
    }

    public void addDatabase(Context context){
        Database database;
        database = new Database(context);
        database.open();
        database.addRec("Temperature");
        database.close();
    }
}
