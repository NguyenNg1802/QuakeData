package com.example.quakedata.utils;

public class Constants {

    private Constants() {}

    /** Read timeout for setting up the HTTP request */
    static final int READ_TIMEOUT = 10000; /* milliseconds */

    /** Connect timeout for setting up the HTTP request */
    static final int CONNECT_TIMEOUT = 15000; /* milliseconds */

    /** HTTP response code when the request is successful */
    static final int SUCCESS_RESPONSE_CODE = 200;

    /** Request method type "GET" for reading information from the server */
    static final String REQUEST_METHOD_GET = "GET";

    /** URL for data from the USGS */
    public static final String EARTHQUAKE_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";

    /** Parameter */
    public static final String END_TIME_PARAM = "endtime";
    public static final String START_TIME_PARAM = "starttime";
    public static final String LIMIT_PARAM = "limit";
    public static final String ORDER_BY_PARAM = "orderby";
    public static final String FORMAT_PARAM = "format";
    /** Parameter */
    public static final String FORMAT = "geojson";


}
