package com.example.android.fistconnect.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private static final long GAME_OVER_VIBRATION_LENGTH = 800;
    private static final long HIT_MADE_VIBRATION_LENGTH = 300;
    private static final long YOUR_TURN_VIBRATION_LENGTH = 150;
    private static final long WAIT_BEFORE_LIST_ACTIVITY = 2500;

    private Match currentMatch;
    private String currentUserId;
    private String enemyId;
    private Boolean isFirstPunch;
    private LastPunch lastPunch;
    private GestureDetector gestureDetector;

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
        setTheme(R.style.AppTheme);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        currentMatch = (Match) getIntent().getSerializableExtra("match_information");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        createDatabaseReferences();
        setEnemyId();
        getViewElementReferences();
        initiateGestureDetector();

        makeFirstMoveIfNeeded();
        setListenerForNewPunch();
        setListenerForEndGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (punchListener != null) punchReference.removeEventListener(punchListener);
        if (gameOverListener != null) isOverReference.removeEventListener(gameOverListener);
        matchReference.removeValue();
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
        gestureDetector.startListeningForNextPunch();
    }

    public void punchButtonClick(View view) {
        checkIfPunchIsCorrect(HitType.PUNCH);
    }

    public void blockButtonClick(View view) {
        checkIfPunchIsCorrect(HitType.BLOCK);
    }

    public void lowBlowButtonClick(View view) {
        checkIfPunchIsCorrect(HitType.LOW_BLOW);
    }

    private void setLastPunchInDatabase(HitType hitType) {
        changeButtonClickability(false);
        yourTurnText.setVisibility(View.INVISIBLE);

        currentMatch.setLastPunch(new LastPunch(currentUserId, hitType));
        punchReference.setValue(currentMatch.getLastPunch());
    }

    private void checkIfPunchIsCorrect(HitType hitType) {
        if (isFirstPunch) {
            setLastPunchInDatabase(hitType);
            isFirstPunch = false;
            return;
        }

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
        Toast.makeText(this, "You lost!", Toast.LENGTH_SHORT).show();
        vibrateForTime(GAME_OVER_VIBRATION_LENGTH);
        isOverReference.setValue(true);
        gameOverRoutine();
    }

    private void gameOverRoutine() {
        isOverReference.removeEventListener(gameOverListener);
        punchReference.removeEventListener(punchListener);

        gameOverListener = null;
        punchListener = null;

        matchReference.removeValue();
        startListActivityAfterTime(WAIT_BEFORE_LIST_ACTIVITY);
    }

    private void setListenerForNewPunch() {
        punchListener = punchReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LastPunch incomingPunch = dataSnapshot.getValue(LastPunch.class);

                if (incomingPunch != null && !incomingPunch.getUserID().equals(currentUserId)) {
                    lastPunch = incomingPunch;
                    vibrateForTime(YOUR_TURN_VIBRATION_LENGTH);
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
                    Toast.makeText(GameActivity.this, "Game Over, you won!", Toast.LENGTH_SHORT).show();
                    gameOverRoutine();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void startListActivityAfterTime(long milliseconds) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent listActivityIntent = new Intent(GameActivity.this, ListActivity.class);
                startActivity(listActivityIntent);
            }
        }, milliseconds);
    }

    private void initiateGestureDetector() {
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        gestureDetector = new GestureDetector(sensorManager);

        gestureDetector.setHitListener(new GestureDetector.newHitListener() {
            @Override
            public void onHitMade(HitType hitMade) {
                vibrateForTime(HIT_MADE_VIBRATION_LENGTH);
                checkIfPunchIsCorrect(hitMade);
            }

            @Override
            public void onHitTimeout() {
                gameLostByCurrentPlayer();
            }
        });
    }

    private void vibrateForTime(long milliseconds) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(milliseconds);
        }
    }
}