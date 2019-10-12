/**
 *
 *  File Name: Naivgate.js (path: app/src/main/java/com.example.navigator/utils/Naivgate.js)
 *  Version: 1.0
 *  Author: Brute Force - Database Management
 *  Project: Indoor Mall Navigation
 *  Organisation: DVT
 *  Copyright: (c) Copyright 2019 University of Pretoria
 *  Update History:*
 *
 *  Date        Author           Changes
 *  --------------------------------------------
 *  20/06/2019  Bandile Dlamini Original
 *
 *
 *  Functional Description: This program file helps the user to Navigate.
 *  Error Messages: None
 *  Constraints: None
 *  Assumptions: It is assumed that the user knows how to navigate.
 *
 */


package com.example.navigator;

import android.content.ServiceConnection;
import android.hardware.GeomagneticField;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.animation.Animator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.navigator.interfaces.NavigationFragmentInteractionListener;
import com.example.navigator.utils.AngleLowpassFilter;
import com.example.navigator.utils.ArDisplayView;
import com.example.navigator.utils.BeaconNavigator;
import com.example.navigator.utils.CompassView;
import com.example.navigator.utils.Installation;
import com.example.navigator.utils.LowPassFilter;
import com.example.navigator.utils.MapPoint;
import com.example.navigator.utils.Orientation;
import com.example.navigator.utils.RadarScanView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import com.example.navigator.utils.BeaconNavigator;
import com.example.navigator.utils.BeaconNavigator.ArrivalHandler;


import static android.content.Context.SENSOR_SERVICE;
import static com.example.navigator.MainActivity.map;
import static com.example.navigator.MainActivity.navigator;


/**
 * A simple {@link Fragment} subclass.
 */

public class Navigate extends Fragment implements SensorEventListener,
        LocationListener{

    private RelativeLayout mContainer;
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private AngleLowpassFilter angleLowpassFilter = new AngleLowpassFilter();
    public static final String NA = "N/A";
    public static final String FIXED = "FIXED";
    private static final int LOCATION_MIN_TIME = 30 * 1000;
    private static final int LOCATION_MIN_DISTANCE = 10;
    private Orientation orienter;
    private SensorManager sensorManager;
    private Sensor sensorGravity;
    private Sensor sensorMagnetic;
    private LocationManager locationManager;
    private Location currentLocation;
    private GeomagneticField geomagneticField;
    private TextView textDirection, textLat, textLong;
    private CompassView compassView;
    private String TAG = "NavigationFragment";
    private NavigationFragmentInteractionListener mListener;
    private View rootView;
    private ImageView green_dot;
    private ImageView arrowView;
    private FrameLayout arContentOverlay = null;
    private Handler handler = new Handler();
    private Button navigateButton = null;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private SensorManager mSensorManager;
    private Sensor accSensor;
    private Sensor magnetSensor;
    private String selectedShop = "";
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String > adapter;
    private RadarScanView mRadar;
    private String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA
    };
    private ArrayList<Beacon> beaconsInRange = new ArrayList<>();
    private double bearing = 180;
    private double distance;
    private float[] orientationSmoothing = new float[10];
    private int smoothing = 0;


//    final Runnable distanceFromBeaconProcess = new Runnable() {
//        @Override
//        public void run() {
//            onBeaconServiceConnect();
//        }
//    };


    public Navigate() {
        // Required empty public constructor
    }

    public boolean searchList(String needle, ArrayList<String> haystack)
    {
        boolean foundNeedle = false;
        ArrayList<String> needleStack = new ArrayList<>();

        for(int i = 0; i < haystack.size(); i++){
            if(haystack.get(i).toLowerCase().contains(needle.toLowerCase())){

                if(!needleStack.contains(haystack.get(i))){
                    needleStack.add(haystack.get(i));
                }
                foundNeedle = true;
            }
        }

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, needleStack);

        return foundNeedle;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        beaconManager = BeaconManager.getInstanceForApplication(getContext());
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
//        beaconManager.bind(this);


        rootView = inflater.inflate(R.layout.fragment_navigate,container,false);
        orienter = new Orientation();


        //------------------------ Search Bar Implementation--------------------------------------
        SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
        listView = (ListView) rootView.findViewById(R.id.lv1);
        list = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Shop").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ShopName = snapshot.child("name").getValue().toString();
                    list.add(ShopName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedShop = listView.getItemAtPosition(position).toString() ;
                navigateButton.setVisibility(View.VISIBLE);
                navigateButton.setText("Navigate to " + selectedShop);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(searchList(query, list))
                {
                    listView.setAdapter(adapter);
                    return true;
                }
                else
                {
                    listView.setAdapter(null);
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchList(newText, list))
                {
                    listView.setAdapter(adapter);
                    return true;
                }
                else
                {
                    listView.setAdapter(null);
                    return false;
                }
            }
        });

        //--------------- End of Search Implimentation --------------------------------------------

        Log.d(TAG,"onCreateView");
