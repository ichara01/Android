package com.example.ioanna.newsfeed;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.ioanna.newsfeed.Utitlities.REQUEST_URL;
import static com.example.ioanna.newsfeed.Utitlities.REQUEST_URL_CUSTOM;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewFeed>> {
    //Tag for the log messages
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    //Search Param
    protected static String searchParam = "";
    //Loader ID
    private static final int LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private CustomArrayAdapter adapter;
    private ProgressBar loadingIndicator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Empty State Text
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        //Progress Bar
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        // Show loading indicator
        loadingIndicator.showContextMenu();
        loadingIndicator.setVisibility(View.VISIBLE);
        // This is the Adapter being used to display the list's data.
        adapter = new CustomArrayAdapter(this, new ArrayList<NewFeed>());
        //REQUEST_URL_CUSTOM = REQUEST_URL_CUSTOM + SEARCH + API_KEY;
        // Check Network connectivity
        if (HasNetworkConnecticity()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    /**
     * On click of search button retrieve data
     *
     * @param view
     */
    public void RetrieveContent(View view) {
        //Reset URL
        REQUEST_URL_CUSTOM = REQUEST_URL;
        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);
        loadingIndicator.showContextMenu();
        // Search param
        TextView txtSearch = (TextView) findViewById(R.id.txt_search);
        searchParam = txtSearch.getText().toString();
        // Hide soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Log.i(LOG_TAG, "Search by " + searchParam);
        // Check Network connectivity
        if (HasNetworkConnecticity()) {
            // Called when the action bar search text has changed.  Update
            // the search filter, and restart the loader to do a new query
            // with this filter.
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    /**
     * Check Network connectivity
     * if there is a network connectivity, it return true,
     * else it returns false
     */
    private boolean HasNetworkConnecticity() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            return false;
        }
    }

    /**
     * Update the screen to display information from the given {@link NewFeed}.
     */
    private void UpdateUI(final ArrayList<NewFeed> contents) {
        Log.i(LOG_TAG, "UpdateUI()");
        //Clear adapter
        adapter.clear();
        // Adapter creates list item view for each item
        adapter = new CustomArrayAdapter(this, contents);
        ListView list = (ListView) this.findViewById(R.id.list_view);
        // Attach the adapter to the listView.
        list.setAdapter(adapter);
        list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    NewFeed selectedItem = contents.get(position);
                    String webURL = selectedItem.getWebURL();
                    //Check if selected data has a webURL
                    if (webURL.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Story URL is not available", Toast.LENGTH_SHORT);
                    } else {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webURL));
                        startActivity(myIntent);
                    }
                } catch (ActivityNotFoundException e) {
                    Log.e(LOG_TAG, "Cannot open web URL");
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<NewFeed>> onCreateLoader(int id, Bundle args) {
        return new NewFeedLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewFeed>> loader, ArrayList<NewFeed> data) {
        Log.i(LOG_TAG, "Start onLoadFinished()");
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);
        //Reset URL
        REQUEST_URL_CUSTOM = REQUEST_URL;
        // Clear the adapter of previous earthquake data
        adapter.clear();
        // If there is no result, do nothing.
        if (data == null || data.isEmpty()) {
            // Set empty state text to display "No content found."
            mEmptyStateTextView.setText(R.string.no_content);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            return;
        }
        mEmptyStateTextView.setVisibility(View.GONE);
        UpdateUI(data);
        Log.i(LOG_TAG, "Finish onLoadFinished()");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewFeed>> loader) {
        Log.i(LOG_TAG, "onLoaderReset()");
        UpdateUI(null);
    }
}
