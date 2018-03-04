package com.example.android.fistconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent getIntent = getIntent();
        Match currentMatch = (Match) getIntent.getSerializableExtra("match_information");
    }
}
