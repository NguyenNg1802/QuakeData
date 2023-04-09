package com.example.quakedata;

public class Earthquake {
    private double mag;
    private String place;
    private long unix_time;
    private String url;
    private int ver;
    private int ver2;
    private int ver;
    private int ver2;

    public Earthquake (double mag, String place, long unix_time, String url) {
        this.mag = mag;
        this.place = place;
        this.unix_time = unix_time;
        this.url = url;
    }

    public double getMag() {
        return mag;
    }

    public void setMag (double mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getUnix_time() {
        return unix_time;
    }

    public void setUnix_time(long unix_time) {
        this.unix_time = unix_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }
    
    public void setVer2(int ver2) {
        this.ver2 = ver2;
    }

    public int getVer2() {
        return ver2;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }
    
    public void setVer2(int ver2) {
        this.ver2 = ver2;
    }

    public int getVer2() {
        return ver2;
    }
}
