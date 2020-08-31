package com.example.quakedata.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.quakedata.Earthquake;
import com.example.quakedata.MainActivity;

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
import java.util.List;

public class Query {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Query() {

    }

    public static List<Earthquake> fetchEarthquake(String requestUrl) {
        Log.d("fetchEarthquake", "fetchEarthquake");
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            // TODO Handle the IOException
        }

        return extractEarthquakes(jsonResponse);
    }

    private static List<Earthquake> extractEarthquakes(String JSON) {
        List<Earthquake> earthquakes = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(JSON);
            JSONArray features = reader.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = (JSONObject)features.get(i);
                JSONObject properties = (JSONObject)feature.getJSONObject("properties");



                /** Check whether fields contain NaN or Null, if yes then skip to next object. */
                if (Double.isNaN(properties.optDouble("mag"))) {
                    continue;
                }

                if (properties.optString("place").isEmpty()) {
                    continue;
                }

                if (Double.isNaN(properties.getDouble("time"))) {
                    continue;
                }

                if (properties.optString("url").isEmpty()) {
                    continue;
                }


                earthquakes.add
                        (
                                new Earthquake (
                                        properties.getDouble("mag"),
                                        properties.getString("place"),
                                        properties.getLong("time"),
                                        properties.getString("url")
                                )
                        );
            }
        }catch (JSONException e) {
            Log.e("JSON", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
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

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.d("HTTP Request", "HTTP Request");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

}
