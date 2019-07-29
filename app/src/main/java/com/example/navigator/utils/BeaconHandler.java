package com.example.navigator.utils;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import java.util.Collection;

public class BeaconHandler implements BeaconConsumer {

    private BeaconManager beaconManager;
    private static String TAG = "BeaconHandler";
    private double distance;
    private String beaconName;
    private String bluetoothAddress;
    private Context parent;
//    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public BeaconHandler(Context context)
    {
        distance = 0;
        parent = context;
        beaconManager = BeaconManager.getInstanceForApplication(context);
        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw a beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {    }

        try {beaconManager.removeAllRangeNotifiers();
            beaconManager.addRangeNotifier(new RangeNotifier() {
                @Override
                public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                    if (beacons.size() > 0) {
                        Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");

                        distance = beacons.iterator().next().getDistance();

//                        ((TextView)rootView.findViewById(R.id.distance_from_beacon)).setText(distance_str);

                        bluetoothAddress = beacons.iterator().next().getBluetoothAddress();
                        //beaconName = "Unrecognized Beacon";
                        if(bluetoothAddress.equals("51:0D:6F:72:E6:A0")){
                            beaconName = "Edgars";
                        }
                        else if (bluetoothAddress.equals("72:92:CF:8E:05:68")){
                            beaconName = "Spitz";
                        }
                        String distanceLabel = "Distance from " + beaconName;
//                        ((TextView)rootView.findViewById(R.id.distance_label)).setText(distanceLabel);

                        if(distance < 1.00){
//                            reachedDestination();
                        }

                        Log.i(TAG,distance + "");
                    }
                }
            });
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    @Override
    public Context getApplicationContext(){
        return parent;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection){

    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i){
        return false;
    }
}
