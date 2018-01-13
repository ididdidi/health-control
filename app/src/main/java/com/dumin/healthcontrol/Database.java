package com.dumin.healthcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

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
    private String[] formQuery(@NonNull String[] getColumns){
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

        String requestValues =  COLUMN_SYSTOLIC_PRESSURE + " || '/' || " + COLUMN_DIASTOLIC_PRESSURE +
                " || ' ' || " + COLUMN_PULSE + " as " + columnValues;

        String[] getColumns = {columnTime, columnHealth, requestValues};
        String[] columns = formQuery(getColumns);

        return mDB.query(BP_TABLE, columns, null, null, null, null, null);
    }
    // To retrieve data from a table of glucose
    public Cursor getGlucose(@NonNull String columnTime, @NonNull String columnHealth,
                             @NonNull String columnValues) {
        String requestValues = "round(" + COLUMN_GLUCOSE_LEVELS + ", 1)" + " as "+ columnValues;

        String[] getColumns = {columnTime, columnHealth, requestValues};
        String[] columns = formQuery(getColumns);

        return mDB.query(GLC_TABLE, columns, null, null, null, null, null);
    }
    // To retrieve data from a table of temperature
    public Cursor getTemperature(@NonNull String columnTime, @NonNull String columnHealth,
                                 @NonNull String columnValues) {
        String requestValues = "round(" + COLUMN_BODY_TEMPERATURE + ", 1)" + " as "+ columnValues;

        String[] getColumns = {columnTime, columnHealth, requestValues};
        String[] columns = formQuery(getColumns);

        return mDB.query(TMPR_TABLE, columns, null, null, null, null, null);
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
}