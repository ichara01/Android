package com.example.ioanna.habittracking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.ioanna.habittracking.HabitTrackingContract.SQL_CREATE_HABIT_ENTRIES;
import static com.example.ioanna.habittracking.HabitTrackingContract.SQL_CREATE_HABIT_TRACKING_ENTRIES;
import static com.example.ioanna.habittracking.HabitTrackingContract.SQL_DELETE_HABIT_ENTRIES;
import static com.example.ioanna.habittracking.HabitTrackingContract.SQL_DELETE_HABIT_TRACKING_ENTRIES;

/**
 * Created by Ioanna on 24/07/2017.
 */

public class HabitTrackingDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HabitTracking.db";

    public HabitTrackingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        //Create Habit table
        db.execSQL(SQL_CREATE_HABIT_ENTRIES);
        Log.i("onCreate", "Habit table has been created");
        //Create Habit Tracking table
        db.execSQL(SQL_CREATE_HABIT_TRACKING_ENTRIES);
        Log.i("onCreate", "Habit Tracking table has been created");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_HABIT_TRACKING_ENTRIES);
        Log.i("onUpgrade", "Habit table has been deleted");
        db.execSQL(SQL_DELETE_HABIT_ENTRIES);
        Log.i("onUpgrade", "Habit Tracking table has been deleted");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
