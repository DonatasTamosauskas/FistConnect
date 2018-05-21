package com.example.android.fistconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.fistconnect.R;
import com.example.android.fistconnect.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstLoginInfoActivity extends AppCompatActivity {

    private static final int BEGINNER_LEVEL = 0;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Integer currentAvatarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login_info);

        createDatabaseConnections();
    }

    private void createDatabaseConnections() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewUser(View view) {
        EditText usernameEditText = findViewById(R.id.registration_username);
        String username = usernameEditText.getText().toString();
        String userID = firebaseUser.getUid();

        if (TextUtils.isEmpty(username) || currentAvatarId == null) {
            if (currentAvatarId == null)
                Toast.makeText(this, "Please select an avatar", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "Please choose a username", Toast.LENGTH_SHORT).show();

        } else {
            User newUserInfo = new User(username, BEGINNER_LEVEL, currentAvatarId, userID);
            databaseReference.child("users").child(userID).setValue(newUserInfo);

            startListActivity();
        }

    }

    private void startListActivity() {
        Intent listActivityIntent = new Intent(FirstLoginInfoActivity.this, ListActivity.class);
        startActivity(listActivityIntent);
    }

    public void imageId1(View v) {
        currentAvatarId = 0;

        LinearLayout linearLayout = findViewById(R.id.main_image);
        linearLayout.setVisibility(LinearLayout.GONE);

        LinearLayout secondLayout = findViewById(R.id.first_image);
        secondLayout.setVisibility(LinearLayout.VISIBLE);
    }

    public void imageId2(View v) {
        currentAvatarId = 1;

        LinearLayout linearLayout = findViewById(R.id.main_image);
        linearLayout.setVisibility(LinearLayout.GONE);

        LinearLayout secondLayout = findViewById(R.id.second_image);
        secondLayout.setVisibility(LinearLayout.VISIBLE);
    }
}
