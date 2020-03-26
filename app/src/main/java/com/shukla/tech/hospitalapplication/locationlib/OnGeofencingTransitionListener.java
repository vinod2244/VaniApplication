package com.shukla.tech.hospitalapplication.locationlib;

import com.shukla.tech.hospitalapplication.locationlib.geofencing.utils.TransitionGeofence;

/**
 * Created by mrm on 4/1/15.
 */
public interface OnGeofencingTransitionListener {
    void onGeofenceTransition(TransitionGeofence transitionGeofence);
}