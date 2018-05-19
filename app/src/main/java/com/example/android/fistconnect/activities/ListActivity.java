package com.example.android.fistconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.fistconnect.R;
import com.example.android.fistconnect.adapters.EnemyObjectAdapter;
import com.example.android.fistconnect.models.Match;
import com.example.android.fistconnect.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private DatabaseReference usersReference;
    private DatabaseReference matchReference;
    private Match match;
    private User currentUser;
    private User enemy;
    private ListView enemyListView;
    private ArrayList<User> arrayOfEnemies;
    private ValueEventListener matchRequestListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setDatabaseReferences();
        setCurrentUser();
        setEnemies();
        setOnClickListenerForEnemyList();
        setListenerForMachRequest();

    }

    private void setDatabaseReferences() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        usersReference = FirebaseDatabase.getInstance().getReference("users");
        matchReference = FirebaseDatabase.getInstance().getReference("match");
    }

    private void setCurrentUser() {
        ArrayList<User> arrayOfUsers = new ArrayList<>();
        final EnemyObjectAdapter currentUserAdapter = new EnemyObjectAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.display_currentUser);
        listView.setAdapter(currentUserAdapter);

        String currentUserId = firebaseUser.getUid();

        usersReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                currentUserAdapter.add(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setEnemies() {
        arrayOfEnemies = new ArrayList<>();
        final EnemyObjectAdapter enemyAdapter = new EnemyObjectAdapter(this, arrayOfEnemies);
        enemyListView = (ListView) findViewById(R.id.display_enemies);
        enemyListView.setAdapter(enemyAdapter);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    enemy = ds.getValue(User.class);
                    if (!enemy.getUserID().equals(currentUser.getUserID())) {
                        enemyAdapter.add(enemy);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersReference.addListenerForSingleValueEvent(eventListener);
    }

    private void setOnClickListenerForEnemyList() {
        enemyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User challenger = arrayOfEnemies.get(position);
                Intent matchIntent = new Intent(ListActivity.this, MatchActivity.class);
                matchIntent.putExtra("enemy_information", challenger);
                startActivity(matchIntent);
            }
        });
    }

    private void setListenerForMachRequest() {
        matchRequestListener = matchReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if ((match = snapshot.getValue(Match.class)) != null) {
                    matchReference.child(firebaseUser.getUid()).removeEventListener(matchRequestListener);
                    matchReference.child(firebaseUser.getUid()).child("hasStarted").setValue(true);

                    Intent gameActivity = new Intent(ListActivity.this, GameActivity.class);
                    gameActivity.putExtra("match_information", match);
                    startActivity(gameActivity);
                    //Toast.makeText(ListActivity.this, "BLT KONNEKT", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
