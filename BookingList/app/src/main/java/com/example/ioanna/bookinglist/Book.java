package com.example.ioanna.bookinglist;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import static android.content.ContentValues.TAG;

/**
 * Created by Ioanna on 09/07/2017.
 */

public class Book implements Parcelable {
    private String mTitle;
    private String mSubtitle;
    private String mAuthor;
    private String mImageURL;

    Book(String title, String subtitle, String author, String imageURL) {
        mTitle = title;
        mSubtitle = subtitle;
        mAuthor = author;
        mImageURL = imageURL;
    }

    Book(Parcel in) {
        mTitle = in.readString();
        mSubtitle = in.readString();
        mAuthor = in.readString();
        mImageURL = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubtitle() { return mSubtitle; }

    public String getAuthor() { return mAuthor; }

    public String getImageURL() { return mImageURL; }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v(TAG, "Book - writeToParcel..."+ flags);
        dest.writeString(mTitle);
        dest.writeString(mSubtitle);
        dest.writeString(mAuthor);
        dest.writeString(mImageURL);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public String toString() {
        String s = "Title: " + mTitle + ", " + "Subtitle: " + mSubtitle + ", "  + ", Author:" + mAuthor + ", Image Url:" + mImageURL + "\n";
        return s;
    }
}
