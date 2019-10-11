package com.example.navigator.utils;

import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BeaconNavigator extends BeaconReader {
    private Map map;
    private List<MapPoint> nodes;
    private String TAG = "BeaconNavigator";
    private String targetID = null;
    private MapPoint[] directions = null;
    private MapPoint nearestPoint = null;
    private double distanceThreshold = 0.4;
    private int pathIndex = 0;
    public ArrivalHandler arrival = null;
//    private boolean navigating = false;//Do we even need this?

    public BeaconNavigator() {
        super();
        Log.d(TAG, "BeaconNavigator: Initialised");
    }

    public BeaconNavigator(Map _map) {
        super();
        map = _map;
        nodes = map.inflateMap();
    }

    public BeaconNavigator(Map _map, double threshold) {
        super();
        map = _map;
        nodes = map.inflateMap();
        distanceThreshold = threshold;
    }

    public boolean isNavigating() {
        return targetID != null;
    }


    public void setTargetID(String id) {
        Log.d(TAG, "setTargetID: Set target to" + id);
        targetID = id;
        if (nearestPoint != null) {
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).getId().equals(nearestPoint.getId())) {
                    setDirections(nodes.get(i).getDirectionsTo(targetID, nodes.size()));
                }
            }
        }
    }

    private void setDirections(MapPoint[] directions) {
        Log.d(TAG, "setDirections: Setting Directions to:  " + MapPoint.flattenDirections(directions));
        this.directions = directions;
        pathIndex = 0;
    }

    private boolean distanceCheck() {
        Log.d(TAG, "distanceCheck: Distance " + nearest.getDistance());
        return nearest.getDistance() <= distanceThreshold;
    }

    public void navigate() {
        if (targetID != null) {
            if (nearestPoint != null) {
                Log.d(TAG, "navigate: NearestPoint Is set and target is set");
                if (nearestPoint.getId().equals(targetID)) {
                    Log.d(TAG, "navigate: Nearest point is the target");
                    if (distanceCheck()) {
                        Log.d(TAG, "navigate: Arrived");
                        targetID = null;
                        if(arrival != null)
                        {
                            arrival.onArrival();
                        }
                    } else {
                        Log.d(TAG, "navigate: Almost there");
                    }
                } else if (directions[pathIndex].getId().equals(nearestPoint.getId())) {
                    Log.d(TAG, "navigate: Nearest Point is the next node to reach");
                    if (distanceCheck()) {
                        Log.d(TAG, "navigate: Time to go to the next node");
                        pathIndex++;
                    } else {
                        Log.d(TAG, "navigate: Almost at the point");
                    }
                } else if (pathIndex != 0 && directions[pathIndex - 1].getId().equals(nearestPoint.getId())) {
                    Log.d(TAG, "navigate: Nearest Point is the previous node to reach");
                    if (distanceCheck()) {
                        Log.d(TAG, "navigate: Time to go to the next node");
                    } else {
                        Log.d(TAG, "navigate: Almost at the point");
                    }
                } else {
                    boolean isOnPath = false;
                    for (int i = 0; i < directions.length; i++) {
                        if (directions[i].getId().equals(nearestPoint.getId())) {
                            isOnPath = true;
                        }
                    }

                    if (isOnPath) {
                        Log.d(TAG, "navigate: User is on path");
                    } else {
                        Log.d(TAG, "navigate: User is not on the path. Recalculate");
                    }
                }

            } else {
                Log.e(TAG, "navigate: nearestPoint is null but target is set.", new NullPointerException());
            }
//            setTargetID(targetID);
        }
    }

    public float getBearing() {
        return (float) nearestPoint.getBearingTo(directions[pathIndex].getId());
    }

    private MapPoint[] popDirection(MapPoint[] directions) {
        if (directions.length > 1) {
            MapPoint[] newDirections = new MapPoint[directions.length - 1];
            for (int i = 1; i < directions.length; i++) {
                newDirections[i - 1] = directions[i];
            }
            return newDirections;
        } else {
            return new MapPoint[0];
        }
    }

    public void setArrival(ArrivalHandler _onArrival)
    {
        arrival = _onArrival;
    }



    @Override
    public void update(Collection<Beacon> beacons) {
        super.update(beacons);
        nearestPoint = MapPoint.matchingID(nodes, nearest.getId1().toString());
        navigate();
    }

    public static class ArrivalHandler
    {
        public ArrivalHandler(){};
        public void onArrival()
        {

        }
    }
}
