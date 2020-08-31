package com.example.quakedata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.Preference;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.quakedata.utils.Query;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private ListView listView;
    private CustomAdapter adapter;
    private TextView emptyTextView;
    private ProgressBar progressBar;

    /** URL USGS dataset **/
    //private String USGS_DATA = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=50";

    private String USGS_DATA;

    private static final int EARTHQUAKE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** URL USGS dataset **/
        USGS_DATA = Preferences.getPreferredUrl(this);

        /** Create empty list **/
        listView = (ListView)findViewById(R.id.list);
        emptyTextView = (TextView)findViewById(R.id.empty);
        listView.setEmptyView(emptyTextView);

        adapter = new CustomAdapter(MainActivity.this, R.layout.list_item);
        adapter.addAll(new ArrayList<Earthquake>());


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = Uri.parse(adapter.getItem(i).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        /** Check internet available **/
        if (isInternetAvailable()) {
            /** Using Asynctask **/
            // QuakeAsyncTask task = new QuakeAsyncTask();
            // task.execute(USGS_DATA);

            /** Init loader **/
            Log.d("initLoader", "initLoader");
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, new MyLoaderCallbacks());
        }
        else {
            progressBar = (ProgressBar)findViewById(R.id.loading);
            progressBar.setVisibility(View.GONE);

            emptyTextView.setText("No Internet connection.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu_item){
            emptyTextView.setVisibility(View.GONE);
            progressBar = (ProgressBar)findViewById(R.id.loading);
            progressBar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, new MyLoaderCallbacks());
            return true;
        }

        if (item.getItemId() == R.id.settings_menu_item) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    private class MyLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
        @NonNull
        @Override
        public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
            Log.d("onCreateLoader", "onCreateLoader");
            return new EarthquakeLoader(MainActivity.this, USGS_DATA);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {
            Log.d("onLoadFinished", "onLoadFinished");

            progressBar = (ProgressBar)findViewById(R.id.loading);
            progressBar.setVisibility(View.GONE);

            if (data != null && !data.isEmpty()) {
                updateUi(data);
            }
            else {
                emptyTextView.setText("No earthquakes found.");
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
            Log.d("onLoaderReset", "onLoaderReset");
            adapter.clear();
        }
    }

    private class QuakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... strings) {
            return Query.fetchEarthquake(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            Log.d("Response", earthquakes+"");

            if (earthquakes.isEmpty()) {
                return;
            }
            updateUi(earthquakes);
        }
    }

    private void updateUi(final List<Earthquake> earthquakes) {
        adapter = new CustomAdapter(MainActivity.this, R.layout.list_item);
        adapter.addAll(earthquakes);

        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = Uri.parse(adapter.getItem(i).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}