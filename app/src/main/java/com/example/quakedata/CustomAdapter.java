package com.example.quakedata;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Earthquake> {
    private List<Earthquake> earthquakes= new ArrayList<>();
    private Earthquake earthquake;

    public CustomAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Earthquake> collection) {
        super.addAll(collection);
        this.earthquakes.addAll(collection);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        earthquake = earthquakes.get(position);

        Date d = new java.util.Date(earthquake.getUnix_time());



        TextView mag = (TextView)convertView.findViewById(R.id.mag);
        mag.setText(formatMagnitude(earthquake.getMag()));

        GradientDrawable mag_circle = (GradientDrawable)mag.getBackground();
        mag_circle.setColor(getMagColor(earthquake.getMag()));

        TextView dis = (TextView)convertView.findViewById(R.id.dis);
        dis.setText(getDistance(earthquake.getPlace()));

        TextView loc = (TextView)convertView.findViewById(R.id.loc);
        loc.setText(getLocation(earthquake.getPlace()));

        TextView date = (TextView)convertView.findViewById(R.id.date);
        date.setText(toDate(d));

        TextView time = (TextView)convertView.findViewById(R.id.time);
        time.setText(toTime(d));

        return convertView;
    }

    private String toDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    private  String toTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    private String getDistance(String place) {
        if (place.contains("of") == true) {
            return place.substring(0, place.indexOf("of") + 2);
        }
        else {
            return "Near the";
        }
    }

    private String getLocation(String place) {
        if (place.contains("of") == true) {
            return place.substring(place.indexOf("of") + 3);
        }
        else {
            return place;
        }
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagColor(double mag) {
        int color;
        switch ((int)Math.floor(mag)) {
            case 0:
            case 1:
                color = R.color.magnitude1;
                break;
            case 2:
                color = R.color.magnitude2;
                break;
            case 3:
                color = R.color.magnitude3;
                break;
            case 4:
                color = R.color.magnitude4;
                break;
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            default:
                color = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), color);
    }
}
