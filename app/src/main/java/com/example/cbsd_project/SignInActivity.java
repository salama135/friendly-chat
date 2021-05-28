package com.example.cbsd_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cbsd_project.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final String TAG = "SignInActivity";
    EditText editTextEmail;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.activity_sign_in_editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.activity_sign_in_editTextPassword);

        Button buttonSignIn = (Button) findViewById(R.id.activity_sign_in_buttonSignIn);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkFields())
                {
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();

                    signIn(email, password);

                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Data - try again", Toast.LENGTH_SHORT).show();
                }
            }
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
//            Intent intent = new Intent(MainActivity.this, ViewUserActivity.class);
            Intent intent = new Intent(SignInActivity.this, ViewRoomActivity.class);
            startActivity(intent);

        }  // No user is signed in

        // [END check_current_user]
    }

    public boolean checkFields(){
        return !editTextEmail.getText().toString().isEmpty() &&
                !editTextPassword.getText().toString().isEmpty();
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            assert user != null;
                            String name = user.getDisplayName();
                            User currentUser = new User(name, user.getEmail(), user.getUid());
                            User.setCurrentUser(currentUser);

                            // go to home
//                            Intent intent = new Intent(SignInActivity.this, ViewUserActivity.class);
                            Intent intent = new Intent(SignInActivity.this, ViewRoomActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END sign_in_with_email]
    }
}