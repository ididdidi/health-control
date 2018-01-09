package com.dumin.healthcontrol;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Display;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewEntry extends AppCompatActivity implements onSomeEventListener {

    private final String TIME = "time";
    private long timeInSeconds;
    Time androidTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        // Back arrow in action bar
        onCreateActionBar();

        androidTime = new Time(Time.getCurrentTimezone());
        androidTime.setToNow();

        // To set the current time or taken from Bundle
        setKnownTime(savedInstanceState, androidTime);

        // Display date and  time on screen
        onCreateTimeView(androidTime);

    }

    private void onCreateActionBar(){
        // Back arrow in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // To set the current time or taken from Bundle
    private void setKnownTime(Bundle savedInstanceState, @NonNull Time androidTime) {
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            setupAddEntryFragment(intent.getStringExtra(MainActivity.APP_PREFERENCES));
            timeInSeconds = androidTime.toMillis(true)/1000;
        } else {
            timeInSeconds = savedInstanceState.getLong(TIME, androidTime.toMillis(false));
            androidTime.set(timeInSeconds * 1000);
        }
    }

    // Display date and  time on screen
    private void onCreateTimeView(@NonNull Time androidTime){

        TextView dateTextView = (TextView) findViewById(R.id.date);
        dateTextView.setText(androidTime.format("%d.%m.%Y"));

        TextView timeTextView = (TextView) findViewById(R.id.time);
        timeTextView.setText(androidTime.format("%H:%M"));
    }

    // chooses the fragment to display the current measurement
    private void setupAddEntryFragment (@NonNull String measurement){
        FragmentTransaction frTransaction = getFragmentManager().beginTransaction();

        switch (measurement) {
            case MainActivity.BLOOD_PRESSURE: {
                AddBloodPressureEntry addBloodPressureEntry = new AddBloodPressureEntry();
                frTransaction.add(R.id.fragment, addBloodPressureEntry);
                break;
            }
            case MainActivity.GLUCOSE: {
                AddGlucoseEntry addGlucoseEntry = new AddGlucoseEntry();
                frTransaction.add(R.id.fragment, addGlucoseEntry);
                break;
            }
            case MainActivity.TEMPERATURE: {
                AddTemperatureEntry addTemperatureEntry = new AddTemperatureEntry();
                frTransaction.add(R.id.fragment, addTemperatureEntry);
                break;
            }
        }
        frTransaction.commit();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TIME, timeInSeconds);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Sends a signal about the successful adding entry
    @Override
    public void someEvent(boolean update) {
        Intent intent = getIntent();
        intent.putExtra("update",true);
        setResult(RESULT_OK, intent);
        this.finish();
    }
    // Stealing time from the Activity
    @Override
    public long getTimeInSeconds(){
        return timeInSeconds;
    }
}
