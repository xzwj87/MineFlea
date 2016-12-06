package com.github.xzwj87.mineflea.utils;

import com.amap.api.maps.model.LatLng;

/**
 * a utility class to get distance between two points
 * reference: http://www.geodatasource.com/developers/c-sharp
 */

public class LocationUtils {

    public static int getDistance(LatLng l1,LatLng l2){
        if(l1 == null || l2 == null) return 0;

        double longDiff = l1.longitude - l2.longitude;
        double dist = Math.sin(degToRad(l1.latitude) * degToRad(l2.latitude) +
                    Math.cos(degToRad(l1.latitude)) * Math.cos(degToRad(l2.latitude)) * Math.cos(degToRad(longDiff)));
        dist = radToDeg(Math.acos(dist));
        dist *= 60 * 1.1515;
        // convert it to kilometer
        dist *= 1.609344;

        return (int)dist;
    }

    private static double degToRad(double deg){
        return (deg * Math.PI /180);
    }

    private static double radToDeg(double rad){
        return (rad/Math.PI * 180);
    }
}
