package com.example.ioanna.newsfeed;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Ioanna on 15/07/2017.
 */

public class NewFeed implements Parcelable {
    private String mTitle;
    private String mSection;
    private String mpPublicatioDate;
    private String mWebURL;

    NewFeed(String title, String section, String date, String webURL) {
        mTitle = title;
        mSection = section;
        mpPublicatioDate = date;
        mWebURL = webURL;
    }

    NewFeed(Parcel in) {
        mTitle = in.readString();
        mSection = in.readString();
        mpPublicatioDate = in.readString();
        mWebURL = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mpPublicatioDate;
    }

    public String getWebURL() {
        return mWebURL;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v(TAG, "Book - writeToParcel..." + flags);
        dest.writeString(mTitle);
        dest.writeString(mSection);
        dest.writeString(mpPublicatioDate);
        dest.writeString(mWebURL);
    }

    public static final Creator<NewFeed> CREATOR = new Creator<NewFeed>() {
        @Override
        public NewFeed createFromParcel(Parcel in) {
            return new NewFeed(in);
        }

        @Override
        public NewFeed[] newArray(int size) {
            return new NewFeed[size];
        }
    };

    @Override
    public String toString() {
        String s = "Title: " + mTitle + ", " + "Section: " + mSection + ", Date:" + mpPublicatioDate + ", Web URL: " + mWebURL + "\n";
        return s;
    }
}
