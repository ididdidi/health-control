package com.dumin.healthcontrol;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Displays statistics in TabLayout
 */

public class Statistics extends Fragment {

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Log.d(LOG_TAG, "Statistics onCreate");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        Toast.makeText(context, sp.getString(MainActivity.MEASUREMENT,
                MainActivity.BLOOD_PRESSURE), Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "Statistics onCreateView");
        return inflater.inflate(R.layout.statistics, container, false);
    }


    final String LOG_TAG = "myLogs";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Statistics onAttach");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Statistics onActivityCreated");
    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Statistics onStart");
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Statistics onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Statistics onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Statistics onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "Statistics onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Statistics onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "Statistics onDetach");
    }


}
