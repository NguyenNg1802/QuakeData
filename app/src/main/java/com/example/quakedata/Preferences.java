package com.example.quakedata;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.preference.PreferenceManager;

import com.example.quakedata.utils.Constants;


public class Preferences {
    /**
     * Get Uri.Builder based on stored SharedPreferences.
     * @param context Context used to access SharedPreferences
     * @return Uri.Builder
     */
    public static Uri.Builder getPreferredUri(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        // getString retrieves a String value from the preferences. The second parameter is the
        // default value for this preference.
        String numOfItems = sharedPrefs.getString(
                context.getString(R.string.settings_number_of_items_key),
                context.getString(R.string.settings_number_of_items_default));


        // Get the information from SharedPreferences and check for the value associated with the key
        String orderBy = sharedPrefs.getString(
                context.getString(R.string.settings_order_by_key),
                context.getString(R.string.settings_order_by_default));


        // Get the start time information from SharedPreferences and check for the value associated with the key
        String startTime = sharedPrefs.getString(
                context.getString(R.string.settings_start_time_key),
                context.getString(R.string.settings_start_time_default));


        Uri baseUri = Uri.parse(Constants.EARTHQUAKE_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(Constants.LIMIT_PARAM, numOfItems);
        uriBuilder.appendQueryParameter(Constants.FORMAT_PARAM, Constants.FORMAT);
        uriBuilder.appendQueryParameter(Constants.ORDER_BY_PARAM, orderBy);
        uriBuilder.appendQueryParameter(Constants.START_TIME_PARAM, startTime);
        return uriBuilder;
    }

    /**
     * Returns String Url for query
     * @param context Context used to access getPreferredUri method
     */
    public static String getPreferredUrl(Context context) {
        Uri.Builder uriBuilder = getPreferredUri(context);
        return uriBuilder.toString();
    }
}
