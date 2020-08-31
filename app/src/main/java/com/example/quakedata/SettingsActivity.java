package com.example.quakedata;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener, DatePickerDialog.OnDateSetListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // Find the preference for number of items
            Preference numOfItems = findPreference(getString(R.string.settings_number_of_items_key));
            // bind the current preference value to be displayed
            bindPreferenceSummaryToValue(numOfItems);

            // Find the "order by" Preference object according to its key
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            // Update the summary so that it displays the current value stored in SharedPreferences
            bindPreferenceSummaryToValue(orderBy);


            // Find the "start date" Preference object according to its key
            Preference startTime = findPreference(getString(R.string.settings_start_time_key));
            setOnPreferenceClick(startTime);
            // bind the current preference value to be displayed
            bindPreferenceSummaryToValue(startTime);

        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            // Since it starts counting the months from 0, add one to the month value.
            month = month + 1;
            // Date the user selected
            String selectedDate = year + "-" + month + "-" + dayOfMonth;
            // Convert selected date string(i.e. "2017-2-1" into formatted date string(i.e. "2017-02-01")
            String formattedDate = formatDate(selectedDate);

            // Storing selected date
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.settings_start_time_key), formattedDate).apply();

            // Update the displayed preference summary after it has been changed
            Preference fromDatePreference = findPreference(getString(R.string.settings_start_time_key));
            bindPreferenceSummaryToValue(fromDatePreference);
        }

        /**
         * Convert selected date string(i.e. "2017-2-1" into formatted date string(i.e. "2017-02-01")
         *
         * @param dateString is the selected date from the DatePicker
         * @return the formatted date string
         */

        private String formatDate(String dateString) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
            Date dateObject = null;
            try {
                dateObject = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
            return df.format(dateObject);
        }

        /**
         * This method is called when the user has clicked a Preference.
         */
        private void setOnPreferenceClick(Preference preference) {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    if (key.equalsIgnoreCase(getString(R.string.settings_start_time_key))) {
                        showDatePicker();
                    }
                    return false;
                }
            });
        }


        /**
         * Show the current date as the default date in the picker
         */
        private void showDatePicker() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(getActivity(),
                    this, year, month, dayOfMonth).show();
        }


        /**
         * This method is called when the user has changed a Preference.
         * Update the displayed preference summary (the UI) after it has been changed.
         * @param preference the changed Preference
         * @param newValue the new value of the Preference
         * @return True to update the state of the Preference with the new value
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            Log.e("onPreferenceChange", stringValue);
            // Update the summary of a ListPreference using the label
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        /**
         * Set this fragment as the OnPreferenceChangeListener and
         * bind the value that is in SharedPreferences to what will show up in the preference summary
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the current NewsPreferenceFragment instance to listen for changes to the preference
            // we pass in using
            preference.setOnPreferenceChangeListener(this);

            // Read the current value of the preference stored in the SharedPreferences on the device,
            // and display that in the preference summary
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            Log.e("bindPreferenceSummary", preferenceString);
            onPreferenceChange(preference, preferenceString);
        }

    }
}