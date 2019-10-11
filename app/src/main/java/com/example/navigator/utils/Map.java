package com.example.navigator.utils;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private List<MapPointWrapper> nodes = new ArrayList<MapPointWrapper>();

    public Map()
    {

    }

    public void addPoint(MapPoint point)
    {
        nodes.add(new MapPointWrapper(point));
    }

    public List<MapPoint> inflateMap()
    {
        List<MapPoint> pointList = new ArrayList<MapPoint>();
        for (int i = 0; i < nodes.size(); i++)
        {
            pointList.add(new MapPoint(nodes.get(i)));
        }

        for (int i = 0; i < nodes.size(); i++)
        {
            for (int j = 1; j < nodes.size(); j++)
            {
                   if(nodes.get(i).getNearby().contains(pointList.get(j).getId()))
                   {
                       int index = nodes.get(i).getNearby().indexOf(pointList.get(j).getId());
                       pointList.get(i).addTwoWayPoint(pointList.get(j), nodes.get(i).getDistancesNearby().get(index), nodes.get(i).getBearingNearby().get(index));
                   }
            }
        }

        return pointList;
    }

    public List<MapPointWrapper> getNodes() {
        return nodes;
    }

    public String idFromName(String name)
    {
        for (int i = 0; i < nodes.size(); i++)
        {
            if(nodes.get(i).getName().equals(name))
            {
                return nodes.get(i).getId();
            }
        }
        return null;
    }

}


