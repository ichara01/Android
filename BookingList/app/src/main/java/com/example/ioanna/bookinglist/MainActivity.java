package com.example.ioanna.bookinglist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.ioanna.bookinglist.Utitlities.REQUEST_URL;
import static com.example.ioanna.bookinglist.Utitlities.REQUEST_URL_CUSTOM;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>>{
    /** Tag for the log messages **/
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    /**Loader ID**/
    private static final int BOOK_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private CustomArrayAdapter adapter;
    private ProgressBar loadingIndicator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // This is the Adapter being used to display the list's data.
        adapter = new CustomArrayAdapter(this, new ArrayList<Book>());
        // Empty State Text
        mEmptyStateTextView = (TextView)findViewById(R.id.empty_view);
        //Progress Bar
        loadingIndicator = (ProgressBar)findViewById(R.id.loading_indicator);
        // Show loading indicator
        loadingIndicator.showContextMenu();
        loadingIndicator.setVisibility(View.VISIBLE);
        // Check Network connectivity
        if(HasNetworkConnecticity()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            getSupportLoaderManager().initLoader(BOOK_LOADER_ID, null, this);
        }
    }

    /**
     * On click of search button
     * @param view
     */
    public void RetrieveBooks(View view){
        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);
        loadingIndicator.showContextMenu();
        TextView txtSearch = (TextView)findViewById(R.id.txt_search);
        String searchParam = txtSearch.getText().toString();
        // Hide soft keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        // Check Network connectivity
        if(HasNetworkConnecticity()) {
            // Called when the action bar search text has changed.  Update
            // the search filter, and restart the loader to do a new query
            // with this filter.
            REQUEST_URL_CUSTOM = REQUEST_URL_CUSTOM + searchParam + "&maxResults=20";
            getSupportLoaderManager().restartLoader(BOOK_LOADER_ID, null, this);
        }
    }
    /**
     * Check Network connectivity
     * if there is a network connectivity, it return true,
     * else it returns false
     */
    private boolean HasNetworkConnecticity(){
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
     * Update the screen to display information from the given {@link Book}.
     */
    private void UpdateUI(ArrayList<Book> books) {
        //Clear adapter
         adapter.clear();
        // Adapter creates list item view for each item
        adapter = new CustomArrayAdapter(this, books);
        ListView list = (ListView) this.findViewById(R.id.list_view);
        // Attach the adapter to the listView.
        list.setAdapter(adapter);
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);
        //Reset URL
        REQUEST_URL_CUSTOM = REQUEST_URL;
        // Clear the adapter of previous earthquake data
         adapter.clear();
        // If there is no result, do nothing.
        if (data == null  || data.isEmpty()) {
            // Set empty state text to display "No books found."
            mEmptyStateTextView.setText(R.string.no_books);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            return;
        }
        mEmptyStateTextView.setVisibility(View.GONE);
        UpdateUI(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        UpdateUI(null);
    }
}
