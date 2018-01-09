package com.dumin.healthcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences appPref;
    final static String APP_PREFERENCES = "app_preferences";
    final static String MEASUREMENT = "measurement";
    final static String BLOOD_PRESSURE = "SIS / DIA  PUL";
    final static String GLUCOSE = "Glucose";
    final static String TEMPERATURE = "Temperature";

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creates the Toolbar and NavigationView
        onCreateNavigationView();

        // Button add new entries
        onCreateActionButton();

        // Creates Tabs with pages
        onCreateTabLayout();

    }

    // Creates the Toolbar and NavigationView
    private void onCreateNavigationView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Button add new entries
    private void onCreateActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_entry_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewEntry.class);
                intent.putExtra(APP_PREFERENCES, loadPreferences(MEASUREMENT));
                startActivityForResult(intent, 1);
            }
        });
    }

    // Creates Tabs with pages
    private void onCreateTabLayout() {

        // Add pages in fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2); // Limit is all
        setupViewPager(viewPager);

        // for rendering pages in TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
    // creates pages(fragments) and fills ViewPager
    private void setupViewPager(@NonNull ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new EntriesList(), "Data");
        adapter.addFragment(new Graphics(), "Graphics");
        adapter.addFragment(new Statistics(), "Statistics");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()){
            case R.id.blood_pressure: {
                savePreferences(MEASUREMENT, BLOOD_PRESSURE);
                break;
            }
            case R.id.glucose: {
                savePreferences(MEASUREMENT, GLUCOSE);
                break;
            }
            case R.id.temperature: {
                savePreferences(MEASUREMENT, TEMPERATURE);
                break;
            }
        default: savePreferences(MEASUREMENT, BLOOD_PRESSURE);
        }
        // The message for the loader that the data has been changed
        this.getSupportLoaderManager().getLoader(0).forceLoad();

        TextView viewMs = (TextView) findViewById(R.id.tv_measurement);
        viewMs.setText(loadPreferences(MainActivity.MEASUREMENT));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // gets the result from the intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data == null) {return;}
        if(data.getBooleanExtra("update",false)){
            this.getSupportLoaderManager().getLoader(0).forceLoad();
        }
    }

    //Allows you to make settings app
    private void savePreferences(@NonNull String key, @NonNull String value) {
        appPref = this.getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = appPref.edit();
        editor.putString(key, value);
        editor.apply();
        Log.wtf(getClass().getName(), loadPreferences(MEASUREMENT));
    }

    //Reads the application settings
    private String loadPreferences(@NonNull String key) {
        appPref = this.getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        switch (key) {
            case MEASUREMENT:
                return appPref.getString(key,
                        BLOOD_PRESSURE);
            default:
                return "LoadPreferences no correct";
        }
    }

}
