package com.example.android.fistconnect;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameActivity extends AppCompatActivity {
    private Match currentMatch = new Match();

    private Player firstPlayer = new Player();
    private Player secondPlayer = new Player();

    private GestureDetector gestureDetector = new GestureDetector();
    private SensorManager mSensorManager;
    private boolean hasSensors;

    private boolean hasGestureHappened = false;

    private int moveMadeByFirstPlayer;
    private int moveMadeBySecondPlayer;

    private boolean peckingOrder = true;

    private boolean responseLoop = false;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //firstPlayer = currentMatch.getPlayer1();
        //secondPlayer = currentMatch.getPlayer2();

        initiateSensorsForGame();

        gameOn();

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
    }

    public void initiateSensorsForGame() {
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        gestureDetector.initiateSensors(mSensorManager, hasSensors);
        if (!hasSensors) {
            //Do something if sensors ar MIA
        }
    }

    public void gameOn() {

        while (firstPlayer.health != 0 || secondPlayer.health != 0) {

            if(peckingOrder) {
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
    }

    public void firstPunch(final Player firstPlayer) {
        TextView textView = (TextView) findViewById(R.id.player_one_go);
        textView.setText("Player one go!");

        new CountDownTimer(3000, 100) {
            public void onTick(long millisUntilFinished) {
                if(gestureDetector.hasGestureHappened()) {
                    if(gestureDetector.getGestureType() == 1) {
                        moveMadeByFirstPlayer = 1;
                    } else if(gestureDetector.getGestureType() == 2) {
                        moveMadeByFirstPlayer = 2;
                    } else {
                        moveMadeByFirstPlayer = 3;
                    }
                }
            }

            public void onFinish() {
                if(!gestureDetector.hasGestureHappened()) {
                    firstPlayer.setHealth(firstPlayer.getHealth() - 1);
                    peckingOrder = false;
                }
            }
        };
    }

    public void responsePunch(final Player secondPlayer) {
        TextView textView = (TextView) findViewById(R.id.player_one_go);
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
                }
            }

            public void onFinish() {
                if(!gestureDetector.hasGestureHappened()) {
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
    }

    public void determineIsMoveSuccesful() {

        if (moveMadeByFirstPlayer == 1) {
            if (moveMadeBySecondPlayer == 3) {
                responseLoop = true;
            } else {
                secondPlayer.setHealth(secondPlayer.getHealth() - 1);
                responseLoop = false;
            }
        }

        if (moveMadeByFirstPlayer == 2) {
            if (moveMadeBySecondPlayer == 1) {
                responseLoop = true;
            } else {
                secondPlayer.setHealth(secondPlayer.getHealth() - 1);
                responseLoop = false;
            }
        }

        if (moveMadeByFirstPlayer == 3) {
            if (moveMadeBySecondPlayer == 2) {
                responseLoop = true;
            } else {
                secondPlayer.setHealth(secondPlayer.getHealth() - 1);
                responseLoop = false;
            }
        }
    }
}
