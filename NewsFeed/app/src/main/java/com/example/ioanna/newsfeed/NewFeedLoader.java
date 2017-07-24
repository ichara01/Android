package com.example.ioanna.newsfeed;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.ioanna.newsfeed.MainActivity.LOG_TAG;
import static com.example.ioanna.newsfeed.MainActivity.searchParam;
import static com.example.ioanna.newsfeed.Utitlities.API_KEY;
import static com.example.ioanna.newsfeed.Utitlities.REQUEST_URL_CUSTOM;
import static com.example.ioanna.newsfeed.Utitlities.SEARCH;
import static com.example.ioanna.newsfeed.Utitlities.createUrl;
import static com.example.ioanna.newsfeed.Utitlities.extractNewFeedsDetailsFromJson;
import static com.example.ioanna.newsfeed.Utitlities.makeHttpRequest;

/**
 * Created by Ioanna on 15/07/2017.
 */

public class NewFeedLoader extends AsyncTaskLoader<ArrayList<NewFeed>> {

    public NewFeedLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "Start onStartLoading()");
        forceLoad();
        Log.i(LOG_TAG, "Finish onStartLoading()");
    }

    @Override
    public ArrayList<NewFeed> loadInBackground() {
        Log.i(LOG_TAG, "Start loadInBackground()");
        REQUEST_URL_CUSTOM = REQUEST_URL_CUSTOM + SEARCH + searchParam + API_KEY;
        // Don't perform the request if the URL is null.
        if (REQUEST_URL_CUSTOM.isEmpty() || REQUEST_URL_CUSTOM == null) {
            return null;
        }
        // Create URL object
        URL url = createUrl(REQUEST_URL_CUSTOM);
        Log.i(LOG_TAG, "loadInBackground(): URL: " + REQUEST_URL_CUSTOM);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create an {@link ArrayList<NewFeed>} object
        ArrayList<NewFeed> news = extractNewFeedsDetailsFromJson(jsonResponse);
        Log.i(LOG_TAG, "loadInBackground(): Returns: " + news.size() + " items");
        // Return the {@link ArrayList<NewFeed>} object as the result fo the {@link NewFeedingAsyncTask}
        Log.i(LOG_TAG, "Finish loadInBackground()");
        return news;
    }
}
