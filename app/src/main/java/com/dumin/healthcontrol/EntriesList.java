package com.dumin.healthcontrol;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
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

    public static final int LOADER_ID = 0;
    private static final int CM_DELETE_ID = 1;

    private Context activityContext;
    private ListView lvData;
    private Database database;
    private SimpleCursorAdapter scAdapter;
    private SPrefManager appPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();
        appPref = new SPrefManager(activityContext);
        // open the DB connection
        database = new Database(activityContext);
        database.open();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.entries_list, container, false);

        TextView viewMs = (TextView) view.findViewById(R.id.tv_measurement);
        viewMs.setText(appPref.loadPreferences(SPrefManager.MEASUREMENT));

        onCreateAdapter(view);

        return view;
    }

    private void onCreateAdapter(View view){
        // generated columns mapping
        String[] from = new String[] {DBLoader.COLUMN_TIME, DBLoader.COLUMN_OVERALL_HEALTH, DBLoader.COLUMN_VALUE};
        int[] to = new int[] {R.id.time_txt, R.id.overall_health, R.id.value_txt};

        // create adapter and custom list
        scAdapter = new SimpleCursorAdapter(activityContext, R.layout.entries_list_item, null, from, to, 0);
        lvData = (ListView) view.findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        // added context menu to the list
        registerForContextMenu(lvData);

        // create a loader to read data
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
            database.delRec(appPref.loadPreferences(SPrefManager.MEASUREMENT), acmi.id);
            // получаем новый курсор с данными
            getActivity().getSupportLoaderManager().getLoader(EntriesList.LOADER_ID).forceLoad();
            getActivity().getSupportLoaderManager().getLoader(Graphics.LOADER_ID).forceLoad();
            getActivity().getSupportLoaderManager().getLoader(Statistics.LOADER_ID).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onDestroy() {
        super.onDestroy();
        // close the connection when exiting
        database.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new DBLoader(activityContext, database);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}