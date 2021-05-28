package com.example.cbsd_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.cbsd_project.helpers.Constants;
import com.example.cbsd_project.models.Room;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRoomActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    final String TAG = "CreateRoomActivity";

    EditText editTextRoomName;
    Switch aSwitchIsPrivate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        mDatabase = FirebaseDatabase.getInstance().getReference();

//        mDatabase.child("users").child(userId).child("username").setValue(name);


        editTextRoomName = (EditText) findViewById(R.id.activity_create_room_editTextRoomName);
        aSwitchIsPrivate = (Switch) findViewById(R.id.activity_create_room_switchIsPrivate);

        Button buttonCreateRoom = (Button) findViewById(R.id.activity_create_room_buttonCreateRoom);

        buttonCreateRoom.setOnClickListener(v -> {

            if(checkFields()){
                String roomName = editTextRoomName.getText().toString();
                boolean isPrivate = aSwitchIsPrivate.isChecked();

                String roomID = mDatabase.push().getKey();

                writeNewRoom(roomID, roomName, isPrivate);

                Toast.makeText(getApplicationContext(), "Room Added", Toast.LENGTH_SHORT).show();

                super.onBackPressed();
            }
        });
    }

    public boolean checkFields(){
        return !editTextRoomName.getText().toString().isEmpty();
    }

    public void writeNewRoom(String roomID, String name, boolean isPrivate) {

        String roomType = (isPrivate)? Constants.RoomTypePrivate : Constants.RoomTypePublic;
        Room room = new Room(name, roomType, roomID);
        mDatabase.child(Room.firebasePath).child(roomID).setValue(room);
    }
}