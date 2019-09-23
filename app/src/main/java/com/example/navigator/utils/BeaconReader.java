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
        setNearest(inRange.get(0));
        double distance = inRange.get(0).getDistance();
        for (int i = 1; i < inRange.size(); i++)
        {
            if(distance > inRange.get(i).getDistance())
            {
                setNearest(inRange.get(i));
                Log.d(TAG, "update: Setting nearest to: " + inRange.get(i).getId1());
            }
        }
    }

    private void setNearest(Beacon beacon)
    {
        nearest = beacon;
        Log.d(TAG, "setNearest: Setting nearest to: " + beacon.getId1());
    }


}
