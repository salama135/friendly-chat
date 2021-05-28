package com.example.cbsd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cbsd_project.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    final String TAG = "SignUpActivity";

    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextName = (EditText) findViewById(R.id.activity_sign_up_editTextName);
        editTextEmail = (EditText) findViewById(R.id.activity_sign_up_editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.activity_sign_up_editTextPassword);

        Button buttonSignUp = (Button) findViewById(R.id.activity_sign_up_buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkFields())
                {
                    String name = editTextName.getText().toString();
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();

                    createAccount(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Data - try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkFields(){
        return !editTextName.getText().toString().isEmpty() &&
                !editTextEmail.getText().toString().isEmpty() &&
                !editTextPassword.getText().toString().isEmpty();
    }

    private void createAccount(String name, String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(getApplicationContext(), "User Added Successfully", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = mAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        assert user != null;
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                });

                        // add to DB
//                            addAccountToDB(name, email, password, "");
                        writeNewUser(user.getUid(), name, email);

                        // go to home
                        Intent intent = new Intent(SignUpActivity.this, ViewUserActivity.class);
                        startActivity(intent);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        // [END create_user_with_email]
    }

    public void writeNewUser(String userID, String name, String email) {
        User currentUser = new User(name, email, userID);

        User.setCurrentUser(currentUser);

        mDatabase.child(User.firebasePath).child(userID).setValue(currentUser);
    }

//    private void addAccountToDB(String name, String email, String password, String photoUri){
//
//        // Create a new user with a first and last name
//        Map<String, Object> user = new HashMap<>();
//        user.put("name", name);
//        user.put("email", email);
//        user.put("password", password);
//        user.put("photoUri", photoUri);
//
//        String userPath = email.split("@", 2)[0];
//
//        db.collection("users").document(userPath)
//                .set(user)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                        Toast.makeText(getApplicationContext(), "failed to add doc",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}