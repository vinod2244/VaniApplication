package com.shukla.tech.hospitalapplication.locationlib;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by mrm on 4/1/15.
 */
public interface OnActivityUpdatedListener {
    void onActivityUpdated(DetectedActivity detectedActivity);
}