//        beaconManager = BeaconManager.getInstanceForApplication(getContext());
//        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
//        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
//        beaconManager.bind(this);


        navigateButton = rootView.findViewById(R.id.navigate_button);
        textLat = (TextView) rootView.findViewById(R.id.latitude);
        textLong = (TextView) rootView.findViewById(R.id.longitude);
        textDirection = (TextView) rootView.findViewById(R.id.text);
        compassView = (CompassView) rootView.findViewById(R.id.compass);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        magnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        green_dot = rootView.findViewById(R.id.greenDot);
        arrowView = rootView.findViewById(R.id.arrow);


        if (ContextCompat.checkSelfPermission( getContext() ,android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String [] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION
            );
        }

        LocationListener locationListener;
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        Boolean flag = displayGpsStatus();
        if (flag) {
            Log.v(TAG, "onClick");
            locationListener = new MyLocationListener();

            if(havePermissions()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            }
            else {
                requestPermissions();
            }

        } else {
            Toast.makeText(getContext(),"Your GPS is: OFF", Toast.LENGTH_LONG).show();
        }

        mListener.timeEvent("App Opened to Navigate");
        configureOverlayWindow();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavigationFragmentInteractionListener) {
            mListener = (NavigationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        navigator.setBeaconFound(new BeaconNavigator.BeaconFoundHandler(){
            @Override
            public void onBeaconFound() {
                rootView.findViewById(R.id.greenDot).setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");

        stopSensing();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        registerSensors();

        if (!havePermissions()) {
            Log.i(TAG, "Requesting permissions needed for this app.");
            requestPermissions();
        }

        Location gpsLocation = null;

        if (getContext() != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            LOCATION_MIN_TIME, LOCATION_MIN_DISTANCE, this);
                    // get last known position
                    gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (gpsLocation != null) {
                        currentLocation = gpsLocation;
                    } else {
                        Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (networkLocation != null) {
                            currentLocation = networkLocation;
                        } else {
                            currentLocation = new Location(FIXED);
                            currentLocation.setAltitude(1);
                            currentLocation.setLatitude(43.296482);
                            currentLocation.setLongitude(5.36978);
                        }
                        onLocationChanged(currentLocation);
                    }
                }
            }
        }

    }

    @Override
    public void onStop() {
        Log.d(TAG,"onStop");
        stopAllRunnables();
//        handler.removeCallbacks(distanceFromBeaconProcess);

        sensorManager.unregisterListener(this, sensorGravity);
        sensorManager.unregisterListener(this, sensorMagnetic);
        locationManager.removeUpdates(this);

        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        updateLocation(location);
        geomagneticField = new GeomagneticField(
                (float) currentLocation.getLatitude(),
                (float) currentLocation.getLongitude(),
                (float) currentLocation.getAltitude(),
                System.currentTimeMillis());
    }

    private void updateLocation(Location location) {
        if (FIXED.equals(location.getProvider())) {
            textLat.setText(NA);
            textLong.setText(NA);
        }

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        NumberFormat formatter = new DecimalFormat("#0.00", dfs);
        textLat.setText("Lat : " + formatter.format(location.getLatitude()));
        textLong.setText("Long : " + formatter.format(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG,"onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        rootView.findViewById(R.id.instructions_container).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.arrived_at_destination).setVisibility(View.GONE);
        rootView.findViewById(R.id.ar_container).setVisibility(View.GONE);
        rootView.findViewById(R.id.camera_frame).setVisibility(View.GONE);
        rootView.findViewById(R.id.ar_content_overlay).setVisibility(View.GONE);


        assignClickListeners();
        configureSensors();
        registerSensors();
    }

    private void registerSensors()
    {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);

    }

    private void assignClickListeners()
    {
        rootView.findViewById(R.id.navigate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onLongClick: ");
                mListener.trackEvent("App Opened to Navigate");

                rootView.findViewById(R.id.instructions_container).setVisibility(View.GONE);
                initializeAr();
                prepareNavigation();
            }
        });


        rootView.findViewById(R.id.stop_button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                rootView.findViewById(R.id.instructions_container).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.arrived_at_destination).setVisibility(View.GONE);
                rootView.findViewById(R.id.arrow).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.ar_container).setVisibility(View.GONE);
                rootView.findViewById(R.id.camera_frame).setVisibility(View.GONE);
                rootView.findViewById(R.id.ar_content_overlay).setVisibility(View.GONE);
                navigator.setTargetID(null);


                listView.setVisibility(View.VISIBLE);
                navigateButton.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        View.OnClickListener playAgain = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareNavigation();
                mListener.trackEvent("Play Again Button Clicked");
            }
        };

        rootView.findViewById(R.id.play_again_win).setOnClickListener(playAgain);

        View.OnClickListener shareApp = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareGeneral();
                mListener.trackEvent("Share Link Clicked");
            }
        };

    }

    private void configureOverlayWindow() {
        arContentOverlay = (FrameLayout)rootView.findViewById(R.id.ar_content_overlay);
    }

    private void configureSensors()
    {
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        boolean isGyroAvailable = sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
        boolean isAccelAvailable = sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);

        Log.d(TAG, "isGyroAvailable: " +isGyroAvailable);
        Log.d(TAG, "isAccelAvailable: " +isAccelAvailable);
    }

    private void initializeAr() {
        arContentOverlay.setVisibility(View.VISIBLE);
        FrameLayout arViewPane = (FrameLayout) rootView.findViewById(R.id.camera_frame);
        arViewPane.removeAllViews();
        ArDisplayView arDisplay = new ArDisplayView(getContext());
        arViewPane.addView(arDisplay);
        arViewPane.setVisibility(View.VISIBLE);

    }

