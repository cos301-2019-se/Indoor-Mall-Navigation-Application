package com.example.navigator.utils;


import java.util.List;

/**
 * A class made to hold MapPoints for Database Storage to avoid the entire infinite looping issue
 */
public class MapPointWrapper
{
    private List<String> nearby = null;// List of nearby MapPoints
    private List<Double> distancesNearby = null, bearingNearby = null;// Distances to the listed MapPoints
    private String name = null, id=null;//Name and ID of this MapPoint

    public MapPointWrapper(MapPoint point)
    {
        name = point.getName();
        id = point.getId();
        nearby = point.getNearby();
        distancesNearby = point.getDistancesNearby();
        bearingNearby = point.getBearingNearby();

    }

    public List<String> getNearby() {
        return nearby;
    }

    public List<Double> getDistancesNearby() {
        return distancesNearby;
    }

    public List<Double> getBearingNearby() {
        return bearingNearby;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
