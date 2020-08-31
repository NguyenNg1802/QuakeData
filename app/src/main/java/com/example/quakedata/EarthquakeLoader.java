package com.example.quakedata;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;


import com.example.quakedata.utils.Query;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String url;

    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d("onStartLoading", "onStartLoading");
        forceLoad();
    }

    @Nullable
    @Override

    /**
     * This is on a background thread.
     */
    public List<Earthquake> loadInBackground() {
        Log.d("loadInBackground", this.url);
        if (this.url == null) {
            return null;
        }
        return Query.fetchEarthquake(this.url);
    }
}
