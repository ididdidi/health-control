package com.dumin.healthcontrol;

import android.content.Context;

import java.sql.Time;

/**
 * Created by operator on 05.01.2018.
 */

public class Glucose extends Entry {

    double glucose;

    public Glucose(Long longtime, double glc){
        super(longtime);
        glucose = glc;
    }

//    @Override
//    public Time getTime() {
//        return super.getTime();
//    }

    public double getGlucose() {
        return glucose;
    }

    public void addDatabase(Context context){
        Database database;
        database = new Database(context);
        database.open();
        database.addBloodPressure(120,80, 70,3, 23.3);
        database.close();
    }
}
