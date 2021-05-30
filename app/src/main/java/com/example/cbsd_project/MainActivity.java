package com.example.cbsd_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cbsd_project.helpers.ThemeUtil;
import com.example.cbsd_project.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeUtil.setTheme(this);

        setContentView(R.layout.activity_main);

        Button buttonSignUp = (Button) findViewById(R.id.activity_main_buttonSignUp);

        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        Button buttonSignIn = (Button) findViewById(R.id.activity_main_buttonSignIn);

        buttonSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        checkCurrentUser();
    }

    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null ) {
            // User is signed in

            String name = user.getDisplayName();

            Toast.makeText(getApplicationContext(), "Welcome " + name, Toast.LENGTH_LONG).show();


            User currentUser = new User(name, user.getEmail(), user.getUid());
            User.setCurrentUser(currentUser);

            // go to home
            Intent intent = new Intent(MainActivity.this, ViewRoomsActivity.class);
            startActivity(intent);

        }  // No user is signed in

        // [END check_current_user]
    }
}