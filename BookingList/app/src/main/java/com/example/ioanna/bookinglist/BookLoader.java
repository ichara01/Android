package com.example.ioanna.bookinglist;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.ioanna.bookinglist.MainActivity.LOG_TAG;
import static com.example.ioanna.bookinglist.Utitlities.REQUEST_URL_CUSTOM;
import static com.example.ioanna.bookinglist.Utitlities.createUrl;
import static com.example.ioanna.bookinglist.Utitlities.extractBooksDetailsFromJson;
import static com.example.ioanna.bookinglist.Utitlities.makeHttpRequest;

/**
 * Created by Ioanna on 15/07/2017.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<Book>> {

    public BookLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        // Don't perform the request if the URL is null.
        if (REQUEST_URL_CUSTOM.isEmpty() || REQUEST_URL_CUSTOM == null) {
            return null;
        }
        // Create URL object
        URL url = createUrl(REQUEST_URL_CUSTOM);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create an {@link ArrayList<Book>} object
        ArrayList<Book> books = extractBooksDetailsFromJson(jsonResponse);
        // Return the {@link ArrayList<Book>} object as the result fo the {@link BookingAsyncTask}
        return books;
    }
}
