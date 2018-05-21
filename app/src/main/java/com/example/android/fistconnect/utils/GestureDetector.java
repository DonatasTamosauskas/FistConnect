package com.example.android.fistconnect.utils;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import com.example.android.fistconnect.activities.GameActivity;
import com.example.android.fistconnect.activities.ListActivity;
import com.example.android.fistconnect.models.HitType;

public class GestureDetector implements SensorEventListener {

    private static final String TAG = "GestureDetector";
    private static final long HIT_TIMEOUT_TIME = 3000;

    private SensorManager mSensorManager;
    private HitType lastHitType;
    private newHitListener hitListener;

    boolean punchAllowed;
    private boolean hasSensors;
    private boolean isListeningForMove;

    public GestureDetector(SensorManager sensorManager) {
        punchAllowed = true;
        hitListener = null;
        initiateSensors(sensorManager); // mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
    }

    private void initiateSensors(SensorManager newSensorManager) {
        mSensorManager = newSensorManager;

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null && mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {

            Sensor linearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            Sensor gravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

            mSensorManager.registerListener(this, linearAcceleration, mSensorManager.SENSOR_DELAY_FASTEST);
            mSensorManager.registerListener(this, gravitySensor, mSensorManager.SENSOR_DELAY_FASTEST);
            hasSensors = true;

        } else {
            hasSensors = false;
        }
    }

    public void startListeningForNextPunch() {
        startHitTimeoutTimer(HIT_TIMEOUT_TIME);
        isListeningForMove = true;
    }

    public void removeSensorListeners() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (!isListeningForMove) return;

        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_GRAVITY) {
            punchAllowed = (sensorEvent.values[2] < 0);
        }

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[0] > 15 && sensorEvent.values[1] < 5 && sensorEvent.values[2] < 5 && punchAllowed) {
            Log.e(TAG, "123 Punch");
            isListeningForMove = false;
            hitListener.onHitMade(HitType.PUNCH);

        } else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[0] > 15 && sensorEvent.values[1] < 5 && sensorEvent.values[2] < 5 && !punchAllowed) {
            Log.e(TAG, "123 Low blow");
            isListeningForMove = false;
            hitListener.onHitMade(HitType.LOW_BLOW);

        } else if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && sensorEvent.values[1] > 15 && sensorEvent.values[0] < 10 && sensorEvent.values[2] < 10) {
            Log.e(TAG, "123 Block");
            isListeningForMove = false;
            hitListener.onHitMade(HitType.BLOCK);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void startHitTimeoutTimer(long milliseconds) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isListeningForMove) hitListener.onHitTimeout();
                isListeningForMove = false;
            }
        }, milliseconds);
    }

    public interface newHitListener {
        void onHitMade(HitType hitMade);

        void onHitTimeout();
    }

    public HitType getLastHitType() {
        return lastHitType;
    }

    public void setLastHitType(HitType lastHitType) {
        this.lastHitType = lastHitType;
    }

    public boolean isHasSensors() {
        return hasSensors;
    }

    public void setHasSensors(boolean hasSensors) {
        this.hasSensors = hasSensors;
    }

    public boolean isListeningForMove() {
        return isListeningForMove;
    }

    public void setListeningForMove(boolean listeningForMove) {
        isListeningForMove = listeningForMove;
    }

    public newHitListener getHitListener() {
        return hitListener;
    }

    public void setHitListener(newHitListener hitListener) {
        this.hitListener = hitListener;
    }

    public static long getHitTimeoutTime() {
        return HIT_TIMEOUT_TIME;
    }
}