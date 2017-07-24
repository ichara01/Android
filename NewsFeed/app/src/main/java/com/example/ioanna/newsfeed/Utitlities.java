package com.example.ioanna.newsfeed;

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

import static com.example.ioanna.newsfeed.MainActivity.LOG_TAG;

/**
 * Created by Ioanna on 15/07/2017.
 */

public class Utitlities {

    /**
     * URL to query the Guardian dataset for content information
     */
    protected static final String REQUEST_URL = "https://content.guardianapis.com/";
    protected static final String API_KEY = "&api-key=25623a35-f33a-4b5d-8557-f100f26d62e7";
    protected static final String SEARCH = "search?";
    public static String REQUEST_URL_CUSTOM = REQUEST_URL;

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        Log.i(LOG_TAG, "Start createUrl()");
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
        Log.i(LOG_TAG, "Start makeHttpRequest()");
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
            Log.e(LOG_TAG, "Problem retrieving the content JSON results.", e);
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
        Log.i(LOG_TAG, "Finish makeHttpRequest()");
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
     * Return an {@link ArrayList<NewFeed>} object by parsing out information
     * about the content
     */
    public static ArrayList<NewFeed> extractNewFeedsDetailsFromJson(String contentJSON) {
        if (TextUtils.isEmpty(contentJSON)) {
            return null;
        }
        // Create a new {@link ArrayList<NewFeed>} object
        ArrayList<NewFeed> news = new ArrayList<NewFeed>();
        // If the JSON string is empty or null, then return early.
        try {
            JSONObject baseJsonResponse = new JSONObject(contentJSON);
            if (!baseJsonResponse.has("response"))
                return null;
            JSONObject responsesObject = baseJsonResponse.getJSONObject("response");
            if (!responsesObject.has("results"))
                return null;
            JSONArray resultArray;
            JSONObject newDtls;
            String title, section, publicationDate, webUrl;
            resultArray = responsesObject.getJSONArray("results");
            // If there are results in the array
            for (int i = 0; i < resultArray.length(); ++i) {
                try {
                    // Extract out content details
                    newDtls = resultArray.getJSONObject(i);
                    // Extract out the title
                    title = newDtls.has("webTitle") ? newDtls.getString("webTitle") : "";
                    // Extract out the section
                    section = newDtls.has("sectionName") ? newDtls.getString("sectionName") : "";
                    // Extract out the publication Date
                    publicationDate = newDtls.has("webPublicationDate") ? newDtls.getString("webPublicationDate") : "";
                    // Extract out the webURL
                    webUrl = newDtls.has("webUrl") ? newDtls.getString("webUrl") : "";
                    // Add the new {@link NewFeed} to the list
                    news.add(new NewFeed(title, section, publicationDate, webUrl));
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Problem parsing the result details", e);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the content JSON results", e);
        }
        Log.i(LOG_TAG, "extractNewFeedsDetailsFromJson(): Extract JSON data");
        // Return the list of content
        return news;
    }
}
