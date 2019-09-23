package com.example.navigator.utils;

import android.util.Log;

import org.altbeacon.beacon.Beacon;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BeaconNavigator extends BeaconReader
{
    private Map map;
    private List<MapPoint> nodes;
    private String TAG = "BeaconNavigator";
    private String targetID = null;
    private MapPoint[] directions = null;
    private MapPoint nearestPoint = null;
    private double distanceThreshold = 0.2;
//    private boolean navigating = false;//Do we even need this?

    public BeaconNavigator()
    {
        super();
        Log.d(TAG, "BeaconNavigator: Initialised");
    }

    public BeaconNavigator(Map _map)
    {
        super();
        map = _map;
        nodes = map.inflateMap();
    }

    public BeaconNavigator(Map _map, double threshold)
    {
        super();
        map = _map;
        nodes = map.inflateMap();
        distanceThreshold = threshold;
    }

    public boolean isNavigating()
    {
        return targetID != null;
    }


    public void setTargetID(String id)
    {
        Log.d(TAG, "setTargetID: Set target to" + id);
        targetID = id;
        for (int i = 0; i < nodes.size(); i++)
        {
            if(nodes.get(i).getId().equals(nearestPoint.getId()))
            {
                setDirections(nodes.get(i).getDirectionsTo(targetID, nodes.size()));
            }
        }
    }

    private void setDirections(MapPoint[] directions) {
        Log.d(TAG, "setDirections: Setting Directions to:  " + MapPoint.flattenDirections(directions));
        this.directions = directions;
    }

    private boolean distanceCheck()
    {
        return nearest.getDistance() <= distanceThreshold;
    }

    public void navigate()
    {
        if(targetID != null)
        {
            if(nearestPoint != null)
            {
                Log.d(TAG, "navigate: NearestPoint Is set and target is set");
                if(nearestPoint.getId().equals(targetID))
                {
                    Log.d(TAG, "navigate: Nearest point is the target");
                    if(distanceCheck())
                    {
                        Log.d(TAG, "navigate: Arrived");
                        targetID = null;
                    }else
                    {
                        Log.d(TAG, "navigate: Almost there");
                    }
                }else if(directions[0].getId().equals(nearestPoint.getId()))
                {
                    Log.d(TAG, "navigate: Nearest Point is the next node to reach");
                    if(distanceCheck())
                    {
                        Log.d(TAG, "navigate: Time to go to the next node");
                    }else
                    {
                        Log.d(TAG, "navigate: Almost at the point");
                    }
                }else
                {
                    boolean isOnPath = false;
                    for (int i = 0; i < directions.length; i++)
                    {
                        if(directions[i].getId().equals(nearestPoint.getId()))
                        {
                            isOnPath = true;
                        }
                    }

                    if(isOnPath)
                    {
                        Log.d(TAG, "navigate: User has somehow skipped points and is further along the path");
                    }else
                    {
                        Log.d(TAG, "navigate: User is not on the path. Recalculate");
                    }
                }

            }else
            {
                Log.e(TAG, "navigate: nearestPoint is null but target is set.",new NullPointerException());
            }
//            setTargetID(targetID);
        }
    }

    public float getBearing()
    {
        return (float)nearestPoint.getBearingTo(directions[0].getId());
    }



    @Override
    public void update(Collection<Beacon> beacons)
    {
        super.update(beacons);
        nearestPoint = MapPoint.matchingID(nodes, nearest.getId1().toString());
        navigate();
    }
}
