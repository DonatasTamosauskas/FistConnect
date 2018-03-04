package com.example.android.fistconnect;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private CurrentUser user = new CurrentUser();
    private Match match = new Match();
    private Player host = new Player();
    private Player guest = new Player();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Intent getIntent = getIntent();
        Enemy enemy = (Enemy) getIntent.getSerializableExtra("enemy_information");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();
        /*myRef = FirebaseDatabase.getInstance().getReference("users");

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user =  snapshot.getValue(CurrentUser.class);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/


        host.setUserId(currentUser.getUid());
        host.setUsername(currentUser.getDisplayName());

        guest.setUsername(enemy.username);
        guest.setUserId(enemy.userID);
        match.setPlayer1(host);
        match.setWinnerId(host.getUserId());
        match.setPlayer2(guest);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("match").child(guest.getUserId());
        databaseReference.setValue(match);
        Toast.makeText(this, guest.getUsername(), Toast.LENGTH_SHORT).show();
        databaseReference.child("hasStarted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue(Boolean.class)) {
                    Intent matchIntent = new Intent(MatchActivity.this, GameActivity.class);
                    matchIntent.putExtra("match_information", match);
                    startActivity(matchIntent);
                    //Toast.makeText(MatchActivity.this, "something something dark side", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}
