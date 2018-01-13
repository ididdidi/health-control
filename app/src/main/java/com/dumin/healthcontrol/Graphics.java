package com.dumin.healthcontrol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
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
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;


/**
 * Displays graphs in TabLayout
 */

public class Graphics extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String COLUMN_VALUE = "Value";
    private static final String COLUMN_TIME = "Time";
    private final int NUMB_GRAPH= 3;

    private Context activityContext;
    private Database database;
    private  DBLoader dbLoader;
    GraphView graph;
    private ArrayList<LineGraphSeries<DataPoint>> series;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();

        // open the DB connection
        database = new Database(activityContext);
        database.open();
        series = new ArrayList(NUMB_GRAPH);
        series.trimToSize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.graphics, container, false);

        graph = (GraphView) v.findViewById(R.id.graph);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);

        onDraw(graph,series);
        Log.d(LOG_TAG,"onLoadFinished Graphics " + series.size());
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        dbLoader = new DBLoader(activityContext, database);
        return dbLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        onDraw(graph,series);
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

    private void onDraw(GraphView graph, ArrayList<LineGraphSeries<DataPoint>> series){

        int color[] = {Color.RED, Color.CYAN, Color.GREEN};

        SPrefManager appPref = new SPrefManager(activityContext);
        String measurement = appPref.loadPreferences(SPrefManager.MEASUREMENT);
        graph.removeAllSeries();
        graph.onDataChanged(true, true);
        graph.setTitle(measurement);
        graph.setTitleTextSize(36);
        graph.setTitleColor(Color.GRAY);

        // set date label formatter
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm\ndd.MM.yy");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(activityContext, dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
        graph.getGridLabelRenderer().setNumVerticalLabels(5); // only 4 because of the space

//// set manual x bounds to have nice steps
//            graph.getViewport().setMinX(date.getTime());
//            graph.getViewport().setMaxX(date.getTime());
//            graph.getViewport().setXAxisBoundsManual(true);

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);


        Cursor cursor = dbLoader.loadInBackground();

        ArrayList <DataPoint[]> values = new ArrayList<>();

        int count = 0;
        do{
            values.add(new DataPoint[cursor.getCount()]);
        }while (++count < NUMB_GRAPH && measurement.equals(SPrefManager.BLOOD_PRESSURE));
        
        GregorianCalendar calendar = new GregorianCalendar();
        Date date;
        int [] tmpArr;

        if(cursor!=null&&cursor.moveToFirst()){
        count = 0;
            do{
                tmpArr = parseStr( cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_TIME)));
                calendar.set(tmpArr[2],tmpArr[1]-1,tmpArr[0],tmpArr[3],tmpArr[4]);
                date = calendar.getTime();

                if(measurement.equals(SPrefManager.BLOOD_PRESSURE)){
                    tmpArr = parseStr( cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_VALUE)));
                    for (int i=0; i<values.size(); i++) {
                        values.get(i)[count] = new DataPoint(date, tmpArr[i]);
                    }
                } else {
                        values.get(0)[count] = new DataPoint(date, cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VALUE)));
                }
                count++;

            }while(cursor.moveToNext());
        }

        count = 0;
        do{
            if(series.size()<count+1){
                series.add(new LineGraphSeries<>(values.get(count)));
            }else {
                series.get(count).resetData(values.get(count));
            }
            series.get(count).setColor(color[count]);
            series.get(count).setThickness(3);
            graph.addSeries(series.get(count));
        }while (++count < values.size() && measurement.equals(SPrefManager.BLOOD_PRESSURE));

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

    final String LOG_TAG = "myLogs";
}
