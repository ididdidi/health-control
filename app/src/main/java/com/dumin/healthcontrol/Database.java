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
//    public Cursor getAllData() {
//        return mDB.query(DB_TABLE, null, null, null, null, null, null);
//    }

    public Cursor getBloodPressure() {
        String[] columns  = { COLUMN_ID , COLUMN_SYSTOLIC_PRESSURE + " || ' ' || " + COLUMN_PULSE + " as "+ EntriesList.COLUMN_VALUE_TXT, COLUMN_HEALTH, COLUMN_TIME};
        return mDB.query(BP_TABLE, columns, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addBloodPressure(int sp, int dp, int p, int oh, double time) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SYSTOLIC_PRESSURE, sp);
        cv.put(COLUMN_DIASTOLIC_PRESSURE, dp);
        cv.put(COLUMN_PULSE, p);
        cv.put(COLUMN_HEALTH, oh);
        cv.put(COLUMN_TIME, time);
        mDB.insert(BP_TABLE, null, cv);
        Log.d(LOG_TAG, "addBloodPressure(SQLiteDatabase db)");
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(BP_TABLE, COLUMN_ID + " = " + id, null);
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
//            for (int i = 1; i < 5; i++) {
//                addBloodPressure(120+i, 80+i, 70+i, i, 1.0+i);
//            }

            Log.d(LOG_TAG, "onCreate(SQLiteDatabase db)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    final String LOG_TAG = "myLogs";
}