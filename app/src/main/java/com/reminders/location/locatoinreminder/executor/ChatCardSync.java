package com.reminders.location.locatoinreminder.executor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;
import com.reminders.location.locatoinreminder.database.entity.ReminderContact;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;
import com.reminders.location.locatoinreminder.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 20/1/18.
 */

public class ChatCardSync extends AsyncTask<Void, Void, Void> {
    SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    private Context context;
    private AppDatabase appDatabase;
    private List<ChatCardsEntity> chatCardsNew;
    private List<ChatCardsEntity> chatCardsOld;
    private List<ChatCardsEntity> chatCardsLocal;
    private DatabaseReference databaseReference;
    private String phone = "";
    private String sendTo = "";
    private String sendFrom = "";
    private String sendToName = "";


    public ChatCardSync(Context context, AppDatabase appDatabase, DatabaseReference databaseReference
            , String sender, String sendToName, String reciever, ArrayList<ChatCardsEntity> chatCardsLocal) {
        this.context = context;
        this.appDatabase = appDatabase;
        chatCardsOld = new ArrayList<>();
        chatCardsNew = new ArrayList<>();
        this.chatCardsLocal = chatCardsLocal;
        this.databaseReference = databaseReference;
        this.sendTo = sender;
        this.sendToName = sendToName;
        this.sendFrom = reciever;
        phone = new Utils().getFullNumber(context,sender) + reciever;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference reminder = databaseReference.child(phone);
        reminder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    ChatCardsEntity chatCardsEntity = post.getValue(ChatCardsEntity.class);
                    chatCardsEntity.getContactFetch().setContact_number(sendTo);
                    chatCardsEntity.setSendContact(sendFrom);
                    boolean checkResult = checkIfCardIsPresent(Integer.parseInt(post.getKey().trim()));
                    if (checkResult) {
                        chatCardsOld.add(chatCardsEntity);
                    } else {
                        chatCardsNew.add(chatCardsEntity);
                    }
                }
                if (ChatCardSync.this.getStatus() == Status.RUNNING) {

                } else {
                    AsyncTask.execute(() -> {
                        //update old cards
                        if (!chatCardsOld.isEmpty() || !(chatCardsOld == null)) {
                            for (ChatCardsEntity update : chatCardsOld)
                                appDatabase.cardDoa().updateCard(update);
                        }
                        //insert new cards
                        if (!chatCardsNew.isEmpty() || !(chatCardsNew == null)) {
                            for (ChatCardsEntity insert : chatCardsNew)
                                appDatabase.cardDoa().insertCard(insert);
                        }
                        updateReminderCount();

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    public boolean checkIfCardIsPresent(int key) {
        if (!chatCardsLocal.isEmpty() || !(chatCardsLocal == null)) {
            for (ChatCardsEntity ce : chatCardsLocal) {
                if (ce.getCardId() == key) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateReminderCount() {
        int countReminderCard = appDatabase.cardDoa().getContactCardCount(sendTo);
        if (countReminderCard >= 1) {
            //get reminder card count
            int reminderContactCount = appDatabase.reminderContactDoa().chatCardPresent(sendTo);
            if (reminderContactCount > 0) {
                setSharedValues(true, false, sendTo);
                //update contact card
                appDatabase.reminderContactDoa().updateChatCard(new ReminderContact(sendTo,
                        sendToName, countReminderCard, false, true, System.currentTimeMillis()));
            } else {
                setSharedValues(false, true, sendTo);
                //insert contact card
                appDatabase.reminderContactDoa().inserChatCard(new ReminderContact(sendTo,
                        sendToName, countReminderCard, false, true, System.currentTimeMillis()));
            }
        }

    }

    public void setSharedValues(boolean val1, boolean val2, String number) {
        sharedPreferenceSingleton.saveAs(context, ConstantVar.UPDATION, val1);
        sharedPreferenceSingleton.saveAs(context, ConstantVar.INSERTION, val2);
        sharedPreferenceSingleton.saveAs(context, ConstantVar.UPDATED_NUMBER, number);

    }

}
