package com.dumin.healthcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences appPref;
    final static String APP_PREFERENCES = "app_preferences";
    final static String MEASUREMENT = "measurement";
    final static String BLOOD_PRESSURE = "blood_pressure";
    final static String GLUCOSE = "glucose";
    final static String TEMPERATURE = "temperature";

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_entry_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewEntry.class);
                intent.putExtra(APP_PREFERENCES, LoadPreferences(MEASUREMENT));
                startActivityForResult(intent, 1);
            }
        });
        // for rendering pages in TabLayout
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2); // Limit is all
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                SavePreferences(MEASUREMENT, BLOOD_PRESSURE);
                // Handle the camera action
                setupViewPager(viewPager);
                break;
            }
            case R.id.glucose: {
                SavePreferences(MEASUREMENT, GLUCOSE);
                // Handle the camera action
                setupViewPager(viewPager);
                break;
            }
            case R.id.temperature: {
                SavePreferences(MEASUREMENT, TEMPERATURE);
                // Handle the camera action
                setupViewPager(viewPager);
                break;
            }
        default: SavePreferences(MEASUREMENT, BLOOD_PRESSURE);
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                adapter.addFragment(new EntriesList(), "Data");
                adapter.addFragment(new Graphics(), "Graphics");
                adapter.addFragment(new Statistics(), "Statistics");

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data == null) {return;}
        if(data.getBooleanExtra("update",false)){
            this.getSupportLoaderManager().getLoader(0).forceLoad();
        }
    }

    public void SavePreferences(String key, String value) {
        appPref = this.getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = appPref.edit();
        editor.putString(key, value);
        editor.apply();
        Log.wtf(getClass().getName(), LoadPreferences(MEASUREMENT));
    }

    public String LoadPreferences(String key) {
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