//    private void initializeBeaconDistance() {
//        distanceFromBeaconProcess.run();
//    }

    private void reachedDestination() {
//        rootView.findViewById(R.id.stop_button).setVisibility(View.GONE);
        rootView.findViewById(R.id.crosshairs).setVisibility(View.GONE);
        rootView.findViewById(R.id.compass).setVisibility(View.GONE);
        rootView.findViewById(R.id.check_point_label).setVisibility(View.GONE);
        rootView.findViewById(R.id.check_point).setVisibility(View.GONE);
        rootView.findViewById(R.id.distance_label).setVisibility(View.GONE);
        rootView.findViewById(R.id.distance_from_beacon).setVisibility(View.GONE);
        rootView.findViewById(R.id.arrow).setVisibility(View.GONE);
//        handler.removeCallbacks(distanceFromBeaconProcess);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rootView.findViewById(R.id.arrived_at_destination).setVisibility(View.VISIBLE);
            }
        }, 500);

    }

    private void prepareNavigation() {
        arContentOverlay.removeAllViews();

        rootView.findViewById(R.id.instructions_container).setVisibility(View.GONE);
        rootView.findViewById(R.id.how_to_use_container).setVisibility(View.GONE);
        rootView.findViewById(R.id.arrived_at_destination).setVisibility(View.GONE);
        rootView.findViewById(R.id.ar_container).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.stop_button).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.crosshairs).setVisibility(View.VISIBLE);
        ((TextView)rootView.findViewById(R.id.check_point_label)).setTextColor(ContextCompat.getColor(getContext(), R.color.white));


        TextView checkPoint = (TextView) rootView.findViewById(R.id.check_point);
        checkPoint.setText(selectedShop);
        MainActivity.navigator.setTargetID(MainActivity.map.idFromName(selectedShop));
        MainActivity.navigator.setArrival(new ArrivalHandler(){
            @Override
            public void onArrival() {
                reachedDestination();
            }
        });
        MainActivity.navigator.setDistanceHandler(new BeaconNavigator.DistanceHandler(){
            @Override
            public void onDistanceChange(double distance) {
                ((TextView)rootView.findViewById(R.id.distance_from_beacon)).setText("" + Math.round(distance*100)/100);
            }
        });

