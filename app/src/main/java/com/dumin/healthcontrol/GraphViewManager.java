package com.dumin.healthcontrol;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
 * Created by operator on 15.01.2018.
 */

public class GraphViewManager {
    private final int NUMB_GRAPH = 3;
    private GraphView graph;
    private LinearLayout legends;
    private ArrayList<LineGraphSeries<DataPoint>> series;

    private Date dateMin = null;
    private Date dateMax = null;
    private int valueMin = 999;
    private int valueMax = 0;

    public GraphViewManager(View view) {

        graph = (GraphView) view.findViewById(R.id.graph);
        legends = (LinearLayout) view.findViewById(R.id.legends);
        series = new ArrayList(NUMB_GRAPH);
        series.trimToSize();

    }

    public void onDraw(@NonNull Cursor cursor, @NonNull String measurement){

        int color[] = {Color.RED, Color.BLUE, Color.GREEN};
        int count = 0;

        // get data points from a cursor
        ArrayList<DataPoint[]> values = pointsOfCursor(cursor, measurement);

        // remove the GraphView from all the previous series
        graph.removeAllSeries();

        // fill a series of data points and pass them on to GraphView
        do{
            if(series.size()<count+1){
                series.add(new LineGraphSeries<>(values.get(count)));
            }else {
                series.get(count).resetData(values.get(count));
            }

            // set the color of each of the series
            series.get(count).setColor(color[count]);
            series.get(count).setThickness(3);

            // add a new series GraphView
            graph.addSeries(series.get(count));
        }while (++count < values.size() && measurement.equals(SPrefManager.BLOOD_PRESSURE));

        // adjust the view
        customizeGraphView(measurement);

    }

    // Converts the cursor in the DataPoints and sets the minimum and maximum values, returns data points
    private ArrayList<DataPoint[]> pointsOfCursor(@NonNull Cursor cursor, @NonNull String measurement) {

        // Temporary variables
        GregorianCalendar calendar = new GregorianCalendar();
        Date date;
        int[] tmpArr;

        // update any of the extreme values
        dateMin = null;
        dateMax = null;
        valueMin = 999;
        valueMax = 0;

        // Create arrays with the points on the graph
        ArrayList<DataPoint[]> values = new ArrayList<>();
        int count = 0;
        do {
            values.add(new DataPoint[cursor.getCount()]);
        } while (++count < NUMB_GRAPH && measurement.equals(SPrefManager.BLOOD_PRESSURE));

        // Fill the arrays with the points on the graph and find the extreme values
        if (cursor != null && cursor.moveToFirst()) {
            count = 0;
            do {
                // Fill in the coordinates of X date and time
                tmpArr = parseStr(cursor.getString(cursor.getColumnIndexOrThrow(DBLoader.COLUMN_TIME)));
                calendar.set(tmpArr[2], tmpArr[1] - 1, tmpArr[0], tmpArr[3], tmpArr[4]);
                date = calendar.getTime();
                if (dateMin == null || date.before(dateMin)) {
                    dateMin = date;
                }
                if (dateMax == null || date.after(dateMax)) {
                    dateMax = date;
                }

                // Fill in the coordinates of the Y measurements values
                tmpArr = parseStr(cursor.getString(cursor.getColumnIndexOrThrow(DBLoader.COLUMN_VALUE)));
                // If the desired value with three parameters(blood pressure)
                if (measurement.equals(SPrefManager.BLOOD_PRESSURE)) {
                    for (int i = 0; i < values.size(); i++) {
                        values.get(i)[count] = new DataPoint(date, tmpArr[i]);
                        if (valueMin > tmpArr[i]) { valueMin = tmpArr[i]; }
                        if (valueMax < tmpArr[i]) { valueMax = tmpArr[i]; }
                    }
                } else { // If the desired value with one of parameters
                    values.get(0)[count] = new DataPoint(date, cursor.getDouble(cursor.getColumnIndexOrThrow(DBLoader.COLUMN_VALUE)));
                    if (valueMin > tmpArr[0]) { valueMin = tmpArr[0]; }
                    if (valueMax < tmpArr[0]) { valueMax = tmpArr[0]; }
                }
                count++;

            } while (cursor.moveToNext());
        }

        return values;
    }

    // retrieves string numeric sequence and stores them in an integer array
    private int[] parseStr(@NonNull String str) {

        str = str.replaceAll("[^0-9]+", " ");
        String strArr[] = str.split(" ");

        int numArr[] = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            numArr[i] = Integer.parseInt(strArr[i]);
        }

        return numArr;
    }

    // configures a graphical representation
    private void customizeGraphView(  @NonNull String measurement){

        // set Title graph view
        graph.setTitle(measurement);
        graph.setTitleTextSize(36);
        graph.setTitleColor(Color.GRAY);

        // setting extreme values
        graph.getViewport().setMinX(dateMin.getTime() - 3600000);
        graph.getViewport().setMaxX(dateMax.getTime() + 3600000);

        double  deltaY = ( valueMax - valueMin ) * 0.02;
        graph.getViewport().setMinY(valueMin - deltaY);
        graph.getViewport().setMaxY(valueMax + deltaY);

        graph.getViewport().setXAxisBoundsManual(true);

        // set date label formatter
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm\ndd.MM");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext(), dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4); // only 5 because of the space
        graph.getGridLabelRenderer().setNumVerticalLabels(10);

        // configuring the chart legend
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
    }
}
