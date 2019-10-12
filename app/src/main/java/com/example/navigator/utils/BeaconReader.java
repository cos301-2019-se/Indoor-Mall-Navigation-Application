package com.example.navigator.utils;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BeaconReader
{
    protected Beacon nearest;
    protected List<Beacon> inRange;
    protected String TAG = "BeaconReader";


    public BeaconReader()
    {

    }

    public Beacon getNearest()
    {
        return nearest;
    }

    public double getNearestDistance()
    {
        return nearest.getDistance();
    }

    public void update(Collection<Beacon> beacons)
    {
        Log.d(TAG, "update: Updating beacons");
        inRange = new ArrayList<Beacon>(beacons);
        int nearestInd = 0;
//        setNearest(inRange.get(0));
        double distance = inRange.get(0).getDistance();
        for (int i = 0; i < inRange.size(); i++)
        {
            Log.d(TAG, "update: Beacons in range: #" + i + " " + inRange.get(i).getId1().toString() + " distance: " + inRange.get(i).getDistance());
            if(distance > inRange.get(i).getDistance())
            {
                distance = inRange.get(i).getDistance();
                nearestInd = i;
                Log.d(TAG, "update: Setting nearest to: " + inRange.get(i).getId1() + " distance was " + distance);

            }
        }
        setNearest(inRange.get(nearestInd));

    }

    protected void setNearest(Beacon beacon)
    {
        nearest = beacon;
        Log.d(TAG, "setNearest: Setting nearest to: " + beacon.getId1() + " distance was " + beacon.getDistance());
    }


}
