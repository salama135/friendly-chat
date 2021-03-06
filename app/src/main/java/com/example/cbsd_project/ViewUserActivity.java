package com.example.cbsd_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cbsd_project.helpers.ThemeUtil;
import com.example.cbsd_project.models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class ViewUserActivity extends AppCompatActivity {

    private StorageReference storageReference;

    final String TAG = "ViewUserActivity";
    public static final int PICK_PHOTO_FOR_AVATAR = 1;

    EditText editTextName;
    EditText editTextEmail;
    ImageView imageViewUserPhoto;
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeUtil.setTheme(this);

        setContentView(R.layout.activity_view_user);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        editTextName = (EditText) findViewById(R.id.activity_view_user_editTextName);
        editTextEmail = (EditText) findViewById(R.id.activity_view_user_editTextEmail);

        Button buttonUpdateUserAvatar = (Button) findViewById(R.id.activity_view_user_buttonUpdateUserAvatar);
        Button buttonUpdateInfo = (Button) findViewById(R.id.activity_view_user_buttonUpdateInfo);
        Button buttonSignOut = (Button) findViewById(R.id.activity_view_user_buttonSignOut);
        Button buttonUploadPhoto = (Button) findViewById(R.id.buttonUploadPhoto);

        imageViewUserPhoto = (ImageView) findViewById(R.id.activity_view_user_imageViewUserPhoto);

        buttonUpdateUserAvatar.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO_FOR_AVATAR);
        });

        buttonUpdateInfo.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            updateProfile(name, imageURI.toString());
        });

        buttonSignOut.setOnClickListener(v -> {
            signOut();
            // go to main
            Intent intent = new Intent(ViewUserActivity.this, MainActivity.class);
            startActivity(intent);
        });

        buttonUploadPhoto.setOnClickListener(v -> {
            uploadImage();
        });

        checkCurrentUser();
    }

    private void uploadImage() {
        if(imageURI != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ User.getCurrentUser().getUserID());

            imageViewUserPhoto.setDrawingCacheEnabled(true);
            imageViewUserPhoto.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageViewUserPhoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);

            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    imageURI = downloadUri;
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle failures
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                imageViewUserPhoto.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
//            Toast.makeText(getApplicationContext(), "Already SignedIn", Toast.LENGTH_LONG).show();

            getUserProfile();

        } else {
            // No user is signed in
            Toast.makeText(getApplicationContext(), "Not SignedIn", Toast.LENGTH_LONG).show();

            // go to main
            Intent intent = new Intent(ViewUserActivity.this, MainActivity.class);
            startActivity(intent);
        }
        // [END check_current_user]
    }

    public void getUserProfile() {
        // [START get_user_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            if (photoUrl != null) {
                // Download directly from StorageReference using Glide
                Glide.with(this /* context */)
                        .load(photoUrl.toString())
                        .into(imageViewUserPhoto);
            }

            editTextName.setText(name);
            editTextEmail.setText(email);

            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
//            String uid = user.getUid();
        }
        // [END get_user_profile]
    }

    public void updateProfile(String name, String photoUri) {
        // [START update_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(photoUri))
                .build();

        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "User profile updated.");
                    }
                });
        // [END update_profile]
    }

    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }
}