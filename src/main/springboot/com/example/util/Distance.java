package com.example.util;
//this is a class based solution using distnace ,just another way to use it 
public class Distance {

	private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = distance(dLat) + Math.cos(startLat) * Math.cos(endLat) * distace(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double distance(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
