package com.dumin.healthcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by operator on 06.01.2018.
 */

public class Database {

    private static final String DB_NAME = "HealthControlDB";
    private static final int DB_VERSION = 1;

    private static final String OVERALL_HEALTH ="Overall_health";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HEALTH = "Health";

    private static final String BP_TABLE = "Blood_pressure";
    public static final String COLUMN_SYSTOLIC_PRESSURE = "Systolic_pressure";
    public static final String COLUMN_DIASTOLIC_PRESSURE = "Diastolic_pressure";
    public static final String COLUMN_PULSE = "Pulse";

    private static final String GLC_TABLE = "Glucose";
    public static final String COLUMN_GLUCOSE_LEVELS = "Glucose_levels";

    private static final String TMPR_TABLE = "Temperature";
    public static final String COLUMN_BODY_TEMPERATURE = "Body_temperature";

    public static final String COLUMN_TIME = "Time";

    private static final String DB_CREATE_OVERALL_HEALTH =
            "create table " + OVERALL_HEALTH + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_HEALTH + " text" +

                    ");";

    private static final String DB_CREATE_BP_TABLE =
            "create table " + BP_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_SYSTOLIC_PRESSURE + " integer, " +
                    COLUMN_DIASTOLIC_PRESSURE + " integer, " +
                    COLUMN_PULSE + " integer, " +
                    COLUMN_HEALTH + " integer, " +
                    COLUMN_TIME + " real" +
                    ");";

    private static final String DB_CREATE_GLC_TABLE =
            "create table " + GLC_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_GLUCOSE_LEVELS + " real, " +
                    COLUMN_HEALTH + " integer, " +
                    COLUMN_TIME + " real" +
                    ");";

    private static final String DB_CREATE_TMPR_TABLE =
            "create table " + TMPR_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_BODY_TEMPERATURE + " real, " +
                    COLUMN_HEALTH + " integer, " +
                    COLUMN_TIME + " real" +
                    ");";

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public Database(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE

    public Cursor getBloodPressure(String columnValues, String columnHealth, String columnTime) {
        String requestValues =  COLUMN_SYSTOLIC_PRESSURE + " || '/' || " + COLUMN_DIASTOLIC_PRESSURE +
                " || ' ' || " + COLUMN_PULSE + " as " + columnValues;
        String requestHealth = COLUMN_HEALTH + " as "+ columnHealth;
        String requestTime = COLUMN_TIME + " as "+ columnTime;
        String[] columns  = { COLUMN_ID , requestValues, requestHealth, requestTime};

        return mDB.query(BP_TABLE, columns, null, null, null, null, null);
    }

    public Cursor getGlucose(String columnValues, String columnHealth, String columnTime) {
        String requestValues = COLUMN_GLUCOSE_LEVELS + " as "+ columnValues;
        String requestHealth = COLUMN_HEALTH + " as "+ columnHealth;
        String requestTime = COLUMN_TIME + " as "+ columnTime;
        String[] columns  = { COLUMN_ID , requestValues, requestHealth, requestTime};
        return mDB.query(GLC_TABLE, columns, null, null, null, null, null);
    }

    public Cursor getTemperature(String columnValues, String columnHealth, String columnTime) {
        String requestValues = COLUMN_BODY_TEMPERATURE + " as "+ columnValues;
        String requestHealth = COLUMN_HEALTH + " as "+ columnHealth;
        String requestTime = COLUMN_TIME + " as "+ columnTime;
        String[] columns  = { COLUMN_ID , requestValues, requestHealth, requestTime};
        return mDB.query(TMPR_TABLE, columns, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addBloodPressure(int systolicPressure, int diastolicPressure,
                                 int pulse, int overallHealth, double time) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SYSTOLIC_PRESSURE, systolicPressure);
        cv.put(COLUMN_DIASTOLIC_PRESSURE, diastolicPressure);
        cv.put(COLUMN_PULSE, pulse);
        cv.put(COLUMN_HEALTH, overallHealth);
        cv.put(COLUMN_TIME, time);
        mDB.insert(BP_TABLE, null, cv);
    }

    public void addGlucose(double glucose, int overallHealth, double time) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GLUCOSE_LEVELS, glucose);
        cv.put(COLUMN_HEALTH, overallHealth);
        cv.put(COLUMN_TIME, time);
        mDB.insert(GLC_TABLE, null, cv);
    }

    public void addTemperature(double temperature, int overallHealth, double time) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_BODY_TEMPERATURE, temperature);
        cv.put(COLUMN_HEALTH, overallHealth);
        cv.put(COLUMN_TIME, time);
        mDB.insert(TMPR_TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(String measurement, long id) {
        switch (measurement) {
            case MainActivity.BLOOD_PRESSURE:
                mDB.delete(BP_TABLE, COLUMN_ID + " = " + id, null);
                break;
            case MainActivity.GLUCOSE:
                mDB.delete(GLC_TABLE, COLUMN_ID + " = " + id, null);
                break;
            case MainActivity.TEMPERATURE:
                mDB.delete(TMPR_TABLE, COLUMN_ID + " = " + id, null);
                break;
            default:
                break;
        }
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DB_CREATE_BP_TABLE);
            db.execSQL(DB_CREATE_GLC_TABLE);
            db.execSQL(DB_CREATE_TMPR_TABLE);
            db.execSQL(DB_CREATE_OVERALL_HEALTH);

            Log.d(LOG_TAG, "onCreate(SQLiteDatabase db)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    final String LOG_TAG = "myLogs";
}