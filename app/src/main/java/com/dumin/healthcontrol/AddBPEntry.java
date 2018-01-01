package com.dumin.healthcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

/**
 * Created by operator on 01.01.2018.
 */

public class AddBPEntry extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_bp_entry, null);

        NumberPicker numberPicker0 = (NumberPicker) v.findViewById(R.id.np_systolic_pressure);
        numberPicker0.setMaxValue(300);
        numberPicker0.setMinValue(50);
        numberPicker0.setValue(120);

        NumberPicker numberPicker1 = (NumberPicker) v.findViewById(R.id.np_diastolic_pressure);
        numberPicker1.setMaxValue(150);
        numberPicker1.setMinValue(30);
        numberPicker1.setValue(80);

        NumberPicker numberPicker2 = (NumberPicker) v.findViewById(R.id.np_pulse);
        numberPicker2.setMaxValue(500);
        numberPicker2.setMinValue(40);
        numberPicker2.setValue(70);

        return v;
    }
    final String LOG_TAG = "myLogs";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "AddBPEntry onAttach");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "AddBPEntry onActivityCreated");
    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "AddBPEntry onStart");
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "AddBPEntry onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "AddBPEntry onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "AddBPEntry onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "AddBPEntry onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "AddBPEntry onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "AddBPEntry onDetach");
    }
}
