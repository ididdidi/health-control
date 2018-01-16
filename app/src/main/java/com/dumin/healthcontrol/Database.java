package com.dumin.healthcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Class for the creation and management of database.
 */

public class Database {


    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    // The names of tables and fields database
    private static final String DB_NAME = "HealthControlDB";
    private static final int DB_VERSION = 1;

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_HEALTH = "Health";

    private static final String BP_TABLE = "Blood_pressure";
    private static final String COLUMN_SYSTOLIC_PRESSURE = "Systolic_pressure";
    private static final String COLUMN_DIASTOLIC_PRESSURE = "Diastolic_pressure";
    private static final String COLUMN_PULSE = "Pulse";

    private static final String GLC_TABLE = "Glucose";
    private static final String COLUMN_GLUCOSE_LEVELS = "Glucose_levels";

    private static final String TMPR_TABLE = "Temperature";
    private static final String COLUMN_BODY_TEMPERATURE = "Body_temperature";

    private static final String COLUMN_TIME = "Time";

    // The queries to create tables
    // Blood_pressure
    private static final String DB_CREATE_BP_TABLE =
            "create table " + BP_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_SYSTOLIC_PRESSURE + " integer, " +
                    COLUMN_DIASTOLIC_PRESSURE + " integer, " +
                    COLUMN_PULSE + " integer, " +
                    COLUMN_HEALTH + " integer, " +
                    COLUMN_TIME + " int" +
                    ");";
    // Glucose
    private static final String DB_CREATE_GLC_TABLE =
            "create table " + GLC_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_GLUCOSE_LEVELS + " real, " +
                    COLUMN_HEALTH + " integer, " +
                    COLUMN_TIME + " int" +
                    ");";
    // Temperature
    private static final String DB_CREATE_TMPR_TABLE =
            "create table " + TMPR_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_BODY_TEMPERATURE + " real, " +
                    COLUMN_HEALTH + " integer, " +
                    COLUMN_TIME + " int" +
                    ");";

    private static  final  String ORDER = COLUMN_TIME + " DESC";

    // constructor
    public Database(@NonNull Context ctx) {
        mCtx = ctx;
    }

    // to open a connection
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // close the connection
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // The same requests for multiple tables from the database
    private final String[] formQuery(@NonNull String[] getColumns){
        String[] columns = {
                COLUMN_ID,
                "strftime('%d.%m.%Y \n %H:%M', datetime(" + COLUMN_TIME +
                        ", 'unixepoch', 'localtime')) as " + getColumns[0],
                COLUMN_HEALTH + " as " + getColumns[1],
                getColumns[2]};
        String[] copyColumns = new String[4];
        System.arraycopy (columns, 0, copyColumns, 0, 4);
        return copyColumns;
    }

    // To retrieve data from a table of blood pressure
    public Cursor getBloodPressure(@NonNull String columnTime, @NonNull String columnHealth,
                                   @NonNull String columnValues) {

        String selectTime = COLUMN_TIME + " >= strftime('%s','now','-7 day')";

        String requestValues =  COLUMN_SYSTOLIC_PRESSURE + " || '/' || " + COLUMN_DIASTOLIC_PRESSURE +
                " || ' ' || " + COLUMN_PULSE + " as " + columnValues;

        String[] getColumns = {columnTime, columnHealth, requestValues};
        String[] columns = formQuery(getColumns);

        return mDB.query(BP_TABLE, columns, selectTime, null, null, null, ORDER);
    }
    // To retrieve data from a table of glucose
    public Cursor getGlucose(@NonNull String columnTime, @NonNull String columnHealth,
                             @NonNull String columnValues) {
        String selectTime = COLUMN_TIME + " >= strftime('%s','now','-7 day')";

        String requestValues = "round(" + COLUMN_GLUCOSE_LEVELS + ", 1)" + " as "+ columnValues;

        String[] getColumns = {columnTime, columnHealth, requestValues};
        String[] columns = formQuery(getColumns);

        return mDB.query(GLC_TABLE, columns, selectTime, null, null, null, ORDER);
    }
    // To retrieve data from a table of temperature
    public Cursor getTemperature(@NonNull String columnTime, @NonNull String columnHealth,
                                 @NonNull String columnValues) {
        String selectTime = COLUMN_TIME + " >= strftime('%s','now','-7 day')";

        String requestValues = "round(" + COLUMN_BODY_TEMPERATURE + ", 1)" + " as "+ columnValues;

        String[] getColumns = {columnTime, columnHealth, requestValues};
        String[] columns = formQuery(getColumns);

        return mDB.query(TMPR_TABLE, columns, selectTime, null, null, null, ORDER);
    }

