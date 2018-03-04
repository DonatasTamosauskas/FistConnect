package com.example.android.fistconnect;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {
    private Match currentMatch;

    private Player firstPlayer;
    private Player secondPlayer;

    private GestureDetector gestureDetector;
    private SensorManager mSensorManager;
    private boolean hasSensors;

    private boolean hasGestureHappened = false;

    private int moveMadeByFirstPlayer;
    private int moveMadeBySecondPlayer;
    private boolean peckingOrder = true;
    private boolean responseLoop = false;
    private Boolean hasFailed = false;
    private String currentUserId;
    private String enemyId;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference databasePunchReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent getIntent = getIntent();
        currentMatch = (Match) getIntent.getSerializableExtra("match_information");

        Toast.makeText(this, currentMatch.getPlayer2().getUserId(), Toast.LENGTH_SHORT).show();

        databasePunchReference = databaseReference.child("match").child(currentMatch.getPlayer2().getUserId()).child("lastPunch");
        DatabaseReference isOverRef = databaseReference.child("match").child(currentMatch.getPlayer2().getUserId()).child("isOver");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (currentUserId.equals(currentMatch.getPlayer1().getUserId())) {
            enemyId = currentMatch.getPlayer2().getUserId();
        } else {
            enemyId = currentMatch.getPlayer1().getUserId();
        }

        //initiateSensorsForGame();

        //gameOn();

        // This is the Beginning player
        if (currentMatch.player1.getUserId() == currentUserId) {
            firstPunch();
            LastPunch lastPunch = new LastPunch(currentUserId, moveMadeByFirstPlayer);
            currentMatch.lastPunch = lastPunch;
            databaseReference.child("match").child(currentMatch.getPlayer2().getUserId()).setValue(currentMatch);
        }


        databaseReference.child("match").child(currentMatch.getPlayer2().getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue(Match.class).hasPunched && !dataSnapshot.getValue(Match.class).lastPunch.userID.equals(currentUserId)) {
                        responsePunch();
                        if (hasFailed) {
                            currentMatch.setIsOver(true);
                            currentMatch.setWinnerId(enemyId);
                            DatabaseReference matchPositionRef = FirebaseDatabase.getInstance().getReference().child("match").child(currentMatch.getPlayer2().getUserId());
                            matchPositionRef.setValue(currentMatch);
                        } else {
                            LastPunch lastPunch = new LastPunch(currentUserId, moveMadeByFirstPlayer);
                            databasePunchReference.setValue(lastPunch);
                        }
                    }
                } catch (NullPointerException ex){
                    Toast.makeText(GameActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        isOverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class)) {
                    Toast.makeText(GameActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

    public void initiateSensorsForGame() {
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        gestureDetector.initiateSensors(mSensorManager, hasSensors);
        if (!hasSensors) {
            //Do something if sensors ar MIA
        }
    }


    public void firstPunch() {
        /*TextView textView = (TextView) findViewById(R.id.player_one_go);
        textView.setText("Player one go!");

        new CountDownTimer(3000, 100) {
            public void onTick(long millisUntilFinished) {
                if (gestureDetector.hasGestureHappened()) {
                    if (gestureDetector.getGestureType() == 1) {
                        moveMadeByFirstPlayer = 1;
                    } else if (gestureDetector.getGestureType() == 2) {
                        moveMadeByFirstPlayer = 2;
                    } else {
                        moveMadeByFirstPlayer = 3;
                    }

                    currentMatch.hasPunched = true;
                    Toast.makeText(GameActivity.this, "First Punch made", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(GameActivity.this, "no made", Toast.LENGTH_SHORT).show();
            }

            public void onFinish() {
                //firstPunch();
            }
        };*/
        gestureDetector = new GestureDetector();
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        gestureDetector.initiateSensors(mSensorManager, hasSensors);

        if(gestureDetector.hasGestureHappened()){
            Toast.makeText(this, "Gesture happened", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "nohapeno", Toast.LENGTH_SHORT).show();
        }

    }


    public void responsePunch() {
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

                    hasFailed = true;
                } else {
                    determineIsMoveSuccesful();
                }
            }
        };
    }

    public void determineIsMoveSuccesful() {

        if (moveMadeByFirstPlayer == 1) {
            if (moveMadeBySecondPlayer == 3) {
                hasFailed = false;
            } else {
                hasFailed = true;
            }
        }

        if (moveMadeByFirstPlayer == 2) {
            if (moveMadeBySecondPlayer == 1) {
                hasFailed = false;
            } else {
                hasFailed = true;
            }
        }

        if (moveMadeByFirstPlayer == 3) {
            if (moveMadeBySecondPlayer == 2) {
                hasFailed = false;
            } else {
                hasFailed = true;
            }
        }
    }
}
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