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
import com.example.android.fistconnect.models.CurrentUser;
import com.example.android.fistconnect.models.Enemy;
import com.example.android.fistconnect.models.Match;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private DatabaseReference usersReference;
    private DatabaseReference matchReference;
    private CurrentUser user;
    private Match match;
    private Enemy enemy;
    private ListView enemyListView;
    private ArrayList<Enemy> arrayOfEnemies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        usersReference = FirebaseDatabase.getInstance().getReference("users");
        matchReference = FirebaseDatabase.getInstance().getReference("match");
    }

    private void setCurrentUser() {
        ArrayList<Enemy> arrayOfUsers = new ArrayList<>();
        final EnemyObjectAdapter currentUserAdapter = new EnemyObjectAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.display_currentUser);
        listView.setAdapter(currentUserAdapter);

        String currentUserId = currentUser.getUid();

        usersReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(CurrentUser.class);
                currentUserAdapter.add(user);
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
                    enemy = ds.getValue(Enemy.class);
                    if (!enemy.userID.equals(user.userID)) {
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
                Enemy challenger = arrayOfEnemies.get(position);
                Intent matchIntent = new Intent(ListActivity.this, MatchActivity.class);
                matchIntent.putExtra("enemy_information", challenger);
                startActivity(matchIntent);
            }
        });
    }

    private void setListenerForMachRequest() {
        matchReference.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if ((match = snapshot.getValue(Match.class)) != null) {
                    matchReference.child(currentUser.getUid()).child("hasStarted").setValue(true);

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
