package com.dumin.healthcontrol;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


/**
 * Displays data in TabLayout
 */

public class EntriesList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context ativityContext;
    private static final int CM_DELETE_ID = 1;
    public static final String COLUMN_VALUE = "Value";
    public static final String COLUMN_OVERALL_HEALTH = "Overall_health";
    public static final String COLUMN_TIME = "Time";

    ListView lvData;
    Database database;
    SimpleCursorAdapter scAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ativityContext = getActivity();

        // открываем подключение к БД
        database = new Database(ativityContext);
        database.open();

        Log.d(LOG_TAG, "EntriesList onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(ativityContext, loadPreferences(MainActivity.MEASUREMENT), Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.entries_list, container, false);

        // формируем столбцы сопоставления
        String[] from = new String[] {COLUMN_VALUE, COLUMN_OVERALL_HEALTH, COLUMN_TIME};
        int[] to = new int[] { R.id.value_txt, R.id.overall_health, R.id.time_txt};

        // создаем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(ativityContext, R.layout.entries_list_item, null, from, to, 0);
        lvData = (ListView) view.findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);

        // создаем лоадер для чтения данных
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        Log.d(LOG_TAG, "EntriesList onCreateView");
        return view;
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, R.string.delete_record);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            database.delRec(loadPreferences(MainActivity.MEASUREMENT), acmi.id);
            // получаем новый курсор с данными
            getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onDestroy() {
        super.onDestroy();
        // закрываем подключение при выходе
        database.close();

        Log.d(LOG_TAG, "EntriesList onDestroy");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(ativityContext, database);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    static class MyCursorLoader extends CursorLoader {

        Context context;
        Database db;

        public MyCursorLoader(Context context, Database db) {
            super(context);
            this.db = db;
            this.context = context;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor;
            SharedPreferences appPref;
            appPref = context.getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);

            switch (appPref.getString(MainActivity.MEASUREMENT, MainActivity.BLOOD_PRESSURE)) {
                case MainActivity.APP_PREFERENCES:
                    cursor = db.getBloodPressure(COLUMN_VALUE, COLUMN_OVERALL_HEALTH, COLUMN_TIME);
                    break;
                case MainActivity.GLUCOSE:
                    cursor = db.getGlucose(COLUMN_VALUE, COLUMN_OVERALL_HEALTH, COLUMN_TIME);
                    break;
                case MainActivity.TEMPERATURE:
                    cursor = db.getTemperature(COLUMN_VALUE, COLUMN_OVERALL_HEALTH, COLUMN_TIME);
                    break;
                default: cursor = db.getBloodPressure(COLUMN_VALUE, COLUMN_OVERALL_HEALTH, COLUMN_TIME);
            }
            return cursor;
        }

    }

    public String loadPreferences(String key) {
        SharedPreferences appPref;
        appPref = ativityContext.getSharedPreferences(
                MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        switch (key) {
            case MainActivity.MEASUREMENT:
                return appPref.getString(key,
                        MainActivity.BLOOD_PRESSURE);
            default:
                return "LoadPreferences no correct";
        }

    }

    final String LOG_TAG = "myLogs";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "EntriesList onAttach");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "EntriesList onActivityCreated");
    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "EntriesList onStart");
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "EntriesList onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "EntriesList onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "EntriesList onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "EntriesList onDestroyView");
    }

    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "EntriesList onDetach");
    }

}