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

    Context context;
    private static final int CM_DELETE_ID = 1;
    public static final String COLUMN_VALUE_TXT = "Value_to_txt";
    public static final String COLUMN_OVERALL_HEALTH = "Overall_health";
    public static final String COLUMN_TIME_TXT = "Time_txt";

    ListView lvData;
    Database database;
    SimpleCursorAdapter scAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        // открываем подключение к БД
        database = new Database(context);
        database.open();

        Log.d(LOG_TAG, "EntriesList onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sp = context.getSharedPreferences(MainActivity.APP_PREFERENCES, 0);
        Toast.makeText(context, sp.getString(MainActivity.MEASUREMENT,
                MainActivity.BLOOD_PRESSURE), Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.entries_list, container, false);

        // формируем столбцы сопоставления
        String[] from = new String[] {COLUMN_VALUE_TXT, Database.COLUMN_HEALTH, Database.COLUMN_TIME};
        int[] to = new int[] { R.id.value_txt, R.id.overall_health, R.id.time_txt};

        // создаем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(context, R.layout.entries_list_item, null, from, to, 0);
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
            database.delRec(acmi.id);
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
        return new MyCursorLoader(context, database);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    static class MyCursorLoader extends CursorLoader {

        Database db;

        public MyCursorLoader(Context context, Database db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getBloodPressure();
            return cursor;
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