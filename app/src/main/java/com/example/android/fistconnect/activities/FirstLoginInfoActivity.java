package com.example.android.fistconnect.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.fistconnect.R;

public class FirstLoginInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login_info);
    }
}
