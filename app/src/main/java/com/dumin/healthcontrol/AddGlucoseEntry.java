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
import android.widget.TextView;

import com.github.shchurov.horizontalwheelview.HorizontalWheelView;

import java.util.Locale;

/**
 * Allows you to add the values of glucose and heart rate in Entry list.
 */

public class AddGlucoseEntry extends Fragment {

    private final String GLUCOSE = "glucose";
    private onSomeEventListener updateInformMainActivity;
    private HorizontalWheelView horizontalWheelView;
    private TextView tvGlucose;
    private double glucose;
    private final double ten = 10.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // To set the current values or taken from Bundle
        if (savedInstanceState == null) {
            glucose = 5.6;
        } else {
            glucose = savedInstanceState.getDouble(GLUCOSE, 5.6);
        }

        setHasOptionsMenu(true);    // In order to get back
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_glucose_entry, null);

        horizontalWheelView = (HorizontalWheelView) v.findViewById(R.id.horizontalWheelView);
        horizontalWheelView.setOnlyPositiveValues(true);
        horizontalWheelView.setDegreesAngle(56f);

        tvGlucose = (TextView) v.findViewById(R.id.tv_glucose_value);
        tvGlucose.setText(String.valueOf(glucose));

        setupListeners();   // Starts the listener
        updateText();       // Necessary for correct display after screen rotation
        return v;
    }

    // Listening to touch the screen and change the values
    private void setupListeners() {
        horizontalWheelView.setListener(new HorizontalWheelView.Listener() {
            @Override
            public void onRotationChanged(double radians) {
                updateValue();
                updateText();
            }
        });
    }

    // Sets a new value of the measurement result
    private void updateValue() {
        glucose = horizontalWheelView.getDegreesAngle() / ten;
    }

    // Displays change parameter values in TextView
    private void updateText() {
        String text = String.format(Locale.US, "%.1f", glucose);
        tvGlucose.setText(text);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(GLUCOSE, glucose);
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
                        R.drawable.ic_sentiment_neutral_black_24dp, glucose);
                updateInformMainActivity.someEvent(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Add the values in the database
    private void addDatabase(@NonNull Context context, long timeInSeconds, int overallHealth, double glc){
        Database database = new Database(context);
        database.open();
        database.addGlucose(timeInSeconds, overallHealth, glc);
        database.close();
    }
}
