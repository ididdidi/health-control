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
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = context.getSharedPreferences(MainActivity.APP_PREFERENCES,
                Context.MODE_PRIVATE);
        Toast.makeText(context, sp.getString(MainActivity.MEASUREMENT,
                MainActivity.BLOOD_PRESSURE), Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.statistics, container, false);
    }

}
