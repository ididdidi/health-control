package com.dumin.healthcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
 * Allows you to add the values of temperature and heart rate in Entry list.
 */

public class AddTemperatureEntry extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private onSomeEventListener updateInformMainActivity;
    private final static String TEMPERATURE = "temperature";
    private double temperature;
    private TextView tvTemper;
    private double minTemper = 34.0;
    private final double ten = 10.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To set the current values or taken from Bundle
        if (savedInstanceState == null) {
            temperature = 36.6;
        } else {
            temperature = savedInstanceState.getDouble(TEMPERATURE, 36.6);
        }

        setHasOptionsMenu(true);    // In order to get back
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_temperature_entry, null);

        final SeekBar seekBar = (SeekBar) v.findViewById(R.id.temperature_sb);
        seekBar.setOnSeekBarChangeListener(this);

        tvTemper = (TextView) v.findViewById(R.id.temperature_value);
        tvTemper.setText(String.valueOf(temperature));
        return v;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        temperature = seekBar.getProgress() / ten + minTemper;
        tvTemper.setText(String.valueOf(temperature));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        temperature = seekBar.getProgress() / ten + minTemper;
        tvTemper.setText(String.valueOf(temperature));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        temperature = seekBar.getProgress() / ten + minTemper;
        tvTemper.setText(String.valueOf(temperature));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(TEMPERATURE, temperature);
    }

    // Updating the information in the fragments to MainActivity
    @Override
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
                addDatabase(getActivity(), updateInformMainActivity.getTimeInSeconds(),
                        R.drawable.ic_sentiment_neutral_black_24dp, temperature);
                updateInformMainActivity.someEvent(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDatabase(@NonNull Context context, long timeInSeconds, int overallHealth, double tmprt){
        Database database = new Database(context);
        database.open();
        database.addTemperature(timeInSeconds, overallHealth, tmprt);
        database.close();
    }
}
