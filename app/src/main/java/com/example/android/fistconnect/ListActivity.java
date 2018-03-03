package com.example.android.fistconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Create current user adapter
        ArrayList<Enemy> arrayOfUsers = new ArrayList<>();
        EnemyObjectAdapter adapter = new EnemyObjectAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.display_currentUser);
        listView.setAdapter(adapter);

        Enemy e = new Enemy();
        adapter.add(e);

        //Create adapter for enemies
        final ArrayList<Enemy> arrayOfEnemies = new ArrayList<>();
        EnemyObjectAdapter enemyAdapter = new EnemyObjectAdapter(this, arrayOfEnemies);
        ListView enemyListView = (ListView) findViewById(R.id.display_enemies);
        enemyListView.setAdapter(enemyAdapter);

        for (int i = 0; i < 10; i++) {
            Enemy f = new Enemy();
            enemyAdapter.add(f);
        }
        enemyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Enemy item = arrayOfEnemies.get(position);
                Intent matchIntent = new Intent(ListActivity.this, MatchActivity.class);
               // matchIntent.putExtra("conv_information", item);
                startActivity(matchIntent);
            }
        });
    }


}
