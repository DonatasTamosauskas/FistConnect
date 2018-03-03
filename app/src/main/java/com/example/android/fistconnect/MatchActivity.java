package com.example.android.fistconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private DatabaseReference myRef;
    private CurrentUser user;
    private Match match;
    private Player host;
    private Player guest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent getIntent=getIntent();
        Enemy enemy=(Enemy) getIntent.getSerializableExtra("match_information");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();
        myRef = FirebaseDatabase.getInstance().getReference("users");

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user =  snapshot.getValue(CurrentUser.class);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        host.setUserId(user.userID);
        host.setUsername(user.username);
        host.setAvatarID(user.avatarID);

        guest.setAvatarID(enemy.avatarID);
        guest.setUsername(enemy.username);
        guest.setUserId(enemy.userID);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

    }


}