    // to add a record to a table of blood pressure
    public void addBloodPressure(long time, int overallHealth, int systolicPressure,
                                 int diastolicPressure, int pulse) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SYSTOLIC_PRESSURE, systolicPressure);
        cv.put(COLUMN_DIASTOLIC_PRESSURE, diastolicPressure);
        cv.put(COLUMN_PULSE, pulse);
        cv.put(COLUMN_HEALTH, overallHealth);
        cv.put(COLUMN_TIME, time);
        mDB.insert(BP_TABLE, null, cv);
    }

    // to add a record to a table of glucose
    public void addGlucose(long time, int overallHealth, double glucose) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GLUCOSE_LEVELS, glucose);
        cv.put(COLUMN_HEALTH, overallHealth);
        cv.put(COLUMN_TIME, time);
        mDB.insert(GLC_TABLE, null, cv);
    }

    // to add a record to a table of temperature
    public void addTemperature(long time, int overallHealth, double temperature) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BODY_TEMPERATURE, temperature);
        cv.put(COLUMN_HEALTH, overallHealth);
        cv.put(COLUMN_TIME, time);
        mDB.insert(TMPR_TABLE, null, cv);
    }

    // to delete a record from the database
    public void delRec(@NonNull String measurement, long id) {
        switch (measurement) {
            case SPrefManager.BLOOD_PRESSURE:
                mDB.delete(BP_TABLE, COLUMN_ID + " = " + id, null);
                break;
            case SPrefManager.GLUCOSE:
                mDB.delete(GLC_TABLE, COLUMN_ID + " = " + id, null);
                break;
            case SPrefManager.TEMPERATURE:
                mDB.delete(TMPR_TABLE, COLUMN_ID + " = " + id, null);
                break;
            default:
                break;
        }
    }

    public Cursor getStatistics(@NonNull String measurement){

        String table = BP_TABLE;
        String selectValue = COLUMN_SYSTOLIC_PRESSURE;
        String columnsValue = COLUMN_ID + ", " + COLUMN_TIME + ", " + COLUMN_SYSTOLIC_PRESSURE +
                " || '/' || " + COLUMN_DIASTOLIC_PRESSURE + " || ' ' || " + COLUMN_PULSE;
        String selectTime = COLUMN_TIME + " >= strftime('%s','now','-7 day')";

        switch (measurement){
            case SPrefManager.BLOOD_PRESSURE:
                table = BP_TABLE;
                selectValue = COLUMN_SYSTOLIC_PRESSURE;
                columnsValue = COLUMN_ID + ", " + COLUMN_TIME + ", " + COLUMN_SYSTOLIC_PRESSURE +
                        " || '/' || " + COLUMN_DIASTOLIC_PRESSURE + " || ' ' || " + COLUMN_PULSE;
                break;
            case SPrefManager.GLUCOSE:
                table = GLC_TABLE;
                selectValue = COLUMN_GLUCOSE_LEVELS;
                columnsValue = COLUMN_ID + ", " + COLUMN_TIME + ", round(" + COLUMN_GLUCOSE_LEVELS + ", 1)";
                break;
            case SPrefManager.TEMPERATURE:
                table = TMPR_TABLE;
                selectValue = COLUMN_BODY_TEMPERATURE;
                columnsValue = COLUMN_ID + ", " + COLUMN_TIME + ", round(" + COLUMN_BODY_TEMPERATURE + ", 1)";
                break;
            default:
                break;
        }

        String max = StatisticsLoader.COLUMN_MAX;
        String min = StatisticsLoader.COLUMN_MIN;
        String avg = StatisticsLoader.COLUMN_AVG;
        String maxTime = StatisticsLoader.COLUMN_MAX_TIME;
        String minTime = StatisticsLoader.COLUMN_MIN_TIME;
        String avgTime = StatisticsLoader.COLUMN_AVG_TIME;

        String[] columnAVG = {"AVG(" + selectValue + ") as avg"};
        Cursor cursor = mDB.query(table,columnAVG,selectTime,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0){ return null; }
            double avgValue = cursor.getDouble(cursor.getColumnIndexOrThrow("avg"));

        String sbq_max = "( SELECT " + columnsValue + " as " + max +" FROM " + table +
                " WHERE " + selectTime + " ORDER BY " + selectValue + " DESC LIMIT 1 ) as sbq_max";

        String sbq_min = "( SELECT " + columnsValue + " as " + min + " FROM " + table +
                " WHERE " + selectTime + " ORDER BY " + selectValue + " ASC LIMIT 1 ) as sbq_min";

        cursor.moveToFirst();
        Log.d(logTeg, "!" + cursor.getCount() + " " + cursor.getString(cursor.getColumnIndexOrThrow("avg")));

        String sbq_avg = "( SELECT " + columnsValue + " as " + avg + " FROM " + table +
                " WHERE " + selectTime + " AND " + selectValue +
                " <= " + avgValue + " ORDER BY " + selectValue + " DESC LIMIT 1 ) as sbq_avg";

        String query = sbq_max + ", " + sbq_min + ", " + sbq_avg;

        String[] columns = {
                max, "sbq_max." + COLUMN_TIME + " as " + maxTime,
                min, "sbq_min." + COLUMN_TIME + " as " + minTime,
                avg, "sbq_avg." + COLUMN_TIME + " as " + avgTime };

        cursor = mDB.query(query,columns,null,null,null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0){ return null; }
        Log.d(logTeg, "!" + cursor.getCount() + " " + cursor.getString(cursor.getColumnIndexOrThrow("avg")));
        return cursor;
    }

    // class for the creation and management of database
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@NonNull Context context, @NonNull String name, @NonNull CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // create and fill DB
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_BP_TABLE);
            db.execSQL(DB_CREATE_GLC_TABLE);
            db.execSQL(DB_CREATE_TMPR_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
    private final String logTeg = "myLogs";
}