//        initializeBeaconDistance();
    }

    private void shareGeneral() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share link using"));
    }


    private void stopAllRunnables()
    {
    }

    private void stopSensing() {
        sensorManager.unregisterListener(this);
    }


    protected double bearing(double startLat, double startLng, double endLat, double endLng){
        double longitude1 = startLng;
        double longitude2 = endLng;
        double latitude1 = Math.toRadians(startLat);
        double latitude2 = Math.toRadians(endLat);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        float north = 360-orienter.updateOrientation(event);
        while(north > 360)
        {
            north -= 360;
        }
        addSmoothing(north);
        Log.d(TAG, "onSensorChanged: North: " + north);
        Log.d(TAG, "onSensorChanged: Smoothed: North: " + getSmoothing());
        if(navigator.isNavigating())
        {
            float bearing = (getSmoothing() + navigator.getBearing());

            float rotation = arrowView.getRotation();
            if(rotation < bearing)
            {
                arrowView.setRotation((float)(rotation + 1));
            }else
            {
                arrowView.setRotation((float)(rotation - 1));
            }
//            arrowView.setRotation(bearing);
        }

    }

    private void updateTextDirection(double bearing) {
        int range = (int) (bearing / (360f / 16f));
        String dirTxt = "";

        if (range == 15 || range == 0)
            dirTxt = "N";
        if (range == 1 || range == 2)
            dirTxt = "NE";
        if (range == 3 || range == 4)
            dirTxt = "E";
        if (range == 5 || range == 6)
            dirTxt = "SE";
        if (range == 7 || range == 8)
            dirTxt = "S";
        if (range == 9 || range == 10)
            dirTxt = "SW";
        if (range == 11 || range == 12)
            dirTxt = "W";
        if (range == 13 || range == 14)
            dirTxt = "NW";

        textDirection.setText("" + ((int) bearing) + ((char) 176) + " "+ dirTxt);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(TAG, "onAccuracyChanged");
        Log.d(TAG, sensor.toString());

        switch(sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                break;
            case Sensor.TYPE_GYROSCOPE:
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                break;
        }

        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD && i == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            // TODO manage fact that compass data are unreliable ...
            // toast ? display on screen ?
        }

    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getContext().getContentResolver();
        boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;
        } else {
            return false;
        }
    }

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

//            currentLat = loc.getLatitude();
//            currentLong = loc.getLongitude();

            //bearing(loc.getLongitude(), loc.getLatitude(), 0.0, 0.0);

            try {
                /*------- To get city name from coordinates -------- */
                String cityName = null;
                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(loc.getLatitude(),
                            loc.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        System.out.println(addresses.get(0).getLocality());
                        cityName = addresses.get(0).getLocality();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String s = longitude + "\n" + latitude + "\n\nMy Current City is: " + cityName;
            }
            catch (Exception ex){
            }
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }


    private boolean havePermissions() {
        if (getContext() != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 1);
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
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
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

    private void addSmoothing(float orientation)
    {
        if(smoothing < orientationSmoothing.length)
        {
            orientationSmoothing[smoothing] = orientation;
            smoothing++;
        }else
        {
            for (int i = 0; i < orientationSmoothing.length-1; i++)
            {
                orientationSmoothing[i] = orientationSmoothing[i+1];
            }
            orientationSmoothing[orientationSmoothing.length-1] = orientation;
        }
    }

    private float getSmoothing()
    {
        float sum = 0;
        for (int i = 0; i < orientationSmoothing.length; i++)
        {
            sum += orientationSmoothing[i];
        }
        return sum/orientationSmoothing.length;
    }



}
