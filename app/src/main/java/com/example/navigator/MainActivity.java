/**
 *
 *  File Name: WishList.js (path: app/src/main/java/com.example.navigator/utils/wishList.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  02/07/2019  Thomas Honiball    Original
 *
 *
 *  Functional Description: This program file Has the Navigation.
 *  Error Messages: None
 *  Constraints: None
 *  Assumptions: It is assumed that the user knows how to use the App.
 *
 */
package com.example.navigator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.navigator.utils.BeaconNavigator;
import com.example.navigator.utils.BeaconReader;
import com.example.navigator.utils.DatabaseConn;
import com.example.navigator.utils.FirebaseConn;
import com.example.navigator.utils.Map;
import com.example.navigator.utils.MapPoint;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import com.example.navigator.interfaces.NavigationFragmentInteractionListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer, NavigationFragmentInteractionListener {
    private TextView mTextMessage;
    protected static final String TAG = "MonitoringActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    private RelativeLayout mContainer;
    public MixpanelAPI mixpanel;
    public BottomNavigationView bottomNavigationView;
    public static Map map = null;
    public static BeaconNavigator navigator;
    private BeaconManager manager;
    private final Runnable distanceFromBeaconProcess = new Runnable() {
        @Override
        public void run() {
            onBeaconServiceConnect();
        }
    };

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean havePermissions() {

        if (getBaseContext() != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
    }

    @Override
    public void onFragmentInteraction(String previousFragment, String upcomingFragment, Bundle data) {
        Log.d(TAG, "onFragmentInteraction: " + upcomingFragment);
        switch(upcomingFragment) {
            case "GameFragment":
                createAndAddFragment(previousFragment, upcomingFragment, Navigate.class, true, data);
                break;

        }
    }

    public void createAndAddFragment(String previousFragment, String upcomingFragment, Class<? extends Fragment> fragClass, boolean
            addToBackStack, Bundle bundleData) {

        Fragment frag = getSupportFragmentManager().findFragmentByTag(upcomingFragment);
        if(frag == null) {
            Log.d(TAG, "frag is null: " + upcomingFragment);

            try {
                frag = fragClass.newInstance();
                frag.setArguments(bundleData);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "frag is not null : " + upcomingFragment);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, frag, upcomingFragment);
        if(addToBackStack)
        {
            ft.addToBackStack(upcomingFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void hideSplashScreen() {
        //getWindow().getDecorView().findViewById(R.id.splash_screen).setVisibility(View.GONE);
    }

    @Override
    public void trackEvent(String eventName) {
        mixpanel.track(eventName);
    }

    @Override
    public void timeEvent(String eventName) {
        mixpanel.timeEvent(eventName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseConn.open(new FirebaseConn());
        Log.i(TAG, "Beacon Reader Initialised");
        manager = BeaconManager.getInstanceForApplication(this);
        manager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        manager.bind(this);
        map = buildMiniMap();
        navigator = new BeaconNavigator(map);

        setContentView(R.layout.activity_main);

        final String GO_TO = getIntent().getStringExtra("GO_TO");

        bottomNavigationView = findViewById(R.id.nav_view);

        if (!havePermissions()) {
            Log.i(TAG, "Requesting permissions needed for this app.");
            requestPermissions();
        }

        mixpanel = MixpanelAPI.getInstance(this, "9feb719bbed8b10b51fe93fd9915d97d");

        final Navigate navigateFragment = new Navigate();
        final Cart cartFragment = new Cart();
        final Scan scanFragment = new Scan();
        final Wishlist searchFragment = new Wishlist();

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setFragment(navigateFragment);
                        return true;
                    case R.id.navigation_wishlist:
                        setFragment(searchFragment);
                        return true;
                    case R.id.navigation_scan:
                        setFragment(scanFragment);
                        return true;
                    case R.id.navigation_cart:
                        setFragment(cartFragment);
                        return true;
                }
                return false;
            }
        };


        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(GO_TO != null){
            if(GO_TO.equals("scanFragment")) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_scan);
            }
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }


    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    private void showLinkToSettingsSnackbar() {
        if (mContainer == null) {
            return;
        }
        Snackbar.make(mContainer,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).show();
    }

    private void showRequestPermissionsSnackbar() {
        if (mContainer == null) {
            return;
        }
        Snackbar.make(mContainer, R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Request permission.
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            manager.stopRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            manager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[]permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return;
        }

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    Log.i(TAG, "Permission denied without 'NEVER ASK AGAIN': "
                            + permission);
                    showRequestPermissionsSnackbar();
                } else {
                    Log.i(TAG, "Permission denied with 'NEVER ASK AGAIN': "
                            + permission);
                    showLinkToSettingsSnackbar();
                }
            } else {
                Log.i(TAG, "Permission granted, building GoogleApiClient");
            }
        }
    }



    private MapPoint[] buildTestMap()
    {
        DatabaseConn data = DatabaseConn.open();
        data.truncate("Map");

        MapPoint root, almost_there, not_quite, midpoint, little_further, end, another_point, point_a, point_b, point_c, point_d;

        root = new MapPoint("STARTING POINT", "0");
        almost_there = new MapPoint("ALMOST THERE", "8");
        not_quite = new MapPoint("NOT QUITE", "7");
        midpoint = new MapPoint("MIDPOINT", "2");
        little_further = new MapPoint("LITTLE FURTHER", "6");
        end = new MapPoint("END", "1");
        another_point = new MapPoint("ANOTHER POINT", "3");
        point_a = new MapPoint("POINT A", "4");
        point_b = new MapPoint("POINT B", "5");
        point_c = new MapPoint("POINT C", "9");
        point_d = new MapPoint("POINT D", "10");

        //Add points to End
        end.addTwoWayPoint(not_quite, 9.5, 120);
        end.addTwoWayPoint(almost_there, 7.8, 240);

        //Add points to Almost_There
        almost_there.addTwoWayPoint(midpoint, 8.3, 150);

        //Add points to Midpoint
        midpoint.addTwoWayPoint(little_further, 9.6, 250);

        //Add points to Little Further
        little_further.addTwoWayPoint(another_point, 5.7, 250);

        //Add points to Another Point
        another_point.addTwoWayPoint(point_a, 8.6, 90);
        another_point.addTwoWayPoint(point_c, 10.1, 120);

        //Add points to Point A
        point_a.addTwoWayPoint(point_b, 9.3, 90);

        //Add points to Point C
        point_c.addTwoWayPoint(point_d, 20.2, 90);

        //Add points to Point B
        point_b.addTwoWayPoint(point_d, 3.5, 180);

        //Add points to root
        root.addTwoWayPoint(almost_there, 21.5, 30);
        root.addTwoWayPoint(not_quite, 10.5, 330);
        root.addTwoWayPoint(midpoint, 7.2, 120);
        root.addTwoWayPoint(little_further, 30.6, 180);
        data.insert("Map", root);
        data.insert("Map", almost_there);
        data.insert("Map", not_quite);
        data.insert("Map", midpoint);
        data.insert("Map", little_further);
        data.insert("Map", end);
        data.insert("Map", another_point);
        data.insert("Map", point_a);
        data.insert("Map", point_b);
        data.insert("Map", point_c);
        data.insert("Map", point_d);
        MapPoint[] mapList = {root, almost_there, not_quite, midpoint, little_further, end, another_point, point_a, point_b, point_c, point_d};
        return mapList;
    }

    private Map buildMiniMap()
    {
        DatabaseConn data = DatabaseConn.open();
        data.truncate("Map");

        MapPoint CNA, woolworths, pnp;

        CNA = new MapPoint("CNA", "c4a514b8-0492-4249-9575-1fab2d059c44");
        woolworths = new MapPoint("Woolworths", "4edf0c20-f1b0-4d0d-8c2e-fd0758057dfe");

        pnp = new MapPoint("Pick 'n Pay", "a038e0b7-505c-4340-a32a-13645a04cece");

//        Khodani: 00f0c1d6-7539-4ca7-b676-2b9a1e352f24
//        Thomas: c4a514b8-0492-4249-9575-1fab2d059c44
//        Bandile_1: 4edf0c20-f1b0-4d0d-8c2e-fd0758057dfe
//        Thabo: a038e0b7-505c-4340-a32a-13645a04cece
//        Mpho:


        CNA.addTwoWayPoint(woolworths, 2.0, 257);
        woolworths.addTwoWayPoint(pnp, 4, 70);
        Map map = new Map();
        map.addPoint(CNA);
        map.addPoint(woolworths);
        map.addPoint(pnp);
//        MapPoint[] mapList = {CNA, woolworths, pnp};
        data.insert("Map", map);
        return map;
    }

//    BeaconConsumer Overrides
@Override
protected void onDestroy() {
    super.onDestroy();
    manager.unbind(this);
}
@Override
public void onBeaconServiceConnect() {
    manager.removeAllRangeNotifiers();
    manager.addRangeNotifier(new RangeNotifier() {
        @Override
        public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
            if (beacons.size() > 0) {
//                Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                navigator.update(beacons);
            }
        }
    });

    try {
        manager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
    } catch (RemoteException e) {    }
}

    private void initializeBeaconDistance() {
        distanceFromBeaconProcess.run();
    }


}
