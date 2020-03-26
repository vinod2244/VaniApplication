package com.shukla.tech.hospitalapplication.locationlib.location;

import android.content.Context;
import android.location.Location;

import com.shukla.tech.hospitalapplication.locationlib.OnLocationUpdatedListener;
import com.shukla.tech.hospitalapplication.locationlib.location.config.LocationParams;
import com.shukla.tech.hospitalapplication.locationlib.utils.Logger;


/**
 * Created by mrm on 20/12/14.
 */
public interface LocationProvider {
    void init(Context context, Logger logger);

    void start(OnLocationUpdatedListener listener, LocationParams params, boolean singleUpdate);

    void stop();

    Location getLastLocation();

}
