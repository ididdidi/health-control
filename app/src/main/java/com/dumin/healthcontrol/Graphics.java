package com.dumin.healthcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Displays graphs in TabLayout
 */

public class Graphics extends Fragment {

    private Context activityContext;
    private Database database;
    private SimpleCursorAdapter scAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();

        // open the DB connection
        database = new Database(activityContext);
        database.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = activityContext.getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);

        View v = inflater.inflate(R.layout.graphics, container, false);

        GraphView graph = (GraphView) v.findViewById(R.id.graph);
        Pencil pencil = new Pencil(activityContext, database);

        pencil.draw(sp.getString(MainActivity.MEASUREMENT, MainActivity.BLOOD_PRESSURE), graph);

        return v;
    }

    private class Pencil {

        private static final String COLUMN_VALUE = "Value";
        private static final String COLUMN_OVERALL_HEALTH = "Overall_health";
        private static final String COLUMN_TIME = "Time";
        private Context context;
        private Database db;

        public Pencil(@NonNull Context context, @NonNull Database db) {
            this.db = db;
            this.context = context;
        }

        public void draw(@NonNull String measurement, GraphView graph){

//            // activate horizontal zooming and scrolling
//            graph.getViewport().setScalable(true);
//
//            // activate horizontal scrolling
//            graph.getViewport().setScrollable(true);
//
//            // activate horizontal and vertical zooming and scrolling
//            graph.getViewport().setScalableY(true);
//
//            // activate vertical scrolling
//            graph.getViewport().setScrollableY(true);


            LineGraphSeries<DataPoint> series0 = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();

            GregorianCalendar calendar = new GregorianCalendar();;
            Date date;
            double value;
            int [] tmpArr;

            Cursor cursor = getCursor(measurement);
            if(cursor!=null&&cursor.moveToFirst()){
                do{
                    tmpArr = parseStr( cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_TIME)));
                    calendar.set(tmpArr[2],tmpArr[1]-1,tmpArr[0],tmpArr[3],tmpArr[4]);
                    date = calendar.getTime();

                    tmpArr = parseStr( cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_VALUE)));
                    series0.appendData(new DataPoint(date, tmpArr[0]),true,42);
                    if(measurement.equals(MainActivity.BLOOD_PRESSURE)){
                        series1.appendData(new DataPoint(date, tmpArr[1]),true,42);
                        series2.appendData(new DataPoint(date, tmpArr[2]),true,42);
                    }
                    //Log.d(LOG_TAG,"cursor.moveToFirst " + date.toString() + " " + value);
                }while(cursor.moveToNext());
            }

            graph.addSeries(series0);
            if(measurement.equals(MainActivity.BLOOD_PRESSURE)){
                graph.addSeries(series1);
                graph.addSeries(series2);
            }


            // set date label formatter
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

//// set manual x bounds to have nice steps
//            graph.getViewport().setMinX(date.getTime());
//            graph.getViewport().setMaxX(date.getTime());
//            graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
            graph.getGridLabelRenderer().setHumanRounding(false);


        }
        public Cursor getCursor(@NonNull String measurement) {
            Cursor cursor;

            switch (measurement) {
                case MainActivity.BLOOD_PRESSURE:
                    cursor = db.getBloodPressure(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                    break;
                case MainActivity.GLUCOSE:
                    cursor = db.getGlucose(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                    break;
                case MainActivity.TEMPERATURE:
                    cursor = db.getTemperature(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                    break;
                default: cursor = db.getBloodPressure(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
            }
            return cursor;
        }

        private int[] parseStr(String str){
            str = str.replaceAll("[^0-9]+", " ");
            String strArr[] = str.split(" ");
            int numArr[] = new int[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                numArr[i] = Integer.parseInt(strArr[i]);
            }
            int ret[] = new int [strArr.length];
            System.arraycopy(numArr,0,ret,0,strArr.length);
            return ret;
        }

    }
    final String LOG_TAG = "myLogs";
}
