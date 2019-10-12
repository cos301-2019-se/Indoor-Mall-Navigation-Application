package com.example.navigator.utils;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class created in order to handle maplistings for beacons. This just allows us to build a pseudo graph in order to find all nearby elements to one another
 */
public class MapPoint {
    private MapPoint[] nearby = null;// List of nearby MapPoints
    private double[] distancesNearby = null, bearingNearby = null;// Distances to the listed MapPoints
    private int points = 0; //Number of points
    private String name = null, id=null;//Name and ID of this MapPoint


    /**
     * Empty constructor just instantiates the nearby and distanceTo arrays
     */
    public MapPoint()
    {
        nearby = new MapPoint[points];
        distancesNearby = new double[points];
        bearingNearby = new double[points];
    }

    /**
     * Constructor to create a named and ID'd point with empty nearby and distanceTo arrays
     * @param _name Name to set
     * @param _id ID to set
     */
    public MapPoint(String _name, String _id)
    {
        name = _name;
        id = _id;
        nearby = new MapPoint[points];
        distancesNearby = new double[points];
        bearingNearby = new double[points];
    }

    public MapPoint(MapPointWrapper wrapper)
    {
        name = wrapper.getName();
        id = wrapper.getId();
        nearby = new MapPoint[points];
        distancesNearby = new double[points];
        bearingNearby = new double[points];
    }

    /**
     * The big boi constructor to set a MapPoint with name and id set as well as provided lists of MapPoints and Distances
     * @param _name Name to be set
     * @param _id ID to be set
     * @param _points MapPoints to set
     * @param distances Distances to set
     */
    public MapPoint(String _name, String _id, MapPoint[] _points, double[] distances, double[] bearings)
    {
        name = _name;
        id = _id;
        clearPoints();
        for (int i = 0; i < distances.length; i++)
        {
            addPoint(_points[i], distances[i], bearings[i]);
        }

    }

    /**
     * Function to add a point to the list of points
     *
     * @param point Point to add
     * @param distance Distance to the specified point
     */
    public void addPoint(MapPoint point, double distance, double bearing)
    {
        points ++;
        MapPoint[] temp = new MapPoint[points];
        double[] tempDist = new double[points];
        double[] tempBearing = new double[points];
        for(int i = 0; i < nearby.length; i ++)
        {
            temp[i] = nearby[i];
            tempDist[i] = distancesNearby[i];
            tempBearing[i] = bearingNearby[i];
        }
        temp[points-1] = point;
        tempDist[points-1] = distance;
        tempBearing[points-1] = bearing;
        nearby = temp;
        distancesNearby = tempDist;
        bearingNearby = tempBearing;
    }

    /**
     * Function to add a point to this points list of points and then add this point to the other points list of points
     * I.E. A must be nearby B, but B must also be nearby A
     * @param point Point to add
     * @param distance Distance to the specified point
     */
    public void addTwoWayPoint(MapPoint point, double distance, double bearing)
    {
        if(!this.getNearby().contains(point.getId()))
        {
            this.addPoint(point, distance, bearing);


        }
        if(!point.getNearby().contains(this.getId()))
        {
            point.addPoint(this, distance, 180+bearing);

        }
    }

    /**
     * Delete all nearby MapPoints
     */
    public void clearPoints()
    {
        points = 0;
        nearby = new MapPoint[0];
        distancesNearby = new double[0];
        bearingNearby = new double[0];
    }

    /**
     * Entry function to find a list of MapPoints leading from this MapPoint to a MapPoint with a specified ID
     * @param target_id The ID of the target MapPoint
     * @param numHops The maximum number of times to recur before assuming failure
     * @return A list of MapPoints between this one and the target, or null on failure
     */
    public MapPoint[] getDirectionsTo(String target_id, int numHops)
    {

        return getDirectionsTo(target_id,numHops, this.getId());
    }

