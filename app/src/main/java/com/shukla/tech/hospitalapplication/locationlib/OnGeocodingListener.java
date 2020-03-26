package com.shukla.tech.hospitalapplication.locationlib;

import java.util.List;

import com.shukla.tech.hospitalapplication.locationlib.geocoding.utils.LocationAddress;

/**
 * Created by mrm on 4/1/15.
 */
public interface OnGeocodingListener {
    void onLocationResolved(String name, List<LocationAddress> results);
}