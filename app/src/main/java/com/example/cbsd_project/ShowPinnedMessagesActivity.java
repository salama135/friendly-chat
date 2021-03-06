package com.example.cbsd_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.cbsd_project.adapters.MessagesAdapter;
import com.example.cbsd_project.helpers.Constants;
import com.example.cbsd_project.helpers.MessagesDBHelper;
import com.example.cbsd_project.models.Message;
import com.example.cbsd_project.models.Room;
import com.example.cbsd_project.models.User;

import java.util.ArrayList;

public class ShowPinnedMessagesActivity extends AppCompatActivity {

    MessagesDBHelper messagesDBHelper;
    RecyclerView listViewMessages;
    ArrayList<Message> messages;
    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pinned_messages);

        listViewMessages = (RecyclerView) findViewById(R.id.activity_show_pinned_messages_listView);

        messages = new ArrayList<Message>();

        adapter = new MessagesAdapter(messages);
        // Attach the adapter to the recyclerview to populate items
        listViewMessages.setAdapter(adapter);

        // Set layout manager to position the items
        listViewMessages.setLayoutManager(new LinearLayoutManager(this));

        messagesDBHelper = new MessagesDBHelper(getApplicationContext());

        Cursor cursor = messagesDBHelper.getPinnedMessages(Room.getCurrentRoom().getRoomID());

        while(!cursor.isAfterLast()){
            Message message = new Message(
                    cursor.getString(cursor.getColumnIndex("content")),
                    cursor.getString(cursor.getColumnIndex("sender")),
                    cursor.getString(cursor.getColumnIndex("messageType")),
                    cursor.getString(cursor.getColumnIndex("messageViewType")));

            Log.e("Get Pinned Message", message.getContent());

            if (message.getSender().equals(User.getCurrentUser().getName())) {
                message.setMessageViewType(Constants.MessageViewTypeSender);
            } else {
                message.setMessageViewType(Constants.MessageViewTypeReceiver);
            }

            messages.add(message);

            cursor.moveToNext();
        }
        adapter.notifyDataSetChanged();
    }
}