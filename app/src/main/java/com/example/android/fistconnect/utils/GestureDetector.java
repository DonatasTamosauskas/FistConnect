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

/*
imported from game activity:

    private GestureDetector gestureDetector;
    private SensorManager mSensorManager;
    private boolean hasSensors;
    private int moveMadeByFirstPlayer;
    private int moveMadeBySecondPlayer;
    private Boolean hasFailed = false;




    //    public void initiateSensorsForGame() {
//        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
//
//        gestureDetector.initiateSensors(mSensorManager, hasSensors);
//        if (!hasSensors) {
//            //Do something if sensors ar MIA
//        }
//    }
//
//
//    public void firstPunch() {
//        /*TextView textView = (TextView) findViewById(R.id.player_one_go);
//        textView.setText("Player one go!");
//
//        new CountDownTimer(3000, 100) {
//            public void onTick(long millisUntilFinished) {
//                if (gestureDetector.hasGestureHappened()) {
//                    if (gestureDetector.getGestureType() == 1) {
//                        moveMadeByFirstPlayer = 1;
//                    } else if (gestureDetector.getGestureType() == 2) {
//                        moveMadeByFirstPlayer = 2;
//                    } else {
//                        moveMadeByFirstPlayer = 3;
//                    }
//
//                    currentMatch.hasPunched = true;
//                    Toast.makeText(GameActivity.this, "First Punch made", Toast.LENGTH_SHORT).show();
//                }
//                Toast.makeText(GameActivity.this, "no made", Toast.LENGTH_SHORT).show();
//            }
//
//            public void onFinish() {
//                //firstPunch();
//            }
//        };*/
//        gestureDetector = new GestureDetector();
//        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
//        gestureDetector.initiateSensors(mSensorManager, hasSensors);
//
//        if (gestureDetector.hasGestureHappened()) {
//            Toast.makeText(this, "Gesture happened", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "nohapeno", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//
//    public void responsePunch() {
////        TextView textView = (TextView) findViewById(R.id.player_one_go);
////        textView.setText("Player two go!");
//
//        new CountDownTimer(3000, 100) {
//            public void onTick(long millisUntilFinished) {
//                if (gestureDetector.hasGestureHappened()) {
//                    if (gestureDetector.getGestureType() == 1) {
//                        moveMadeBySecondPlayer = 1;
//                    } else if (gestureDetector.getGestureType() == 2) {
//                        moveMadeBySecondPlayer = 2;
//                    } else {
//                        moveMadeBySecondPlayer = 3;
//                    }
//                }
//            }
//
//            public void onFinish() {
//                if (!gestureDetector.hasGestureHappened()) {
//
//                    hasFailed = true;
//                } else {
//                    determineIsMoveSuccesful();
//                }
//            }
//        };
//    }
//
//    public void determineIsMoveSuccesful() {
//
//        if (moveMadeByFirstPlayer == 1) {
//            if (moveMadeBySecondPlayer == 3) {
//                hasFailed = false;
//            } else {
//                hasFailed = true;
//            }
//        }
//
//        if (moveMadeByFirstPlayer == 2) {
//            if (moveMadeBySecondPlayer == 1) {
//                hasFailed = false;
//            } else {
//                hasFailed = true;
//            }
//        }
//
//        if (moveMadeByFirstPlayer == 3) {
//            if (moveMadeBySecondPlayer == 2) {
//                hasFailed = false;
//            } else {
//                hasFailed = true;
//            }
//        }
//    }
//}
    /*public void gameOn() {

        while (firstPlayer.health != 0 || secondPlayer.health != 0) {

            if (peckingOrder) {
                firstPunch(firstPlayer);
                if (!peckingOrder) {

                    //Send first move to database.
                    LastPunch lastPunch = new LastPunch(firstPlayer, moveMadeByFirstPlayer);

                    databaseReference.child("match").child(currentMatch.getPlayer2().getUserId()).child("lastPunch").setValue(lastPunch);
                    //Fetch first move from database.


                    //Response punch.
                    responsePunch(secondPlayer);
                }

            } else {
                firstPunch(secondPlayer);
                if (!peckingOrder) {
                    responsePunch(firstPlayer);
                }
            }
        }
    }*/

    /*public void responsePunch(final Player secondPlayer) {
        TextView textView = (TextView) findViewById(R.id.player_one_go);
        textView.setText("Player two go!");

        new CountDownTimer(3000, 100) {
            public void onTick(long millisUntilFinished) {
                if (gestureDetector.hasGestureHappened()) {
                    if (gestureDetector.getGestureType() == 1) {
                        moveMadeBySecondPlayer = 1;
                    } else if (gestureDetector.getGestureType() == 2) {
                        moveMadeBySecondPlayer = 2;
                    } else {
                        moveMadeBySecondPlayer = 3;
                    }
                }
            }

            public void onFinish() {
                if (!gestureDetector.hasGestureHappened()) {
                    secondPlayer.setHealth(secondPlayer.getHealth() - 1);
                    peckingOrder = false;
                } else {
                    determineIsMoveSuccesful();
                    if (responseLoop) {
                        moveMadeByFirstPlayer = moveMadeBySecondPlayer;
                        responsePunch(firstPlayer);
                    }
                }
            }
        };
    }*/

    /*
        while (firstPlayer.health != 0 || secondPlayer.health != 0) {
            TextView textView = (TextView) findViewById(R.id.player_one_go);
            textView.setText("Player one go!");

            new CountDownTimer(3000, 100) {
                public void onTick(long millisUntilFinished) {
                    if(gestureDetector.hasGestureHappened()) {
                        if(gestureDetector.getGestureType() == 1) {
                            moveMadeByLastPlayer = 1;
                        } else if(gestureDetector.getGestureType() == 2) {
                            moveMadeByLastPlayer = 2;
                        } else {
                            moveMadeByLastPlayer = 3;
                        }
                    }
                }

                public void onFinish() {
                    if(!gestureDetector.hasGestureHappened()) {
                        firstPlayer.health--;
                    }
                }
            };

            textView.setText("Player two go!");

            new CountDownTimer(3000, 100) {
                public void onTick(long millisUntilFinished) {
                    if(gestureDetector.hasGestureHappened()) {
                        if(gestureDetector.getGestureType() == 1) {
                            moveMadeBySecondPlayer = 1;
                        } else if(gestureDetector.getGestureType() == 2) {
                            moveMadeBySecondPlayer = 2;
                        } else {
                            moveMadeBySecondPlayer = 3;
                        }

                        //Function to decide what to do.
                        //punchLowBlowBlock();
                    }
                }

                public void onFinish() {
                    if(!gestureDetector.hasGestureHappened()) {
                        secondPlayer.health--;
                    }
                }
            };
        }
 */