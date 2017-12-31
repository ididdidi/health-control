package com.dumin.healthcontrol;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Displays data in TabLayout
 */

public class Entries extends Fragment {

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Log.d(LOG_TAG, "Entries onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = context.getSharedPreferences(MainActivity.APP_PREFERENCES, 0);
        Toast.makeText(context, sp.getString(MainActivity.MEASUREMENT,
                MainActivity.BLOOD_PRESSURE), Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "Entries onCreateView");
        return inflater.inflate(R.layout.entries, container, false);
    }

    final String LOG_TAG = "myLogs";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Entries onAttach");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Entries onActivityCreated");
    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Entries onStart");
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Entries onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Entries onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Entries onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "Entries onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Entries onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "Entries onDetach");
    }


}