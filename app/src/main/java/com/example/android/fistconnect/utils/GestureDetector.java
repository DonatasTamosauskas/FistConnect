package com.example.android.fistconnect.utils;

/**
 * Created by julius on 2018-03-04.
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by julius on 2018-03-04.
 */

public class GestureDetector implements SensorEventListener {

    public static boolean IsGestureDetectorAllowed;
    public static int detectedGestureType;
    private SensorManager mSensorManager;
    private Sensor GravitySensor;
    private Sensor LinearAcceleration;
    private boolean punchAllowed;
    private static final String TAG = "Accelerometer message";
    private static boolean hasMoveHappened = false;


    public void initiateSensors(SensorManager newSensorManager, boolean hasSensors) {
        mSensorManager = newSensorManager;
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null && mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            LinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            GravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            mSensorManager.registerListener(this, LinearAcceleration, mSensorManager.SENSOR_DELAY_FASTEST);
            mSensorManager.registerListener(this, GravitySensor, mSensorManager.SENSOR_DELAY_FASTEST);
            hasSensors = true;
        } else {
            hasSensors = false;
        }

    }

    public int getGestureType() {
        return detectedGestureType;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_GRAVITY) {
            if (sensorEvent.values[2] < 0) {
                punchAllowed = true;
            } else {
                punchAllowed = false;
            }

        }
        /*if(sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){
            // Log.i(TAG, "X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);
        }*/

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[0] > 15 && sensorEvent.values[1] < 5 && sensorEvent.values[2] < 5 && punchAllowed == true) {
            detectedGestureType = 1;
            Log.i(TAG, "123 Punch");
            hasMoveHappened = true;
            mSensorManager.unregisterListener(this);
        }

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[0] > 15 && sensorEvent.values[1] < 5 && sensorEvent.values[2] < 5 && punchAllowed == false) {
            detectedGestureType = 2;
            Log.i(TAG, "123 Low blow");
            hasMoveHappened = true;
            mSensorManager.unregisterListener(this);
        }

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[1] > 15 && sensorEvent.values[0] < 10 && sensorEvent.values[2] < 10) {
            detectedGestureType = 3;
            Log.i(TAG, "123 Block");
            hasMoveHappened = true;
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public boolean hasGestureHappened() {
        if (hasMoveHappened) {
            hasMoveHappened = false;
            return true;
        } else {
            return false;
        }
    }
}