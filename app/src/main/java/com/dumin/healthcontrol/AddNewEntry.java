package com.dumin.healthcontrol;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddNewEntry extends AppCompatActivity implements AddBPEntry.onSomeEventListener {

    final private String FRAGMENT_TAG = "fragment_tag";

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

        FragmentTransaction frTransaction = getFragmentManager().beginTransaction();

        if(intent.getStringExtra(MainActivity.APP_PREFERENCES).equals(MainActivity.BLOOD_PRESSURE)) {
            if(savedInstanceState == null) {
                AddBPEntry addBPEntry = new AddBPEntry();
                frTransaction.add(R.id.fragment, addBPEntry,FRAGMENT_TAG);
            }
        }

        frTransaction.commit();

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

    @Override
    public void someEvent(boolean update) {
        final String LOG_TAG = "myLogs";
        Log.d(LOG_TAG, "AddNewEntry updateInformMainActivity");
        Intent intent = new Intent();
        intent.putExtra("update",true);
        setResult(RESULT_OK, intent);
        this.finish();
    }
}
