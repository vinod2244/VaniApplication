package com.shukla.tech.hospitalapplication.locationlib.location.providers;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.DetectedActivity;

import com.shukla.tech.hospitalapplication.locationlib.OnActivityUpdatedListener;
import com.shukla.tech.hospitalapplication.locationlib.OnLocationUpdatedListener;
import com.shukla.tech.hospitalapplication.locationlib.activity.config.ActivityParams;
import com.shukla.tech.hospitalapplication.locationlib.activity.providers.ActivityGooglePlayServicesProvider;
import com.shukla.tech.hospitalapplication.locationlib.location.LocationProvider;
import com.shukla.tech.hospitalapplication.locationlib.location.config.LocationParams;
import com.shukla.tech.hospitalapplication.locationlib.utils.Logger;

/**
 * Created by mrm on 20/12/14.
 */
public class LocationBasedOnActivityProvider implements LocationProvider, OnActivityUpdatedListener {
    private final ActivityGooglePlayServicesProvider activityProvider;
    private final LocationGooglePlayServicesProvider locationProvider;
    private final LocationBasedOnActivityListener locationBasedOnActivityListener;
    private OnLocationUpdatedListener locationUpdatedListener;
    private LocationParams locationParams;

    public LocationBasedOnActivityProvider(@NonNull LocationBasedOnActivityListener locationBasedOnActivityListener) {
        activityProvider = new ActivityGooglePlayServicesProvider();
        locationProvider = new LocationGooglePlayServicesProvider();
        this.locationBasedOnActivityListener = locationBasedOnActivityListener;
    }

    @Override
    public void init(Context context, Logger logger) {
        locationProvider.init(context, logger);
        activityProvider.init(context, logger);
    }

    @Override
    public void start(OnLocationUpdatedListener listener, LocationParams params, boolean singleUpdate) {
        if (singleUpdate) {
            throw new IllegalArgumentException("singleUpdate cannot be set to true");
        }
        locationProvider.start(listener, params, false);
        activityProvider.start(this, ActivityParams.NORMAL);
        this.locationParams = params;
        this.locationUpdatedListener = listener;
    }

    @Override
    public void stop() {
        locationProvider.stop();
        activityProvider.stop();
    }

    @Override
    public Location getLastLocation() {
        return locationProvider.getLastLocation();
    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {
        LocationParams params = locationBasedOnActivityListener.locationParamsForActivity(detectedActivity);
        if (params != null && locationParams != null && !locationParams.equals(params)) {
            start(locationUpdatedListener, params, false);
        }
    }

    public interface LocationBasedOnActivityListener {
        public LocationParams locationParamsForActivity(DetectedActivity detectedActivity);
    }
}
