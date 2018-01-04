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
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by operator on 04.01.2018.
 */

public class AddTemperatureEntry extends Fragment implements SeekBar.OnSeekBarChangeListener {

    final static String TEMPERATURE = "temperature";
    private onSomeEventListener updateInformMainActivity;
    private double temperature;
    private TextView temper_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            temperature = 36.6;
        } else {
            temperature = savedInstanceState.getDouble(TEMPERATURE, 36.6);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_temperature_entry, null);

        final SeekBar seekBar = (SeekBar) v.findViewById(R.id.temperature_sb);
        seekBar.setOnSeekBarChangeListener(this);

        temper_tv = (TextView) v.findViewById(R.id.temperature_value);
        temper_tv.setText(String.valueOf(temperature));
        return v;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        temperature = seekBar.getProgress() / 10.0 + 32;
        temper_tv.setText(String.valueOf(temperature));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        temperature = seekBar.getProgress() / 10.0 + 32;
        temper_tv.setText(String.valueOf(temperature));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        temperature = seekBar.getProgress() / 10.0 + 32;
        temper_tv.setText(String.valueOf(temperature));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(TEMPERATURE, temperature);
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
//                Temperature tmprt = new Temperature(updateInformMainActivity.getLongTime(),
//                        temperature);

                updateInformMainActivity.someEvent(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
