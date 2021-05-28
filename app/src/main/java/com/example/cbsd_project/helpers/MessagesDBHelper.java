package com.example.cbsd_project.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessagesDBHelper extends SQLiteOpenHelper {

    private static String databaseName = "chatDatabase";
    SQLiteDatabase chatDatabase;

    public MessagesDBHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table messages(" +
                "messageID integer primary key autoincrement, " +
                "roomID text not null, " +
                "sender text not null, " +
                "content text not null, " +
                "messageType text not null, " +
                "messageViewType text not null" +
                ")");

        db.execSQL("create table pinned_messages(" +
                "pinnedMessageID integer primary key autoincrement, " +
                "messageID text, " +
                "FOREIGN KEY(messageID) REFERENCES messages (messageID)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists messages");
        db.execSQL("drop table if exists pinned_messages");
        onCreate(db);
    }


    public void insertNewMessage(String roomID, String sender, String content,
                                 String messageType, String messageViewType){
        chatDatabase = getWritableDatabase();

        ContentValues newMessage = new ContentValues();
        newMessage.put("roomID", roomID);
        newMessage.put("sender", sender);
        newMessage.put("content", content);
        newMessage.put("messageType", messageType);
        newMessage.put("messageViewType", messageViewType);

        chatDatabase.insert("messages", null, newMessage);
    }

    public void insertNewPinnedMessage(String messageID){
        chatDatabase = getWritableDatabase();

        ContentValues newPinnedMessage = new ContentValues();
        newPinnedMessage.put("messageID", messageID);

        chatDatabase.insert("pinned_messages", null, newPinnedMessage);
    }

    public Cursor getMessages(String roomID) {
        chatDatabase = getReadableDatabase();
        String MY_QUERY = "select * from messages where roomID = " + roomID + "";
        Cursor cursor = chatDatabase.rawQuery(MY_QUERY, null );
        if (cursor != null){
            cursor.moveToFirst();
        }

        chatDatabase.close();
        return cursor;
    }

    public Cursor getPinnedMessages() {
        chatDatabase = getReadableDatabase();
        String MY_QUERY = "SELECT " +
                "messages.sender, " +
                "messages.content, " +
                "messages.messageType, " +
                "messages.messageViewType " +
                "FROM messages INNER JOIN pinned_messages " +
                "ON messages.roomID = pinned_messages.roomID";

        Cursor cursor = chatDatabase.rawQuery(MY_QUERY, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        chatDatabase.close();
        return cursor;
    }
}
