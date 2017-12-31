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
 * Displays graphs in TabLayout
 */

public class Graphics extends Fragment {

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        Log.d(LOG_TAG, "Graphics onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        Toast.makeText(context, sp.getString(MainActivity.MEASUREMENT,
                MainActivity.BLOOD_PRESSURE), Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "Graphics onCreateView");
        return inflater.inflate(R.layout.graphics, container, false);
    }


    final String LOG_TAG = "myLogs";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Graphics onAttach");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Graphics onActivityCreated");
    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Graphics onStart");
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Graphics onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Graphics onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Graphics onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "Graphics onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Graphics onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "Graphics onDetach");
    }

}
