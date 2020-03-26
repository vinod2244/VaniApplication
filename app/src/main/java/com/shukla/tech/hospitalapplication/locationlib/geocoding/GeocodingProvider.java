package com.shukla.tech.hospitalapplication.locationlib.geocoding;

import android.content.Context;
import android.location.Location;

import com.shukla.tech.hospitalapplication.locationlib.OnGeocodingListener;
import com.shukla.tech.hospitalapplication.locationlib.OnReverseGeocodingListener;
import com.shukla.tech.hospitalapplication.locationlib.utils.Logger;

/**
 * Created by mrm on 20/12/14.
 */
public interface GeocodingProvider {
    void init(Context context, Logger logger);

    void addName(String name, int maxResults);

    void addLocation(Location location, int maxResults);

    void start(OnGeocodingListener geocodingListener, OnReverseGeocodingListener reverseGeocodingListener);

    void stop();

}
