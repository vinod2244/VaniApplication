package com.shukla.tech.hospitalapplication.common;

/**
 * Created by admin on 10/6/2017.
 */

public class Constants {
    public static boolean APK_MODE_RELEASE = false;

    public static String APP_TAG = "Ceva";

    public static String SERVER_URL = "";
    public static String API_CONFIG = "api-config";
    public static String API_LOGIN = "/userlogin";
    public static String USER_LOGIN_DETAILS = "/userlogindetials";


    public static final String DEFAULT_FOLDER = "ceva";
    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final String NETWORK_STATUS ="Good! Connected to Internet";
    public static final String CROWN_IMAGE_DIRECTORY_NAME = "Crowns Submitted Pics";
    public static final String GIFT_IMAGE_DIRECTORY_NAME = "Gift Submitted Pics";

    public static  boolean isConnected;
}
