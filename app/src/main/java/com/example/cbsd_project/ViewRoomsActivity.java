package com.example.cbsd_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.cbsd_project.adapters.RoomsAdapter;
import com.example.cbsd_project.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewRoomsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    final String TAG = "ViewRoomsActivity";

    RecyclerView listViewRooms;
    ArrayList<Room> rooms;
    RoomsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rooms);

        mDatabase = FirebaseDatabase.getInstance().getReference(Room.firebasePath);

        // Initialize rooms
        rooms = new ArrayList<Room>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                rooms.clear();
                for (DataSnapshot roomSnapshot: snapshot.getChildren()) {
                    Log.e("Get Data", roomSnapshot.getKey());

                    Room room = roomSnapshot.getValue(Room.class);
                    rooms.add(room);
                    Log.e("Get Data", room.getName());
                    Log.e("Get Data", String.valueOf(room.getRoomType()));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("The read failed: ", error.getDetails());
            }
        });

        Button buttonCreateRoom = (Button) findViewById(R.id.activity_view_rooms_buttonCreateRoom);

        buttonCreateRoom.setOnClickListener(v -> {
            Intent intent = new Intent(ViewRoomsActivity.this, CreateRoomActivity.class);
            startActivity(intent);
        });

        listViewRooms = (RecyclerView) findViewById(R.id.activity_view_rooms_listViewRooms);

        adapter = new RoomsAdapter(rooms);
        // Attach the adapter to the recyclerview to populate items
        listViewRooms.setAdapter(adapter);
        // Set layout manager to position the items
        listViewRooms.setLayoutManager(new LinearLayoutManager(this));
    }
}