package com.dumin.healthcontrol;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Displays data in TabLayout
 */

public class EntriesList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;
    private static final String COLUMN_VALUE = "Value";
    private static final String COLUMN_OVERALL_HEALTH = "Overall_health";
    private static final String COLUMN_TIME = "Time";

    private Context activityContext;
    private ListView lvData;
    private Database database;
    private SimpleCursorAdapter scAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();

        // open the DB connection
        database = new Database(activityContext);
        database.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.entries_list, container, false);

        TextView viewMs = (TextView) view.findViewById(R.id.tv_measurement);
        viewMs.setText(loadPreferences(MainActivity.MEASUREMENT));

        // формируем столбцы сопоставления
        String[] from = new String[] {COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE};
        int[] to = new int[] {R.id.time_txt, R.id.overall_health, R.id.value_txt};

        // создаем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(activityContext, R.layout.entries_list_item, null, from, to, 0);
        lvData = (ListView) view.findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);

        // создаем лоадер для чтения данных
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(activityContext, database);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    static private class MyCursorLoader extends CursorLoader {

        private Context context;
        private Database db;

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
                case MainActivity.BLOOD_PRESSURE:
                    cursor = db.getBloodPressure(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                    break;
                case MainActivity.GLUCOSE:
                    cursor = db.getGlucose(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                    break;
                case MainActivity.TEMPERATURE:
                    cursor = db.getTemperature(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
                    break;
                default: cursor = db.getBloodPressure(COLUMN_TIME, COLUMN_OVERALL_HEALTH, COLUMN_VALUE);
            }
            return cursor;
        }

    }

    public String loadPreferences(String key) {
        SharedPreferences appPref;
        appPref = activityContext.getSharedPreferences(
                MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        switch (key) {
            case MainActivity.MEASUREMENT:
                return appPref.getString(key,
                        MainActivity.BLOOD_PRESSURE);
            default:
                return "LoadPreferences no correct";
        }

    }

}