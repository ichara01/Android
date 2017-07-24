package com.example.ioanna.habittracking;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HabitTrackingDbHelper mDbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new HabitTrackingDbHelper(this);
        //Insert Habit Dummy Data
        InsertHabitDummyData();
        //Insert Users' Habits Dummy Data
        InserUsersHabitDummyData();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitTrackingContract.HabitTrackingEntry.COLUMN_HABITTRACKING_CODE,
                HabitTrackingContract.HabitTrackingEntry.COLUMN_HABITTRACKING_USERID
        };

        Cursor cursor = db.query(
                HabitTrackingContract.HabitTrackingEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.txtHabit);
            displayView.setText("Number of tracking habits in database table: " + cursor.getCount());
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    // Dummy Data - Get Users Habits
    private List<UserHabit> GetUsersHabits() {
        List<UserHabit> habits = new ArrayList<UserHabit>();
        habits.add(new UserHabit(1, "joanna01"));
        habits.add(new UserHabit(2, "joanna01"));
        habits.add(new UserHabit(2, "maria01"));
        habits.add(new UserHabit(2, "maria02"));
        habits.add(new UserHabit(5, "helena15"));
        habits.add(new UserHabit(6, "nick01"));
        habits.add(new UserHabit(7, "steph12"));
        habits.add(new UserHabit(8, "nick02"));
        habits.add(new UserHabit(9, "ioanna01"));
        return habits;
    }

    // Dummy Data - Insert Users Habit into the database
    private void InsertUserHabits(List<UserHabit> habits) {
        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            UserHabit habit = null;
            for (int i = 0; i < habits.size(); ++i) {
                habit = habits.get(i);
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(HabitTrackingContract.HabitTrackingEntry.COLUMN_HABITTRACKING_CODE, habit.GetUserHabitCode());
                values.put(HabitTrackingContract.HabitTrackingEntry.COLUMN_HABITTRACKING_USERID, habit.GetUserID());
                // Insert the new row
                db.insert(HabitTrackingContract.HabitTrackingEntry.TABLE_NAME, null, values);
                Log.i("InsertUsersHabits", "User " + habit.GetUserID() + " with habit code " + habit.GetUserHabitCode().toString() + " has been added.");
            }
        } catch (Exception ex) {
            Log.e("InsertUsersHabits", "User's habit cannot be added");
        }
    }

    // Dummy Data - If no Users' Habits exist, then insert them to database
    private void InserUsersHabitDummyData() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitTrackingContract.HabitTrackingEntry.COLUMN_HABITTRACKING_CODE,
                HabitTrackingContract.HabitTrackingEntry.COLUMN_HABITTRACKING_USERID
        };

        Cursor cursor = db.query(
                HabitTrackingContract.HabitTrackingEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        try {
            if (cursor.getCount() == 0) {
                InsertUserHabits(GetUsersHabits());
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    // Dummy Data - Get Habits
    private List<Habit> GetHabits() {
        List<Habit> habits = new ArrayList<Habit>();
        habits.add(new Habit(1, "Swimming"));
        habits.add(new Habit(2, "Tennis"));
        habits.add(new Habit(3, "Gym"));
        habits.add(new Habit(4, "Listen music"));
        habits.add(new Habit(5, "Walking"));
        habits.add(new Habit(6, "Reading"));
        habits.add(new Habit(7, "Watching TV"));
        habits.add(new Habit(8, "Cooking"));
        habits.add(new Habit(9, "Yoga"));
        return habits;
    }

    // Dummy Data - Insert new Habit into the database
    private void InsertHabits(List<Habit> habits) {
        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Habit habit = null;
            for (int i = 0; i < habits.size(); ++i) {
                habit = habits.get(i);
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(HabitTrackingContract.HabitEntry.COLUMN_HABIT_CODE, habit.GetHabitCode());
                values.put(HabitTrackingContract.HabitEntry.COLUMN_HABIT_NAME, habit.GetHabitName());
                // Insert the new row
                db.insert(HabitTrackingContract.HabitEntry.TABLE_NAME, null, values);
                Log.i("InsertHabits", "Habit " + habit.GetHabitName() + " with code " + habit.GetHabitCode().toString() + " has been added.");
            }
        } catch (Exception ex) {
            Log.e("InsertHabits", "Habit cannot be added");
        }
    }

    // Dummy Data - If no Habits exist, then insert habits to database
    private void InsertHabitDummyData() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HabitTrackingContract.HabitEntry.COLUMN_HABIT_CODE
        };

        Cursor cursor = db.query(
                HabitTrackingContract.HabitEntry.TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );
        
        try {
            if (cursor.getCount() == 0) {
                InsertHabits(GetHabits());
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
