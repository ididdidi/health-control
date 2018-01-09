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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewEntry extends AppCompatActivity implements onSomeEventListener {

    private final String TIME = "time";
    private final String OVERALL_HEALTH = "overallHealth";
    private long timeInSeconds;
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
            setupAddEntryFragment(intent.getStringExtra(MainActivity.APP_PREFERENCES));
        }

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

    // To set the current time or taken from Bundle
    private void setKnownTime(Bundle savedInstanceState, @NonNull Time androidTime) {
        if (savedInstanceState == null) {
            timeInSeconds = androidTime.toMillis(true)/1000;
        } else {
            timeInSeconds = savedInstanceState.getLong(TIME, androidTime.toMillis(false));
            androidTime.set(timeInSeconds * 1000);
        }
    }

    // Display date and  time on screen
    private void onCreateTimeView(Bundle savedInstanceState){

        Time androidTime = new Time(Time.getCurrentTimezone());
        androidTime.setToNow();

        // To set the current time or taken from Bundle
        setKnownTime(savedInstanceState, androidTime);

        TextView dateTextView = (TextView) findViewById(R.id.date);
        dateTextView.setText(androidTime.format("%d.%m.%Y"));

        TextView timeTextView = (TextView) findViewById(R.id.time);
        timeTextView.setText(androidTime.format("%H:%M"));
    }

    private void onCreateRGOverallHealth(Bundle savedInstanceState){
        if (savedInstanceState == null) {
            overallHealth = R.drawable.ic_sentiment_neutral_red;
        }else{
            overallHealth = savedInstanceState.getInt(OVERALL_HEALTH, R.drawable.ic_sentiment_neutral_red);
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
                        overallHealth = R.drawable.ic_sentiment_dissatisfied_red;
                        break;
                    case R.id.sentiment_neutral:
                        overallHealth = R.drawable.ic_sentiment_neutral_red;
                        break;
                    case R.id.sentiment_satisfied:
                        overallHealth = R.drawable.ic_sentiment_satisfied_red;
                        break;
                    case R.id.sentiment_very_satisfied:
                        overallHealth = R.drawable.ic_sentiment_very_satisfied_red;
                        break;
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TIME, timeInSeconds);
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
        return timeInSeconds;
    }
    @Override
    public int getOverallHealth(){
        return overallHealth;
    }
}
