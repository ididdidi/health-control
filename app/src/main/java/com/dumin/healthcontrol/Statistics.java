package com.dumin.healthcontrol;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Displays statistics in TabLayout
 */

public class Statistics extends Fragment implements LoaderManager.LoaderCallbacks<StatisticsData> {

    public static final int LOADER_ID = 2;
    private SPrefManager appPref;
    private Context activityContext;
    private Database database;
    private StatisticsLoader statisticLoader;
    private View view;
    private StatisticsData statisticsData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();
        // open the DB connection
        appPref = new SPrefManager(activityContext);
        database = new Database(activityContext);
        database.open();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistics, container, false);

        statisticsData = statisticLoader.loadInBackground();
        setStatistic(view, statisticsData);
        return view;
    }

    @Override
    public Loader<StatisticsData> onCreateLoader(int id, Bundle args) {
        statisticLoader = new StatisticsLoader(activityContext, database);
        return statisticLoader;
    }

    @Override
    public void onLoadFinished(Loader<StatisticsData> loader, StatisticsData data) {
        setStatistic(view, data);
    }

    @Override
    public void onLoaderReset(Loader<StatisticsData> loader) {
        database.close();
    }

    private void setStatistic(@NonNull View view, @NonNull StatisticsData statisticsData) {
        TextView title = view.findViewById(R.id.statistics);
        title.setText(appPref.loadPreferences(SPrefManager.MEASUREMENT));

        TextView maxValue = view.findViewById(R.id.value_max);
        maxValue.setText(statisticsData.getMaxValue());

        TextView timeMaxValue = view.findViewById(R.id.time_max);
        timeMaxValue.setText(statisticsData.getTimeMaxValue());

        TextView minValue = view.findViewById(R.id.value_min);
        minValue.setText(statisticsData.getMinValue());

        TextView timeMinValue = view.findViewById(R.id.time_min);
        timeMinValue.setText(statisticsData.getTimeMinValue());

        TextView avgValue = view.findViewById(R.id.value_avg);
        avgValue.setText(statisticsData.getAvgValue());

        TextView timeAvgValue = view.findViewById(R.id.time_avg);
        timeAvgValue.setText(statisticsData.getTimeAvgValue());
    }

}
