package com.dumin.healthcontrol;

import android.app.FragmentTransaction;
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
        Toast.makeText(this, intent.getStringExtra(MainActivity.APP_PREFERENCES),
                Toast.LENGTH_SHORT).show();

        // Back arrow in action bar
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(intent.getStringExtra(MainActivity.APP_PREFERENCES).equals(MainActivity.BLOOD_PRESSURE)) {
            FragmentTransaction frTransaction = getFragmentManager().beginTransaction();
            AddBPEntry addBPEntry = new AddBPEntry();
            frTransaction.replace(R.id.fragment, addBPEntry);
            frTransaction.commit();
        }

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
