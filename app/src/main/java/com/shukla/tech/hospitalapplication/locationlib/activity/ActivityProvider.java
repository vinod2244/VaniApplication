package com.shukla.tech.hospitalapplication.locationlib.activity;

import android.content.Context;

import com.google.android.gms.location.DetectedActivity;

import com.shukla.tech.hospitalapplication.locationlib.OnActivityUpdatedListener;
import com.shukla.tech.hospitalapplication.locationlib.activity.config.ActivityParams;
import com.shukla.tech.hospitalapplication.locationlib.utils.Logger;

/**
 * Created by mrm on 3/1/15.
 */
public interface ActivityProvider {
    void init(Context context, Logger logger);

    void start(OnActivityUpdatedListener listener, ActivityParams params);

    void stop();

    DetectedActivity getLastActivity();
}
