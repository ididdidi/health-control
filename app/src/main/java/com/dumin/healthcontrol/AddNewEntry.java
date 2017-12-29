package com.dumin.healthcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddNewEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);
        Intent intent = getIntent();
        Toast.makeText(this, intent.getStringExtra("iSettings"), Toast.LENGTH_SHORT).show();

        // Back arrow in action bar
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        NumberPicker numberPicker0 = (NumberPicker) findViewById(R.id.np_systolic_pressure);
        numberPicker0.setMaxValue(300);
        numberPicker0.setMinValue(50);
        numberPicker0.setValue(120);

        NumberPicker numberPicker1 = (NumberPicker) findViewById(R.id.np_diastolic_pressure);
        numberPicker1.setMaxValue(150);
        numberPicker1.setMinValue(30);
        numberPicker1.setValue(80);

        NumberPicker numberPicker2 = (NumberPicker) findViewById(R.id.np_pulse);
        numberPicker2.setMaxValue(500);
        numberPicker2.setMinValue(40);
        numberPicker2.setValue(70);
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
}
