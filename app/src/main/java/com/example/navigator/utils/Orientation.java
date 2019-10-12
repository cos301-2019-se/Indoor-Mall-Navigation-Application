package com.example.navigator.utils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

public class Orientation {

    private float[] gravity = null;
    private float[] geomagnetic = null;
    private float[] rotationMatrix = new float[9];
    private float[] rotationFlipped = new float[9];
    private float[] inclinationMatrix = new float[9];
    private float[] orientation = new float[3];
    private float[] orientationFlipped = new float[3];
    private float azimuth = 0;
    private float inclination = 0;
    private String TAG = "Orientation";

    public Orientation(){}

    private void setGravity(float[] _gravity)
    {
        gravity = new float[3];
        for (int i = 0; i < 3; i++)
        {
            gravity[i] = _gravity[i];
        }
    }

    private void setGeomagnetic(float[] _geomagnetic)
    {
        geomagnetic = new float[3];
        for (int i = 0; i < 3; i++)
        {
            geomagnetic[i] = _geomagnetic[i];
        }
    }

    private void setAzimuth(float[] orientationArray)
    {
        if(orientationArray != null)
        {
            azimuth =(float) Math.toDegrees(orientationArray[0]);
            Log.d(TAG, "setAzimuth: Azimuth : " + azimuth);
        }
    }

    private void checkAzimuth()
    {
        if(isFlat())
        {
            Log.d(TAG, "checkAzimuth: Flat");
            setAzimuth(orientation);
        }else
        {
            Log.d(TAG, "checkAzimuth: Not Flat");
            setAzimuth(orientationFlipped);
        }
    }


    private void updateRotation()
    {
        if(gravity != null && geomagnetic != null)
        {
            boolean gotRotationMatrix = SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, gravity, geomagnetic);
            if(gotRotationMatrix && rotationMatrix != null)
            {
                SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationFlipped);
                SensorManager.getOrientation(rotationMatrix, orientation);
                SensorManager.getOrientation(rotationFlipped, orientationFlipped);
                checkAzimuth();
            }else if(rotationMatrix == null)
            {
                Log.d(TAG, "onSensorChanged: rotation is null");
            }else
            {
                Log.d(TAG, "onSensorChanged: did not get rotation");
            }


        }
    }

    private boolean isFlat()
    {
        if(orientation != null)
        {
            float pitch = (float)Math.toDegrees(orientation[1]);
//            Log.d(TAG, "isFlat: pitch: " + pitch);
            if(pitch < 20  && pitch > -45)
            {
                return true;
            }else
            {
                return false;
            }
        }
        return false;
    }


    public float updateOrientation(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            setGravity(event.values);
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            setGeomagnetic(event.values);
        }

        updateRotation();

        return azimuth;
    }

}
