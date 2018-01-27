package com.reminders.location.locatoinreminder.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.constants.ReminderConstants;
import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;
import com.reminders.location.locatoinreminder.pojo.ContactFetch;
import com.reminders.location.locatoinreminder.util.Utils;

import java.util.List;

/**
 * Created by ayush on 24/1/18.
 */

public class ServerService extends IntentService {
    DatabaseReference databaseReference;

    public ServerService() {
        super(ConstantVar.INTENT_SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent i) {
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantVar.REMINDERS);
        databaseReference.keepSynced(true);
        switch (getInt(i, ConstantVar.INTENT_SERVICE_CHOICE)) {
            case ConstantVar.INSERTCHAT:
                insertIntoFireBaseDB(getInt(i, ReminderConstants.CARD_ID),
                        getString(i, ReminderConstants.CARD_TITLE),
                        getString(i, ReminderConstants.SENDTO_NUMBER),
                        getString(i, ReminderConstants.SENDFROM_NUMBER),
                        getString(i, ReminderConstants.SENDFROM_NUMBER),
                        getString(i, ReminderConstants.NOTES_DATA),
                        getString(i, ReminderConstants.LOCATION),
                        getInt(i, ReminderConstants.COLOR),
                        getString(i, ReminderConstants.TIME),
                        getLong(i, ReminderConstants.EDIT_TIME)
                );
                break;
            case ConstantVar.UPDATECHAT:
                insertIntoFireBaseDB(getInt(i, ReminderConstants.CARD_ID),
                        getString(i, ReminderConstants.CARD_TITLE),
                        getString(i, ReminderConstants.SENDTO_NUMBER),
                        getString(i, ReminderConstants.SENDFROM_NUMBER),
                        getString(i, ReminderConstants.SENDFROM_NUMBER),
                        getString(i, ReminderConstants.NOTES_DATA),
                        getString(i, ReminderConstants.LOCATION),
                        getInt(i, ReminderConstants.COLOR),
                        getString(i, ReminderConstants.TIME),
                        getLong(i, ReminderConstants.EDIT_TIME)
                );
                break;
            case ConstantVar.DELETECHAT:
                deleteChatCards(
                        getIntList(i, ConstantVar.CARD_IDS_DELETION),
                        getString(i, ReminderConstants.SENDFROM_NUMBER),
                        getString(i, ReminderConstants.SENDTO_NUMBER)
                );
                break;
            case ConstantVar.DELETECONTACT:
                deleteWholeChat(
                        getString(i, ReminderConstants.SENDFROM_NUMBER),
                        getString(i, ReminderConstants.SENDTO_NUMBER)
                );
                break;
        }


    }

    void insertIntoFireBaseDB(int cardID, String cardTitle, String sendToNum, String sendToName, String sendFromNum, String notesData, String location, int color, String time, long editTime) {
        ContactFetch contactFetch = new ContactFetch(sendToNum, sendToName);
        databaseReference.child(serverChatID1(sendFromNum, sendToNum)).child(cardID + "").setValue(getChatEntitity(cardID, cardTitle, contactFetch, sendFromNum, notesData, location, color, time, editTime));

    }


    void deleteChatCards(List<Integer> cardIds, String sendFrom, String sendTo) {
        DatabaseReference r2 = FirebaseDatabase.getInstance().getReference(ConstantVar.REMINDERS).child(serverChatID2(sendTo, sendFrom));
        r2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    if (cardIds.contains(Integer.parseInt(post.getKey()))) {
                        post.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteWholeChat(String sendFrom, String sendTo) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    if (post.getKey().equalsIgnoreCase(serverChatID2(sendTo, sendFrom))) {
                        post.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    ChatCardsEntity getChatEntitity(int cardID, String cardTitle, ContactFetch contactFetch, String sendFromNum, String notesData, String location, int color, String time, long editTime) {
        return new ChatCardsEntity(cardID, cardTitle, contactFetch, sendFromNum, notesData, location, color, time, false, false, editTime);
    }

    String getString(Intent intent, String key) {
        return intent.getStringExtra(key);
    }

    int getInt(Intent intent, String key) {
        return intent.getIntExtra(key, 0);
    }

    long getLong(Intent intent, String key) {
        return intent.getLongExtra(key, 0);
    }

    List<Integer> getIntList(Intent intent, String key) {
        return intent.getIntegerArrayListExtra(key);
    }

    String serverChatID1(String s1, String s2) {
        return s1 + new Utils().getFullNumber(s2);
    }

    String serverChatID2(String s1, String s2) {
        return new Utils().getFullNumber(s1) + s2;
    }

}

