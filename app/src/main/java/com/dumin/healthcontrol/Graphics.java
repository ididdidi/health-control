package com.dumin.healthcontrol;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Displays graphs in TabLayout
 */

public class Graphics extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SPrefManager appPref;
    private Context activityContext;
    private Database database;
    private  DBLoader dbLoader;
    private GraphViewManager graphs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();

        // open the DB connection
        database = new Database(activityContext);
        database.open();
        appPref = new SPrefManager(activityContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.graphics, container, false);

        // create a loader to read data
        getActivity().getSupportLoaderManager().initLoader(1, null, this);

        // create a new GraphViewManager
        graphs = new GraphViewManager(v);

        // draw a graph
        graphs.onDraw(dbLoader.loadInBackground(),appPref.loadPreferences(SPrefManager.MEASUREMENT));

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        dbLoader = new DBLoader(activityContext, database);
        return dbLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        graphs.onDraw(dbLoader.loadInBackground(),appPref.loadPreferences(SPrefManager.MEASUREMENT));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // close the connection when exiting
        database.close();
    }

}
