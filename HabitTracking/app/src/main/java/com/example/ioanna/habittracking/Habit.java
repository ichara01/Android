package com.example.ioanna.habittracking;

/**
 * Created by Ioanna on 24/07/2017.
 */

public class Habit {
    private Integer mCode;
    private String mName;

    public Habit(Integer code, String name) {
        mCode = code;
        mName = name;
    }

    public Integer GetHabitCode() {
        return mCode;
    }

    public String GetHabitName() {
        return mName;
    }
}
