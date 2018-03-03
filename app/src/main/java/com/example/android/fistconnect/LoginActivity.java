package com.example.android.fistconnect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private int currentAvatarId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void register(View v) {
        EditText email = (EditText) findViewById(R.id.registration_email);
        String rEmail = email.getText().toString();

        EditText username = (EditText) findViewById(R.id.registration_username);
        final String rUserName = username.getText().toString();

        EditText password = (EditText) findViewById(R.id.registration_password);
        String rPassword = password.getText().toString();

        if (TextUtils.isEmpty(rEmail) || TextUtils.isEmpty(rUserName) || TextUtils.isEmpty(rPassword)) {
            Toast.makeText(this, "Please fill in empty fields", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(rEmail, rPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                currentUser = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(rUserName).build();

                                currentUser.updateProfile(profileUpdates);

                                // Adding a new user in database by UID
                                String userID = currentUser.getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                CurrentUser newCurrentUser = new CurrentUser(rUserName, 0, 0, userID);
                                databaseReference.child("users").child(userID).setValue(newCurrentUser);

                                updateUI();
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                        //Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
    }

    //Needed intent to change UI!!
    private void updateUI() {
        Toast.makeText(this, "Welcome, warrior!", Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(LoginActivity.this, ListActivity.class);
        LoginActivity.this.startActivity(loginIntent);
    }

    public void imageId1(View v) {
        currentAvatarId = 0;
    }

    public void imageId2(View v) {
        currentAvatarId = 1;
    }
}

