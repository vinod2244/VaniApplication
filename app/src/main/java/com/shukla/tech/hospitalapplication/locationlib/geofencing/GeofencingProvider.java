package com.shukla.tech.hospitalapplication.locationlib.geofencing;

import android.content.Context;

import java.util.List;

import com.shukla.tech.hospitalapplication.locationlib.OnGeofencingTransitionListener;
import com.shukla.tech.hospitalapplication.locationlib.geofencing.model.GeofenceModel;
import com.shukla.tech.hospitalapplication.locationlib.utils.Logger;

/**
 * Created by mrm on 20/12/14.
 */
public interface GeofencingProvider {
    void init(Context context, Logger logger);

    void start(OnGeofencingTransitionListener listener);

    void addGeofence(GeofenceModel geofence);

    void addGeofences(List<GeofenceModel> geofenceList);

    void removeGeofence(String geofenceId);

    void removeGeofences(List<String> geofenceIds);

    void stop();

}