    /**
     * Recursive function to find a list of MapPoints leading from this MapPoint to a MapPoint with a specified ID
     * @param target_id The ID of the target MapPoint
     * @param numHops The maximum number of times to recur before assuming failure
     * @param callerID ID of calling point to prevent recursing backwards
     * @return A list of MapPoints between this one and the target, or null on failure
     */
    private MapPoint[] getDirectionsTo(String target_id, int numHops, String callerID)
    {

        if(target_id != null && target_id.equals(this.id))
        {
            MapPoint[] me = {this};
            return me;
        }else if (target_id == null)
        {
            return null;
        }else if(numHops == 0)
        {
            return null;
        }else
        {

//            if(hasVisited.contains(this.id))
//            {
////                return null;
//            }else
//            {
//                hasVisited.add(this.id);
//            }

            MapPoint[][] directions = new MapPoint[points][];
            boolean found = false;
            boolean conflict =false;
            for(int i = 0; i < points; i++)
            {
//                System.out.println(hasVisited.toString() + " AT NODE " + this.getName() + " POLLING " + nearby[i].getName());
//                if(hasVisited.contains(nearby[i].getId()))
//                {
//                    directions[i] = null;
//                }else
//                {
//                    directions[i] = nearby[i].getDirectionsTo(target_id, numHops-1, hasVisited);
//                }
                if(nearby[i].getId().equals(callerID))
                {
                    directions[i] = null;
                }else
                {
                    directions[i] = nearby[i].getDirectionsTo(target_id, numHops-1, this.getId());
                }

                if(directions[i] != null)
                {
                    if(found)
                    {
                        conflict = true;
//                        System.out.println("CONFLICT");
                    }else
                    {
                        found = true;
                    }
                }
            }

            if(conflict)
            {
                int index = -1;
                double distance = -1.0;

                for (int i = 0; i < points; i++)
                {
                    if(directions[i] != null)
                    {
//                        System.out.println("Conflict going to ID: " + target_id);
//                        System.out.println("This direction is from " + flattenDirections(directions[i]) + " Starting at " + this.toString());
                        if(index == -1)
                        {
                            index = i;
                            distance = this.distanceTo(directions[i][0].getId());
                            for (int j = 0; j < directions[i].length-1; j++)
                            {
                                distance += directions[i][j].distanceTo(directions[i][j+1].getId());
                            }
//                            System.out.println("Distance is : " + distance);
                        }else
                        {
                            double competingDistance = this.distanceTo(directions[i][0].getId());
                            for (int j = 0; j < directions[i].length-1; j++)
                            {
                                competingDistance += directions[i][j].distanceTo(directions[i][j+1].getId());
                            }
//                            System.out.println("Competing Distance is : " + competingDistance);
                            if(competingDistance < distance)
                            {
                                index = i;
                                distance = competingDistance;
                            }
                        }
                    }
                }
                if(index == -1)
                {
                    return null;
                }else
                {
//                    System.out.println("Selected Option :" + index);
//                    System.out.println("With Directions: " + flattenDirections(directions[index]));
//                    System.out.println("Starting From: " + this.toString());
//                    System.out.println("And Distance: " + distance);
                    MapPoint[] trail = new MapPoint[directions[index].length+1];
                    trail[0] = this;
                    for (int j = 1; j < trail.length; j++)
                    {
                        trail[j] = directions[index][j-1];
                    }
                    return trail;
                }

            }else
            {
                if(found)
                {
                    for(int i = 0; i < points; i ++)
                    {
                        if(directions[i] != null)
                        {
                            MapPoint[] trail = new MapPoint[directions[i].length+1];
                            trail[0] = this;
                            for (int j = 1; j < trail.length; j++)
                            {
                                trail[j] = directions[i][j-1];
                            }
                            return trail;
                        }
                    }
                    return null;
                }else
                {
                    return null;
                }

            }
        }

    }

    /**
     * Returns the distance to a specified ID if it exists in the nearby list
     * @param targetID The ID to be checked for
     * @return The distance to the target, -1 if the target doesn't exist
     */
    public double distanceTo(String targetID)
    {
        for(int i = 0; i < points; i++)
        {
            if(nearby[i].getId().equals(targetID))
            {
                return distancesNearby[i];
            }
        }
        return -1.0;
    }

    /**
     * Returns the bearing to a specified ID if it exists in the nearby list
     * @param targetID The ID to be checked for
     * @return The bearing to the target, -1 if the target doesn't exist
     */
    public double getBearingTo(String targetID)
    {
        for(int i = 0; i < points; i++)
        {
            if(nearby[i].getId().equals(targetID))
            {
                return bearingNearby[i];
            }
        }
        return -1.0;
    }



    /**
     * Getter
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns readable String version of a MapPoint
     * @return Formatted Name + ID of MapPoint
     */
    public String toString()
    {
        return "[Name: " + name + ", ID: " + id + "]";
    }

    public List<String> getNearby() {
        List<String> flattened = new ArrayList<String>();
        for(int i = 0; i < nearby.length; i++)
        {
            flattened.add(nearby[i].getId());
        }
        return flattened;
    }

    public List<Double> getDistancesNearby() {
        Double[] distanceDoubles = new Double[distancesNearby.length];
        for (int i = 0; i < distancesNearby.length; i++)
        {
            distanceDoubles[i] = distancesNearby[i];
        }
        return Arrays.asList(distanceDoubles);
    }
//
    public List<Double> getBearingNearby() {
        Double[] bearingDoubles = new Double[bearingNearby.length];
        for (int i = 0; i < bearingNearby.length; i++)
        {
            bearingDoubles[i] = bearingNearby[i];
        }
        return Arrays.asList(bearingDoubles);
    }

    public int getPoints() {
        return points;
    }

    public String getNearbyLocations()
    {
        String out = "";
        for (int i = 0; i < nearby.length; i++)
        {
            if(i != 0)
            {
                out += ", ";
            }
            out += nearby[i].toString();
        }
        return out;
    }

    public static String flattenDirections(MapPoint[] directions)
    {
        String out = "";
        for (int i = 0; i < directions.length; i++)
        {
            out += directions[i].toString();
            if(i != directions.length-1)
            {
                out += " => ";
            }
        }
        return out;
    }

    public static MapPoint matchingID(List<MapPoint> list, String id)
    {
//        MapPoint result = null;
        for (int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getId().equals(id))
            {
                return list.get(i);
            }
        }
        return null;
    }

}
