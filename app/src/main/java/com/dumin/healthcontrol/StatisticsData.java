package com.dumin.healthcontrol;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class for storing statistics.
 */

public class StatisticsData {
    private String maxValue;
    private String minValue;
    private String avgValue;
    private long maxTime;
    private long minTime;
    private long avgTime;

    public StatisticsData(String max, long maxT, String min, long minT, String avg, long avgT){
        maxValue = max;
        minValue = min;
        avgValue = avg;
        maxTime = maxT;
        minTime = minT;
        avgTime = avgT;
    }

    public String getAvgValue() {
        return avgValue;
    }
    public String getTimeMaxValue(){
        Date time = new Date(maxTime*1000);
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy\nHH:mm");
        //dateFormat.format();
        return dateFormat.format(time);
    }

    public String getTimeMinValue(){
        Date time = new Date(minTime*1000);
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy\nHH:mm");
        //dateFormat.format();
        return dateFormat.format(time);
    }
    public String getTimeAvgValue() {
        Date time = new Date(avgTime*1000);
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy\nHH:mm");
        //dateFormat.format();
        return dateFormat.format(time);
    }
    public String getMaxValue(){
        return maxValue;
    }

    public String getMinValue(){
        return minValue;
    }
}
