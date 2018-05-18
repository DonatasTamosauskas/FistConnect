package com.example.android.fistconnect.activities;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.fistconnect.R;
import com.example.android.fistconnect.models.HitType;
import com.example.android.fistconnect.models.LastPunch;
import com.example.android.fistconnect.models.Match;
import com.example.android.fistconnect.utils.GestureDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;
    private SensorManager mSensorManager;
    private boolean hasSensors;
    private int moveMadeByFirstPlayer;
    private int moveMadeBySecondPlayer;
    private Boolean hasFailed = false;


    private Match currentMatch;
    private String currentUserId;
    private String enemyId;
    private Boolean isFirstPunch;
    private LastPunch lastPunch;

    private DatabaseReference databaseReference;
    private DatabaseReference matchReference;
    private DatabaseReference punchReference;
    private DatabaseReference isOverReference;
    private ValueEventListener punchListener;
    private ValueEventListener gameOverListener;

    private TextView yourTurnText;
    private Button punchButton;
    private Button blockButton;
    private Button lowBlowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        currentMatch = (Match) getIntent().getSerializableExtra("match_information");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        createDatabaseReferences();
        setEnemyId();
        getViewElementReferences();

        //initiateSensorsForGame();
        //gameOn();

        makeFirstMoveIfNeeded();
        setListenerForNewPunch();
        //setListenerForEndGame();
    }

    private void createDatabaseReferences() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        matchReference = databaseReference.child("match").child(currentMatch.getPlayer2().getUserId());
        punchReference = databaseReference.child("match").child(currentMatch.getPlayer2().getUserId()).child("lastPunch");
        isOverReference = databaseReference.child("match").child(currentMatch.getPlayer2().getUserId()).child("isOver");
    }

    private void setEnemyId() {
        if (currentUserId.equals(currentMatch.getPlayer1().getUserId())) {
            enemyId = currentMatch.getPlayer2().getUserId();
        } else {
            enemyId = currentMatch.getPlayer1().getUserId();
        }
    }

    private void getViewElementReferences() {
        yourTurnText = findViewById(R.id.your_turn_text);
        punchButton = findViewById(R.id.punch_button);
        blockButton = findViewById(R.id.block_button);
        lowBlowButton = findViewById(R.id.low_blow_button);

        changeButtonClickability(false);
    }

    private void changeButtonClickability(boolean enabled) {
        punchButton.setEnabled(enabled);
        blockButton.setEnabled(enabled);
        lowBlowButton.setEnabled(enabled);
    }

    private void makeFirstMoveIfNeeded() {
        if (!currentMatch.getPlayer1().getUserId().equals(enemyId)) {
            isFirstPunch = true;
            recordPunch();
        } else {
            isFirstPunch = false;
        }
    }

    private void recordPunch() {
        yourTurnText.setVisibility(View.VISIBLE);
        changeButtonClickability(true);
    }

    public void punchButtonClick(View view) {
        changeButtonClickability(false);
        yourTurnText.setVisibility(View.INVISIBLE);

        if (isFirstPunch) setLastPunchInDatabase(HitType.PUNCH);
        else checkIfPunchIsCorrect(HitType.PUNCH);
    }

    public void blockButtonClick(View view) {
        changeButtonClickability(false);
        yourTurnText.setVisibility(View.INVISIBLE);

        if (isFirstPunch) setLastPunchInDatabase(HitType.BLOCK);
        else checkIfPunchIsCorrect(HitType.BLOCK);
    }

    public void lowBlowButtonClick(View view) {
        changeButtonClickability(false);
        yourTurnText.setVisibility(View.INVISIBLE);

        if (isFirstPunch) setLastPunchInDatabase(HitType.LOW_BLOW);
        else checkIfPunchIsCorrect(HitType.LOW_BLOW);
    }

    private void setLastPunchInDatabase(HitType hitType) {
        currentMatch.setLastPunch(new LastPunch(currentUserId, hitType));
        matchReference.child("lastPunch").setValue(currentMatch.getLastPunch());
    }

    private void checkIfPunchIsCorrect(HitType hitType) {
        if (lastPunch.getUserRole() == HitType.PUNCH) {
            if (hitType != HitType.BLOCK) gameLostByCurrentPlayer();
            else setLastPunchInDatabase(hitType);

        } else if (lastPunch.getUserRole() == HitType.BLOCK) {
            if (hitType != HitType.LOW_BLOW) gameLostByCurrentPlayer();
            else setLastPunchInDatabase(hitType);

        } else {
            if (hitType != HitType.PUNCH) gameLostByCurrentPlayer();
            else setLastPunchInDatabase(hitType);
        }
    }

    private void gameLostByCurrentPlayer() {
        matchReference.child("lastPunch").removeEventListener(punchListener);
    }

    private void setListenerForNewPunch() {
        punchListener = matchReference.child("lastPunch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LastPunch incomingPunch = dataSnapshot.getValue(LastPunch.class);

                if (incomingPunch != null && !incomingPunch.getUserID().equals(currentUserId)) {
                    lastPunch = incomingPunch;
                    recordPunch();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setListenerForEndGame() {
        gameOverListener = isOverReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isOver = dataSnapshot.getValue(Boolean.class);

                if (isOver != null && isOver) {
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

        if (gestureDetector.hasGestureHappened()) {
            Toast.makeText(this, "Gesture happened", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "nohapeno", Toast.LENGTH_SHORT).show();
        }

    }


    public void responsePunch() {
//        TextView textView = (TextView) findViewById(R.id.player_one_go);
//        textView.setText("Player two go!");

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