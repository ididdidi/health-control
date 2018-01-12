package com.dumin.healthcontrol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Displays graphs in TabLayout
 */

public class Graphics extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context activityContext;
    private Database database;
    GraphView graph;
    Pencil pencil;

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

        View v = inflater.inflate(R.layout.graphics, container, false);

        graph = (GraphView) v.findViewById(R.id.graph);
        pencil = new Pencil(activityContext);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new DBLoader(activityContext, database);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG,"onLoadFinished Graphics" );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        database.close();
    }

    private class Pencil {

        private static final String COLUMN_VALUE = "Value";
        private static final String COLUMN_TIME = "Time";
        private Context context;

        private Pencil(@NonNull Context context) {
            this.context = context;
        }

        private void draw(@NonNull String measurement, GraphView graph, Cursor cursor){

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

            if(cursor!=null&&cursor.moveToFirst()){
                do{
                    tmpArr = parseStr( cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_TIME)));
                    calendar.set(tmpArr[2],tmpArr[1]-1,tmpArr[0],tmpArr[3],tmpArr[4]);
                    date = calendar.getTime();

                    tmpArr = parseStr( cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_VALUE)));
                    series0.appendData(new DataPoint(date, tmpArr[0]),true,42);
                    if(measurement.equals(SPrefManager.BLOOD_PRESSURE)){
                        series1.appendData(new DataPoint(date, tmpArr[1]),true,42);
                        series2.appendData(new DataPoint(date, tmpArr[2]),true,42);
                    }
                    //Log.d(LOG_TAG,"cursor.moveToFirst " + date.toString() + " " + value);
                }while(cursor.moveToNext());
            }

            graph.addSeries(series0);
            if(measurement.equals(SPrefManager.BLOOD_PRESSURE)){
                graph.addSeries(series1);
                graph.addSeries(series2);
            }


            // set date label formatter
            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("HH:mm\ndd.MM.yy");
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context, dateFormat));
            graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space

//// set manual x bounds to have nice steps
//            graph.getViewport().setMinX(date.getTime());
//            graph.getViewport().setMaxX(date.getTime());
//            graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
            graph.getGridLabelRenderer().setHumanRounding(false);


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
