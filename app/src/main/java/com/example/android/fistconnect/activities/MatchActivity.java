package com.example.android.fistconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.fistconnect.R;
import com.example.android.fistconnect.models.User;
import com.example.android.fistconnect.models.Match;
import com.example.android.fistconnect.models.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchActivity extends AppCompatActivity {

    private DatabaseReference currentMatchReference;
    private ValueEventListener matchHasStartedListener;
    private FirebaseUser currentUser;
    private User enemy;

    private Match match;
    private Player host;
    private Player guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        enemy = (User) getIntent().getSerializableExtra("enemy_information");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        setMachInformation();
        setViewText();
        addListenerForMatchHasStartedEvent();
    }

    private void setMachInformation() {
        match = new Match();
        host = new Player();
        guest = new Player();

        host.setUserId(currentUser.getUid());
        host.setUsername(currentUser.getDisplayName());

        guest.setUsername(enemy.getUsername());
        guest.setUserId(enemy.getUserID());

        match.setPlayer1(host);
        match.setPlayer2(guest);

        currentMatchReference = FirebaseDatabase.getInstance().getReference().child("match").child(guest.getUserId());
        currentMatchReference.setValue(match);
    }

    private void setViewText() {
        TextView enemyUsername = findViewById(R.id.match_loading_player_name);
        enemyUsername.setText(enemy.getUsername());
    }

    private void addListenerForMatchHasStartedEvent() {
        matchHasStartedListener = currentMatchReference.child("hasStarted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean hasStarted = dataSnapshot.getValue(Boolean.class);

                if (hasStarted != null && hasStarted) {
                    currentMatchReference.child("hasStarted").removeEventListener(matchHasStartedListener);

                    Intent matchIntent = new Intent(MatchActivity.this, GameActivity.class);
                    matchIntent.putExtra("match_information", match);
                    startActivity(matchIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
