package com.example.cbsd_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbsd_project.ViewRoomActivity;
import com.example.cbsd_project.ViewRoomsActivity;
import com.example.cbsd_project.helpers.Constants;
import com.example.cbsd_project.CreateRoomActivity;
import com.example.cbsd_project.R;
import com.example.cbsd_project.models.Room;

import java.util.List;

public class RoomsAdapter extends
        RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView textViewRoomName;
        public TextView textViewIsPrivate;
        public Button buttonViewRoom;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            textViewRoomName = (TextView) itemView.findViewById(R.id.item_room_textViewRoomName);
            textViewIsPrivate = (TextView) itemView.findViewById(R.id.item_room_textViewIsPrivate);
            buttonViewRoom = (Button) itemView.findViewById(R.id.item_room_buttonViewRoom);
        }
    }

    // ... view holder defined above...

    // Store a member variable for the contacts
    private List<Room> mRooms;

    private Context context;

    // Pass in the contact array into the constructor
    public RoomsAdapter(List<Room> rooms) {
        mRooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_room, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Room room = mRooms.get(position);

        // Set item views based on your views and data model
        TextView textViewRoomName = holder.textViewRoomName;
        textViewRoomName.setText(room.getName());
        TextView textViewRoomType = holder.textViewIsPrivate;

        switch (room.getRoomType()){
            case Constants.RoomTypePrivate:
                textViewRoomType.setText(R.string.room_type_private);
                break;
            case Constants.RoomTypePublic:
                textViewRoomType.setText(R.string.room_type_public);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + room.getRoomType());
        }

        Button buttonViewRoom = holder.buttonViewRoom;
        buttonViewRoom.setOnClickListener(v -> {

            Room.setCurrentRoom(room);

            Intent intent = new Intent(context, ViewRoomActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }
}

