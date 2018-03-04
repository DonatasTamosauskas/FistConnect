package com.example.android.fistconnect;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

    private EnemyObjectAdapter adapter;

    private EnemyObjectAdapter enemyAdapter;
    ListView enemyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Create current user adapter
        ArrayList<Enemy> arrayOfUsers = new ArrayList<>();
        adapter = new EnemyObjectAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.display_currentUser);
        listView.setAdapter(adapter);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();
        usersReference = FirebaseDatabase.getInstance().getReference("users");

        usersReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(CurrentUser.class);
                adapter.add(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Create adapter for enemies
        final ArrayList<Enemy> arrayOfEnemies = new ArrayList<>();
        enemyAdapter = new EnemyObjectAdapter(this, arrayOfEnemies);
        enemyListView = (ListView) findViewById(R.id.display_enemies);
        enemyListView.setAdapter(enemyAdapter);

        usersReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = usersReference.child("users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    enemy = ds.getValue(Enemy.class);
                    if (enemy.userID != user.userID) {
                        enemyAdapter.add(enemy);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        usersdRef.addListenerForSingleValueEvent(eventListener);

        enemyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Enemy challenger = arrayOfEnemies.get(position);
                Intent matchIntent = new Intent(ListActivity.this, MatchActivity.class);
                matchIntent.putExtra("match_information", challenger);
                startActivity(matchIntent);
            }
        });

        matchReference = FirebaseDatabase.getInstance().getReference("match");

        matchReference.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if ((match = snapshot.getValue(Match.class)) != null){
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
