package com.dumin.healthcontrol;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewEntry extends AppCompatActivity implements onSomeEventListener {

    private final String TIME = "time";
    long longTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);

        // Back arrow in action bar
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Time androidTime = new Time(Time.getCurrentTimezone());
        androidTime.setToNow();

        Intent intent = getIntent();
        Toast.makeText(this, intent.getStringExtra(MainActivity.APP_PREFERENCES),
                Toast.LENGTH_SHORT).show();
        if(savedInstanceState == null) {
            setupAddEntryFragment(intent.getStringExtra(MainActivity.APP_PREFERENCES));
            longTime = androidTime.toMillis(false);
        } else{
            longTime = savedInstanceState.getLong(TIME,androidTime.toMillis(false));
            androidTime.set(longTime);
        }

        TextView dateTextView = (TextView) findViewById(R.id.date);
        dateTextView.setText(androidTime.format("%d.%m.%Y"));
        TextView timeTextView = (TextView) findViewById(R.id.time);
        timeTextView.setText(androidTime.format("%H:%M"));
    }

    public void setupAddEntryFragment (@NonNull String measurement){
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
        outState.putLong(TIME, longTime);
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
    public long getLongTime(){
        return longTime;
    }
}
