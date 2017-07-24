package com.example.ioanna.habittracking;

import android.provider.BaseColumns;

/**
 * Created by Ioanna on 24/07/2017.
 */

public final class HabitTrackingContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private HabitTrackingContract() {}

    /* Inner class that defines the table contents */
    public static class HabitEntry implements BaseColumns {
        public static final String TABLE_NAME = "habits";
        public static final String COLUMN_HABIT_CODE = "code";
        public static final String COLUMN_HABIT_NAME = "name";
        public static final String COLUMN_HABIT_CREATEDON = "createdOn";
    }

    public static abstract class HabitTrackingEntry implements BaseColumns {
        public static final String TABLE_NAME = "habitTracking";
        public static final String COLUMN_HABITTRACKING_USERID = "userID";
        public static final String COLUMN_HABITTRACKING_CODE = "code";
        public static final String COLUMN_HABITTRACKING_CREATEDON = "createdOn";
    }

    //Habits
    protected static final String SQL_CREATE_HABIT_ENTRIES =
            "CREATE TABLE " + HabitEntry.TABLE_NAME + " (" +
                    HabitEntry.COLUMN_HABIT_CODE + " INTEGER PRIMARY KEY," +
                    HabitEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL," +
                    HabitEntry.COLUMN_HABIT_CREATEDON + " DEFAULT CURRENT_TIMESTAMP)";

    protected static final String SQL_DELETE_HABIT_ENTRIES =
            "DROP TABLE IF EXISTS " + HabitEntry.TABLE_NAME;

    //Habit Tracking
    protected static final String SQL_CREATE_HABIT_TRACKING_ENTRIES =
            "CREATE TABLE " + HabitTrackingEntry.TABLE_NAME + " (" +
                    HabitTrackingEntry.COLUMN_HABITTRACKING_USERID + " TEXT NOT NULL," +
                    HabitTrackingEntry.COLUMN_HABITTRACKING_CODE + " INTEGER NOT NULL," +
                    HabitTrackingEntry.COLUMN_HABITTRACKING_CREATEDON + " DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(" + HabitTrackingEntry.COLUMN_HABITTRACKING_CODE + ") REFERENCES " + HabitEntry.TABLE_NAME + "(" + HabitEntry.COLUMN_HABIT_CODE + ")," +
                    "PRIMARY KEY(" + HabitTrackingEntry.COLUMN_HABITTRACKING_USERID + "," +  HabitTrackingEntry.COLUMN_HABITTRACKING_CODE  + ")" +
                    " )";

    protected static final String SQL_DELETE_HABIT_TRACKING_ENTRIES =
            "DROP TABLE IF EXISTS " + HabitTrackingEntry.TABLE_NAME;
}

