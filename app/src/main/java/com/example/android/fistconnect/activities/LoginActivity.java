package com.example.android.fistconnect.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.fistconnect.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    public MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);

        startMusic();
        launchFirebaseUiLogin();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (musicPlayer != null) startMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        musicPlayer.stop();
    }

    private void startMusic() {
        musicPlayer = MediaPlayer.create(this, R.raw.main_theme_reg_fin);
        musicPlayer.setLooping(true);
        musicPlayer.start();
    }

    private void launchFirebaseUiLogin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_launcher_new)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                checkFirstTimeRegistrationAndProceedLogin(user);

            } else {
                displayConnectionErrorMessage();
            }
        }
    }

    private void checkFirstTimeRegistrationAndProceedLogin(FirebaseUser user) {
        DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        usersDatabaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) proceedToListActivity();
                else proceedToFirstLoginActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                displayConnectionErrorMessage();
            }
        });
    }

    private void proceedToFirstLoginActivity() {
        Toast.makeText(this, "FirstLoginAct", Toast.LENGTH_SHORT).show();
        musicPlayer.stop();
        Intent firstLoginActivity = new Intent(LoginActivity.this, FirstLoginInfoActivity.class);
        startActivity(firstLoginActivity);
    }

    private void proceedToListActivity() {
        Toast.makeText(this, "ListAct", Toast.LENGTH_SHORT).show();
        musicPlayer.stop();
        Intent listActivity = new Intent(LoginActivity.this, ListActivity.class);
        startActivity(listActivity);
    }

    private void displayConnectionErrorMessage() {
        Toast.makeText(this, "Error has occurred, please try again.", Toast.LENGTH_SHORT).show();
    }
}

