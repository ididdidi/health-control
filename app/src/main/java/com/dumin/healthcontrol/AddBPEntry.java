package com.dumin.healthcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

/**
 * Created by operator on 01.01.2018.
 */

public class AddBPEntry extends Fragment {

    private final String SYSTOLIC_PRESSURE = "systolic_pressure";
    private final String DIASTOLIC_PRESSURE = "diastolic_pressure";
    private final String PULSE = "pulse";
    private onSomeEventListener updateInformMainActivity;
    private int systolic_pressure;
    private NumberPicker numberPicker0;
    private int diastolic_pressure;
    private NumberPicker numberPicker1;
    private int pulse;
    private NumberPicker numberPicker2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {

            systolic_pressure = 120;
            diastolic_pressure = 80;
            pulse = 70;
        } else {
            systolic_pressure = savedInstanceState.getInt(SYSTOLIC_PRESSURE, 120);
            diastolic_pressure = savedInstanceState.getInt(DIASTOLIC_PRESSURE, 80);
            pulse = savedInstanceState.getInt(PULSE, 70);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_bp_entry, null);

        numberPicker0 = (NumberPicker) v.findViewById(R.id.np_systolic_pressure);
        numberPicker0.setMaxValue(300);
        numberPicker0.setMinValue(50);
        numberPicker0.setValue(systolic_pressure);

        numberPicker1 = (NumberPicker) v.findViewById(R.id.np_diastolic_pressure);
        numberPicker1.setMaxValue(150);
        numberPicker1.setMinValue(30);
        numberPicker1.setValue(diastolic_pressure);

        numberPicker2 = (NumberPicker) v.findViewById(R.id.np_pulse);
        numberPicker2.setMaxValue(500);
        numberPicker2.setMinValue(40);
        numberPicker2.setValue(pulse);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SYSTOLIC_PRESSURE, numberPicker0.getValue());
        outState.putInt(DIASTOLIC_PRESSURE, numberPicker1.getValue());
        outState.putInt(PULSE, numberPicker0.getValue());
    }

    // Updating the information in the fragments to MainActivity

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_add_new_entery, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            updateInformMainActivity = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_entry:
                BloodPressure bp = new BloodPressure(updateInformMainActivity.getLongTime(),
                        numberPicker0.getValue(), numberPicker1.getValue(), numberPicker2.getValue());

                updateInformMainActivity.someEvent(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface onSomeEventListener {
        void someEvent(boolean update);     // Sends a signal about the successful adding entry
        long getLongTime();                 // Stealing time from the Activity
    }

}
