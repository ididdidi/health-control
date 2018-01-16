package com.dumin.healthcontrol;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddNewEntry extends AppCompatActivity implements onSomeEventListener {

    private final String TIME = "time";
    private final String OVERALL_HEALTH = "overallHealth";
    private long timeInMillis;
    private int overallHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        // Back arrow in action bar
        onCreateActionBar();

        // Display date and  time on screen
        onCreateTimeView(savedInstanceState);

            // chooses the fragment to display the current measurement
        if(savedInstanceState==null){
            Intent intent = getIntent();
            setupAddEntryFragment(intent.getStringExtra(SPrefManager.APP_PREFERENCES));
        }

        // Added RadioImage to indicate overall health
        onCreateRGOverallHealth(savedInstanceState);
    }

    private void onCreateActionBar(){
        // Back arrow in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // chooses the fragment to display the current measurement
    private void setupAddEntryFragment (@NonNull String measurement){
        FragmentTransaction frTransaction = getFragmentManager().beginTransaction();

        switch (measurement) {
            case SPrefManager.BLOOD_PRESSURE: {
                AddBloodPressureEntry addBloodPressureEntry = new AddBloodPressureEntry();
                frTransaction.add(R.id.fragment, addBloodPressureEntry);
                break;
            }
            case SPrefManager.GLUCOSE: {
                AddGlucoseEntry addGlucoseEntry = new AddGlucoseEntry();
                frTransaction.add(R.id.fragment, addGlucoseEntry);
                break;
            }
            case SPrefManager.TEMPERATURE: {
                AddTemperatureEntry addTemperatureEntry = new AddTemperatureEntry();
                frTransaction.add(R.id.fragment, addTemperatureEntry);
                break;
            }
        }
        frTransaction.commit();
    }

    // To set the current time or taken from Bundle
    private void setKnownTime(Bundle savedInstanceState, @NonNull Calendar calendar) {
        if (savedInstanceState == null) {
            timeInMillis = calendar.getTimeInMillis();
        } else {
            timeInMillis = savedInstanceState.getLong(TIME, calendar.getTimeInMillis());
            calendar.setTimeInMillis(timeInMillis);
        }
    }

    // Display date and  time on screen
    private void onCreateTimeView(Bundle savedInstanceState){

        Calendar calendar = new GregorianCalendar();

        // To set the current time or taken from Bundle
        setKnownTime(savedInstanceState, calendar);

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");


        TextView dateTextView = (TextView) findViewById(R.id.date);
        dateTextView.setText(dateFormat.format(calendar.getTime()));

        TextView timeTextView = (TextView) findViewById(R.id.time);
        timeTextView.setText(timeFormat.format(calendar.getTime()));
    }

    // Added RadioImage to indicate overall health
    private void onCreateRGOverallHealth(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            overallHealth = R.drawable.ic_sentiment_neutral_grey;
        }else{
            overallHealth = savedInstanceState.getInt(OVERALL_HEALTH, R.drawable.ic_sentiment_neutral_grey);
        }
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_overallHealth);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.sentiment_very_dissatisfied:
                        overallHealth = R.drawable.ic_sentiment_very_dissatisfied_red;
                        break;
                    case R.id.sentiment_dissatisfied:
                        overallHealth = R.drawable.ic_sentiment_dissatisfied_orange;
                        break;
                    case R.id.sentiment_neutral:
                        overallHealth = R.drawable.ic_sentiment_neutral_grey;
                        break;
                    case R.id.sentiment_satisfied:
                        overallHealth = R.drawable.ic_sentiment_satisfied_green;
                        break;
                    case R.id.sentiment_very_satisfied:
                        overallHealth = R.drawable.ic_sentiment_very_satisfied_green_green;
                        break;
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TIME, timeInMillis);
        outState.putInt(OVERALL_HEALTH, overallHealth);
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
        return timeInMillis / 1000;
    }
    @Override
    public int getOverallHealth(){
        return overallHealth;
    }
}
