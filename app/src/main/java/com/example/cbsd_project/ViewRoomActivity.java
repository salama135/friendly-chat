package com.example.cbsd_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbsd_project.adapters.MessagesAdapter;
import com.example.cbsd_project.helpers.Constants;
import com.example.cbsd_project.helpers.MessagesDBHelper;
import com.example.cbsd_project.helpers.ThemeUtil;
import com.example.cbsd_project.models.Message;
import com.example.cbsd_project.models.Room;
import com.example.cbsd_project.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ViewRoomActivity extends AppCompatActivity implements LocationListener {

    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    final String TAG = "ViewRoomActivity";

    RecyclerView listViewMessages;
    ArrayList<Message> messages;
    MessagesAdapter adapter;
    EditText editTextMessage;

    MessagesDBHelper messagesDBHelper;

    private static final String INPUT_CONTENT_INFO_KEY = "COMMIT_CONTENT_INPUT_CONTENT_INFO";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "COMMIT_CONTENT_FLAGS";

    private InputContentInfoCompat mCurrentInputContentInfo;
    private int mCurrentFlags;

    LocationManager locationManager;

    public static final int PICK_PHOTO = 1;
    Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeUtil.setTheme(this);

        setContentView(R.layout.activity_view_room);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Room.firebasePath);

        messagesDBHelper = new MessagesDBHelper(getApplicationContext());

        mDatabase = mDatabase.child(Room.getCurrentRoom().getRoomID()).child(Message.firebasePath);

        // Initialize messages
        messages = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                messages.clear();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    Log.e("Get Data", messageSnapshot.getKey());

                    Message message = messageSnapshot.getValue(Message.class);
                    messages.add(message);
                    Log.e("Get Data", message.getContent());
                    Log.e("Get Data", message.getSender());
                    Log.e("Get Data", message.getMessageType());

                    if (message.getSender().equals(User.getCurrentUser().getName())) {
                        message.setMessageViewType(Constants.MessageViewTypeSender);
                    } else {
                        message.setMessageViewType(Constants.MessageViewTypeReceiver);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("The read failed: ", error.getDetails());
            }
        });

        listViewMessages = (RecyclerView) findViewById(R.id.activity_view_room_listViewMessages);

        adapter = new MessagesAdapter(messages);
        // Attach the adapter to the recyclerview to populate items
        listViewMessages.setAdapter(adapter);

        // Set layout manager to position the items
        listViewMessages.setLayoutManager(new LinearLayoutManager(this));


        editTextMessage = createEditTextWithContentMimeTypes(
                new String[]{"image/png", "image/gif", "image/jpeg", "image/webp"});

        final LinearLayout layout =
                (LinearLayout) findViewById(R.id.activity_view_room_linearLayout);

        // This declares that the IME can commit contents with
        // InputConnectionCompat#commitContent() if they match "image/png", "image/gif",
        // "image/jpeg", or "image/webp".
        layout.addView(editTextMessage, 2);

        if (savedInstanceState != null) {
            final InputContentInfoCompat previousInputContentInfo = InputContentInfoCompat.wrap(
                    savedInstanceState.getParcelable(INPUT_CONTENT_INFO_KEY));
            final int previousFlags = savedInstanceState.getInt(COMMIT_CONTENT_FLAGS_KEY);
            if (previousInputContentInfo != null) {
                onCommitContentInternal(previousInputContentInfo, previousFlags);
            }
        }

        ImageView buttonSend = (ImageView) findViewById(R.id.activity_view_room_buttonSend);

        buttonSend.setOnClickListener(v -> {
            if (checkFields()) {
                String messageContent = editTextMessage.getText().toString();

                String messageID = mDatabase.push().getKey();

                writeNewMessage(messageID,
                        messageContent,
                        User.getCurrentUser().getName(),
                        Constants.MessageTypeText,
                        Constants.MessageViewTypeSender);

                editTextMessage.setText("");

                Toast.makeText(getApplicationContext(), "message Added", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonEditRoom = (Button) findViewById(R.id.activity_view_room_buttonEditRoom);

        buttonEditRoom.setOnClickListener(v -> {
            Intent intent = new Intent(ViewRoomActivity.this, ChangeThemeActivity.class);
            startActivity(intent);
        });

        Button buttonViewPinnedMessages = (Button)
                findViewById(R.id.activity_view_room_buttonViewPinnedMessages);

        buttonViewPinnedMessages.setOnClickListener(v -> {
            Intent intent = new Intent(ViewRoomActivity.this, ShowPinnedMessagesActivity.class);
            startActivity(intent);
        });

        Button buttonSearchChat = (Button)
                findViewById(R.id.activity_view_room_buttonSearchChat);

        buttonSearchChat.setOnClickListener(v -> {
            Intent intent = new Intent(ViewRoomActivity.this, SearchChatActivity.class);
            startActivity(intent);
        });

        ImageView imageViewShareLocation = (ImageView)
                findViewById(R.id.activity_view_room_imageViewShareLocation);

        if (ContextCompat.checkSelfPermission(ViewRoomActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ViewRoomActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        imageViewShareLocation.setOnClickListener(v -> {
               String address =  getLocation();

               if(address != null){
                   Log.e("address", address);

                   String messageID = mDatabase.push().getKey();

                   writeNewMessage(messageID,
                           address,
                           User.getCurrentUser().getName(),
                           Constants.MessageTypeText,
                           Constants.MessageViewTypeSender);

                   Toast.makeText(getApplicationContext(), "Location Shared",
                           Toast.LENGTH_SHORT).show();
               }
        });

        ImageView imageViewSharePhoto = (ImageView)
                findViewById(R.id.activity_view_room_imageViewSharePhoto);

        imageViewSharePhoto.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO);
        });
    }

    private void uploadImage(Bitmap bitmap) {
        if(imageURI != null)
        {
            String messageID = mDatabase.push().getKey();
            StorageReference ref = storageReference.child("images/"+ messageID);

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
                    Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                    writeNewMessage(messageID,
                            downloadUri.toString(),
                            User.getCurrentUser().getName(),
                            Constants.MessageTypeImage,
                            Constants.MessageViewTypeSender);
                } else {
                    // Handle failures
                    Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_PHOTO && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                uploadImage(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private String getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,ViewRoomActivity.this);
            Geocoder geocoder = new Geocoder(ViewRoomActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(
                    locationManager.getLastKnownLocation(
                            LocationManager.PASSIVE_PROVIDER).getLatitude(),
                    locationManager.getLastKnownLocation(
                            LocationManager.PASSIVE_PROVIDER).getLongitude(),1);

            return addresses.get(0).getAddressLine(0);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onRestart() {
        ThemeUtil.setTheme(this);
        super.onRestart();
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    // menu item select listener
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        Log.e("item", String.valueOf(item.getGroupId()));

        Message selectedMessage = messages.get(item.getGroupId());

        int id = item.getItemId();

        if (id == Constants.MessageMenuPin) {
            messagesDBHelper.insertNewPinnedMessage(
                    Room.getCurrentRoom().getRoomID(),
                    selectedMessage.getSender(),
                    selectedMessage.getContent(),
                    selectedMessage.getMessageType(),
                    selectedMessage.getMessageViewType());

            return true;
//            case Constants.MessageMenuEdit:
//
//                return true;
//            case Constants.MessageMenuDelete:
//
//                return true;
        }

        return super.onContextItemSelected(item);
    }

    private boolean checkFields() {
        return !editTextMessage.getText().toString().isEmpty();
    }

    public void writeNewMessage(String messageID, String content, String sender, String messageType, String messageViewType) {
        Message message = new Message(content, sender, messageType, messageViewType);
        mDatabase.child(messageID).setValue(message);
    }

    private boolean onCommitContent(InputContentInfoCompat inputContentInfo, int flags,
                                    String[] contentMimeTypes) {
        // Clear the temporary permission (if any).  See below about why we do this here.
        try {
            if (mCurrentInputContentInfo != null) {
                mCurrentInputContentInfo.releasePermission();
            }
        } catch (Exception e) {
            Log.e(TAG, "InputContentInfoCompat#releasePermission() failed.", e);
        } finally {
            mCurrentInputContentInfo = null;
        }


        boolean supported = false;
        for (final String mimeType : contentMimeTypes) {
            if (inputContentInfo.getDescription().hasMimeType(mimeType)) {
                supported = true;
                break;
            }
        }
        if (!supported) {
            return false;
        }

        return onCommitContentInternal(inputContentInfo, flags);
    }

    private boolean onCommitContentInternal(InputContentInfoCompat inputContentInfo, int flags) {
        if ((flags & InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
            try {
                inputContentInfo.requestPermission();
            } catch (Exception e) {
                Log.e(TAG, "InputContentInfoCompat#requestPermission() failed.", e);
                return false;
            }
        }

        String messageID = mDatabase.push().getKey();

        String messageContent = inputContentInfo.getLinkUri().toString();
        writeNewMessage(messageID,
                messageContent,
                User.getCurrentUser().getName(),
                Constants.MessageTypeGif,
                Constants.MessageViewTypeSender);

        // Due to the asynchronous nature of WebView, it is a bit too early to call
        // inputContentInfo.releasePermission() here. Hence we call IC#releasePermission() when this
        // method is called next time.  Note that calling IC#releasePermission() is just to be a
        // good citizen. Even if we failed to call that method, the system would eventually revoke
        // the permission sometime after inputContentInfo object gets garbage-collected.
        mCurrentInputContentInfo = inputContentInfo;
        mCurrentFlags = flags;

        return true;
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle savedInstanceState) {
        if (mCurrentInputContentInfo != null) {
            savedInstanceState.putParcelable(INPUT_CONTENT_INFO_KEY,
                    (Parcelable) mCurrentInputContentInfo.unwrap());
            savedInstanceState.putInt(COMMIT_CONTENT_FLAGS_KEY, mCurrentFlags);
        }
        mCurrentInputContentInfo = null;
        mCurrentFlags = 0;
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Creates a new instance of {@link EditText} that is configured to specify the given content
     * MIME types to EditorInfo#contentMimeTypes so that developers can locally test how the current
     * input method behaves for such content MIME types.
     *
     * @param contentMimeTypes A {@link String} array that indicates the supported content MIME
     *                         types
     * @return a new instance of {@link EditText}, which specifies EditorInfo#contentMimeTypes with
     * the given content MIME types
     */
    private EditText createEditTextWithContentMimeTypes(String[] contentMimeTypes) {
        final String[] mimeTypes;  // our own copy of contentMimeTypes.
        if (contentMimeTypes == null || contentMimeTypes.length == 0) {
            mimeTypes = new String[0];
        } else {
            mimeTypes = Arrays.copyOf(contentMimeTypes, contentMimeTypes.length);
        }
        EditText exitText = new androidx.appcompat.widget.AppCompatEditText(this) {
            @Override
            public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
                final InputConnection ic = super.onCreateInputConnection(editorInfo);
                EditorInfoCompat.setContentMimeTypes(editorInfo, mimeTypes);
                final InputConnectionCompat.OnCommitContentListener callback =
                        (inputContentInfo, flags, opts) -> ViewRoomActivity.this.onCommitContent(
                                inputContentInfo, flags, mimeTypes);
                return InputConnectionCompat.createWrapper(ic, editorInfo, callback);
            }
        };
        exitText.setGravity(Gravity.CENTER);
        exitText.setPadding(10, 10, 10, 10);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( 0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        layoutParams.weight = 1;
        exitText.setLayoutParams(layoutParams);

        exitText.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edit_text_shadow));
        exitText.setHint("Say Something...");
        exitText.setTextColor(Color.WHITE);
        exitText.setHintTextColor(Color.WHITE);
        return exitText;
    }
}