package com.example.ioanna.bookinglist;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.ioanna.bookinglist.MainActivity.LOG_TAG;

/**
 * Created by Ioanna on 15/07/2017.
 */

public class Utitlities {

    /**
     * URL to query the Google dataset for booking information
     */
    protected static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    public static String REQUEST_URL_CUSTOM = REQUEST_URL;

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Problem building the URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the booking JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Closing the input stream could throw an IOException, which is why
            // the makeHttpRequest(URL url) method signature specifies than an IOException
            // could be thrown.
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Book} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    public static ArrayList<Book> extractBooksDetailsFromJson(String bookingsJSON) {
        if (TextUtils.isEmpty(bookingsJSON)) {
            return null;
        }
        // Create a new {@link ArrayList<Book>} object
        ArrayList<Book> books = new ArrayList<Book>();
        // If the JSON string is empty or null, then return early.
        try {
            JSONObject baseJsonResponse = new JSONObject(bookingsJSON);
            if (!baseJsonResponse.has("items"))
                return null;
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");
            JSONObject bookDtls, bookImages;
            String title, subtitle, authors, imageURL;
            // If there are results in the books array
            for (int i = 0; i < booksArray.length(); ++i) {
                try {
                    // Extract out book details
                    bookDtls = booksArray.getJSONObject(i);
                    if (!bookDtls.has("volumeInfo"))
                        continue;
                    JSONObject volumeInfo = bookDtls.getJSONObject("volumeInfo");
                    // Extract out the title
                    title = volumeInfo.has("title") ? volumeInfo.getString("title") : "";
                    // Extract out the subtitle
                    subtitle = volumeInfo.has("subtitle") ? volumeInfo.getString("subtitle") : "";
                    // Extract out the authors
                    authors = volumeInfo.has("authors") ? (volumeInfo.getJSONArray("authors").toString()).replaceAll("\"", "").replaceAll("]", "").replaceAll("\\[", "") : "";
                    // Extract out the book image
                    bookImages = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks") : null;
                    imageURL = bookImages != null ? bookImages.getString("smallThumbnail") : null;
                    // Add the new {@link Book} to the list of books.
                    books.add(new Book(title, subtitle, authors, imageURL));
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Problem parsing the book details", e);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the booking JSON results", e);
        }
        // Return the list of books
        return books;
    }
}
