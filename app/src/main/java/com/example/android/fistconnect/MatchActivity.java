package com.example.android.fistconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent getIntent=getIntent();
        Enemy enemy=(Enemy) getIntent.getSerializableExtra("match_information");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        TextView test=(TextView) findViewById(R.id.TestTest);
        TextView level=(TextView) findViewById(R.id.TestLevelTest);
        level.setText(enemy.level+"");
        test.setText(enemy.username);
    }
}
