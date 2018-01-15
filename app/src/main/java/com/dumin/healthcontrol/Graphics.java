package com.dumin.healthcontrol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


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
    LinearLayout legends;
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

        // create a loader to read data
        getActivity().getSupportLoaderManager().initLoader(1, null, this);

        legends = (LinearLayout) v.findViewById(R.id.legends);

        onDraw(graph, series);
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
        onDraw(graph, series);
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

        int color[] = {Color.RED, Color.BLUE, Color.GREEN};

        SPrefManager appPref = new SPrefManager(activityContext);
        String measurement = appPref.loadPreferences(SPrefManager.MEASUREMENT);
        graph.removeAllSeries();

        Cursor cursor = dbLoader.loadInBackground();

        ArrayList <DataPoint[]> values = new ArrayList<>();

        int count = 0;
        do{
            values.add(new DataPoint[cursor.getCount()]);
        }while (++count < NUMB_GRAPH && measurement.equals(SPrefManager.BLOOD_PRESSURE));
        
        GregorianCalendar calendar = new GregorianCalendar();
        Date date;
        Date dateMin = null;
        Date dateMax = null;
        int valueMin = 999;
        int valueMax = 0;
        int [] tmpArr;

        if(cursor!=null&&cursor.moveToFirst()){
        count = 0;
            do{
                tmpArr = parseStr( cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_TIME)));
                calendar.set(tmpArr[2],tmpArr[1]-1,tmpArr[0],tmpArr[3],tmpArr[4]);
                date = calendar.getTime();
                if(dateMin==null || date.before(dateMin)){ dateMin = date; }
                if(dateMax==null || date.after(dateMax)){ dateMax = date;}

                tmpArr = parseStr( cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_VALUE)));
                if(measurement.equals(SPrefManager.BLOOD_PRESSURE)){
                    for (int i=0; i<values.size(); i++) {
                        values.get(i)[count] = new DataPoint(date, tmpArr[i]);
                        if(valueMin > tmpArr[i]){ valueMin = tmpArr[i]; }
                        if(valueMax < tmpArr[i]){ valueMax = tmpArr[i]; }
                    }
                } else {
                    values.get(0)[count] = new DataPoint(date, cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VALUE)));
                    if(valueMin > tmpArr[0]){ valueMin = tmpArr[0]; }
                    if(valueMax < tmpArr[0]){ valueMax = tmpArr[0]; }
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

        graph.setTitle(measurement);
        graph.setTitleTextSize(36);
        graph.setTitleColor(Color.GRAY);

        graph.getViewport().setMinX(dateMin.getTime() - 3600000);
        graph.getViewport().setMaxX(dateMax.getTime() + 3600000);

        double  deltaY = ( valueMax - valueMin ) * 0.02;
        graph.getViewport().setMinY(valueMin - deltaY);
        graph.getViewport().setMaxY(valueMax + deltaY);

        graph.getViewport().setXAxisBoundsManual(true);

        //        // set date label formatter
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm\ndd.MM");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(activityContext, dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 5 because of the space
        graph.getGridLabelRenderer().setNumVerticalLabels(10);

        String titlesSeries[] = { "SIS", "DIA", "PUL" };
        int[] legendViews = { R.id.graph_legend_view0, R.id.graph_legend_view1, R.id.graph_legend_view2 };
        int[] legendTxt = { R.id.graph_legend_txt0, R.id.graph_legend_txt1, R.id.graph_legend_txt2 };

        if(measurement.equals(SPrefManager.BLOOD_PRESSURE)) {
            for(int i=0; i<legendTxt.length; i++){
                TextView legend = legends.findViewById(legendTxt[i]);
                legend.setText(titlesSeries[i]);
                legend.setVisibility(View.VISIBLE);
                View viewLegend = legends.findViewById(legendViews[i]);
                viewLegend.setVisibility(View.VISIBLE);
            }
        }else{
            TextView legend = legends.findViewById(legendTxt[0]);
            legend.setText(measurement);
            for(int i=1; i<legendTxt.length && i<legendViews.length; i++){
                View viewLegend = legends.findViewById(legendViews[i]);
                viewLegend.setVisibility(View.GONE);
                legend = legends.findViewById(legendTxt[i]);
                legend.setVisibility(View.GONE);
            }
        }

        Log.d(LOG_TAG,"onLoadFinished Graphics valueMin " + valueMin);
        Log.d(LOG_TAG,"onLoadFinished Graphics valueMax " + valueMax);
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
