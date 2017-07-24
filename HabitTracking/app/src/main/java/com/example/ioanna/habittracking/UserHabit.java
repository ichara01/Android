package com.example.ioanna.habittracking;

/**
 * Created by Ioanna on 24/07/2017.
 */

public class UserHabit {
    private Integer mCode;
    private String mUserID;

    public UserHabit(Integer code, String userID) {
        mCode = code;
        mUserID = userID;
    }

    public Integer GetUserHabitCode() {
        return mCode;
    }

    public String GetUserID() {
        return mUserID;
    }
}
