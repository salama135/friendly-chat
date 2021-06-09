package com.example.cbsd_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cbsd_project.adapters.MessagesAdapter;
import com.example.cbsd_project.helpers.Constants;
import com.example.cbsd_project.helpers.ThemeUtil;
import com.example.cbsd_project.models.Message;
import com.example.cbsd_project.models.Room;
import com.example.cbsd_project.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchChatActivity extends AppCompatActivity {
    DatabaseReference mDatabase;

    RecyclerView messagesListView;

    EditText search_textbox;
    Button search_btn;

    ArrayList<Message> messages;
    ArrayList<Message> messagesTemp;
    MessagesAdapter messageArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.setTheme(this);
        setContentView(R.layout.activity_search_chat);

        search_textbox = findViewById(R.id.activity_search_chat_txtbox);
        search_btn = findViewById(R.id.activity_search_searchbtn);
        messagesListView = (RecyclerView) findViewById(R.id.activity_search_listViewMessages);

        mDatabase = FirebaseDatabase.getInstance().getReference(Room.firebasePath);
        mDatabase = mDatabase.child(Room.getCurrentRoom().getRoomID()).child(Message.firebasePath);
        messages = new ArrayList<>();
        messagesTemp = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot messageSnapShot : snapshot.getChildren()) {
                    Message message = messageSnapShot.getValue(Message.class);
                    messages.add(message);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);
        messageArrayAdapter = new MessagesAdapter(messagesTemp);
        messagesListView.setAdapter(messageArrayAdapter);
        messagesListView.setLayoutManager(new LinearLayoutManager(this));

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagesTemp.clear();
                if (search_textbox.getText().toString().length() == 0){
                    messageArrayAdapter.notifyDataSetChanged();
                    return;
                }
                for(Message m : messages){
                    if(m.getContent().contains(search_textbox.getText().toString()) && m.getMessageType().trim().equals(Constants.MessageTypeText)){
                        if (m.getSender().equals(User.getCurrentUser().getName())) {
                            m.setMessageViewType(Constants.MessageViewTypeSender);
                        } else {
                            m.setMessageViewType(Constants.MessageViewTypeReceiver);
                        }
                        messagesTemp.add(m);
                    }
                }
                messageArrayAdapter.notifyDataSetChanged();
            }
        });
    }
}