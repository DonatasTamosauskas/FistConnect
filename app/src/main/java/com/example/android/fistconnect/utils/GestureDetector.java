package com.example.android.fistconnect.utils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.android.fistconnect.models.HitType;

public class GestureDetector implements SensorEventListener {

    private static final String TAG = "GestureDetector";

    private SensorManager mSensorManager;
    private HitType lastHitType;
    private newHitListener hitListener;

    private boolean hasSensors;
    private boolean isListeningForMove;

    public GestureDetector(SensorManager sensorManager) {
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
        //TODO: Vibrations
        //TODO: Timeout for punch
        isListeningForMove = true;
    }

    private void removeSensorListeners() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (!isListeningForMove) return;

        Sensor sensor = sensorEvent.sensor;
        boolean punchAllowed = true;

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