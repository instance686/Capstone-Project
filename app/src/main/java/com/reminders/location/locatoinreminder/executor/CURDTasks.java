package com.reminders.location.locatoinreminder.executor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.constants.ReminderConstants;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ChatCardsEntity;
import com.reminders.location.locatoinreminder.database.entity.ReminderContact;
import com.reminders.location.locatoinreminder.service.ServerService;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 23/1/18.
 */

public class CURDTasks extends AsyncTask<Void, Void, Void> {

    private AppDatabase appDatabase;
    private int choice;
    private ChatCardsEntity chatCardsEntity;
    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();
    private Context context;
    private List<Integer> cardIDs = new ArrayList<>();
    private String name;
    private String chatID;


    public CURDTasks(AppDatabase appDatabase, int choice, String chatID, Context context) {
        this.appDatabase = appDatabase;
        this.chatID = chatID;
        this.choice = choice;
        this.context = context;
    }

    public CURDTasks(AppDatabase appDatabase, int choice, ChatCardsEntity chatCardsEntity, Context context) {
        this.appDatabase = appDatabase;
        this.choice = choice;
        this.chatCardsEntity = chatCardsEntity;
        this.context = context;
    }

    public CURDTasks(AppDatabase appDatabase, int choice, Context context, List<Integer> cardIDs, String chatID, String name) {
        this.chatID = chatID;
        this.appDatabase = appDatabase;
        this.choice = choice;
        this.context = context;
        this.cardIDs = cardIDs;
        this.name = name;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        switch (choice) {
            case ConstantVar.INSERTCHAT:
                insertReminder();
                break;
            case ConstantVar.UPDATECHAT:
                updateReminder();
                break;
            case ConstantVar.DELETECHAT:
                deleteReminder();
                break;
            case ConstantVar.INSERTCONTACT:
                insertContact();
                break;
            case ConstantVar.UPDATECONACT:
                updateContact();
                break;
            case ConstantVar.DELETECONTACT:
                deleteContact();
                break;

        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent serviceIntent = new Intent(context, ServerService.class);
        serviceIntent.putExtra(ConstantVar.INTENT_SERVICE_CHOICE, choice);
        switch (choice) {
            case ConstantVar.INSERTCHAT:
                serviceIntent.putExtra(ReminderConstants.CARD_ID, chatCardsEntity.getCardId());
                serviceIntent.putExtra(ReminderConstants.CARD_TITLE, chatCardsEntity.getCardTitle());
                serviceIntent.putExtra(ReminderConstants.SENDTO_NUMBER, chatCardsEntity.getContactFetch().getContact_number());
                serviceIntent.putExtra(ReminderConstants.SENDTO_NAME, chatCardsEntity.getContactFetch().getContact_name());
                serviceIntent.putExtra(ReminderConstants.SENDFROM_NUMBER, chatCardsEntity.getSendContact());
                serviceIntent.putExtra(ReminderConstants.NOTES_DATA, chatCardsEntity.getNotes());
                serviceIntent.putExtra(ReminderConstants.LOCATION, chatCardsEntity.getLocation());
                serviceIntent.putExtra(ReminderConstants.COLOR, chatCardsEntity.getColor());
                serviceIntent.putExtra(ReminderConstants.TIME, chatCardsEntity.getTime());
                serviceIntent.putExtra(ReminderConstants.EDIT_TIME, chatCardsEntity.getEditMilliseconds());
                break;
            case ConstantVar.UPDATECHAT:
                serviceIntent.putExtra(ReminderConstants.CARD_ID, chatCardsEntity.getCardId());
                serviceIntent.putExtra(ReminderConstants.CARD_TITLE, chatCardsEntity.getCardTitle());
                serviceIntent.putExtra(ReminderConstants.SENDTO_NUMBER, chatCardsEntity.getContactFetch().getContact_number());
                serviceIntent.putExtra(ReminderConstants.SENDTO_NAME, chatCardsEntity.getContactFetch().getContact_name());
                serviceIntent.putExtra(ReminderConstants.SENDFROM_NUMBER, chatCardsEntity.getSendContact());
                serviceIntent.putExtra(ReminderConstants.NOTES_DATA, chatCardsEntity.getNotes());
                serviceIntent.putExtra(ReminderConstants.LOCATION, chatCardsEntity.getLocation());
                serviceIntent.putExtra(ReminderConstants.COLOR, chatCardsEntity.getColor());
                serviceIntent.putExtra(ReminderConstants.TIME, chatCardsEntity.getTime());
                serviceIntent.putExtra(ReminderConstants.EDIT_TIME, chatCardsEntity.getEditMilliseconds());
                break;
            case ConstantVar.DELETECHAT:
                serviceIntent.putExtra(ReminderConstants.SENDTO_NUMBER, chatID);
                serviceIntent.putExtra(ReminderConstants.SENDFROM_NUMBER, sharedPreferenceSingleton.getSavedString(context, ConstantVar.CONTACT_SELF_NUMBER));
                serviceIntent.putIntegerArrayListExtra(ConstantVar.CARD_IDS_DELETION, (ArrayList<Integer>) cardIDs);
                break;
            case ConstantVar.DELETECONTACT:
                serviceIntent.putExtra(ReminderConstants.SENDFROM_NUMBER, sharedPreferenceSingleton.getSavedString(context, ConstantVar.CONTACT_SELF_NUMBER));
                serviceIntent.putExtra(ReminderConstants.SENDTO_NUMBER, chatID);
                break;
        }
        context.startService(serviceIntent);
    }

    void insertReminder() {
        appDatabase.cardDoa().insertCard(chatCardsEntity);//insert card
        updateContactChatCard();
    }

    void updateReminder() {
        appDatabase.cardDoa().updateCard(chatCardsEntity);//update card
        updateContactChatCard();
    }

    void deleteReminder() {
        appDatabase.cardDoa().deleteCard(cardIDs);
        int reminderContactCount = reminderChatCardCount(chatID);
        updateContactCard(chatID, name, reminderContactCount, false, true, System.currentTimeMillis());
    }

    void insertContact() {

    }

    void updateContact() {

    }

    void deleteContact() {
        appDatabase.reminderContactDoa().deleteCard(chatID);
        appDatabase.cardDoa().deleteContactCards(chatID);

    }

    void updateContactChatCard() {
        //get reminder chat cards count
        int countReminderCard = reminderChatCardCount(chatCardsEntity.getContactFetch().getContact_number());
        if (countReminderCard >= 1) {
            //get reminder contact card count
            int reminderContactCount = reminderContactCardCount(chatCardsEntity.getContactFetch().getContact_number());
            if (reminderContactCount > 0)
                updateContactCard(chatCardsEntity.getContactFetch().getContact_number(), chatCardsEntity.getContactFetch().getContact_name(), countReminderCard, false, true, System.currentTimeMillis());

            else
                insertContactCard(chatCardsEntity.getContactFetch().getContact_number(), chatCardsEntity.getContactFetch().getContact_name(), countReminderCard, false, true, System.currentTimeMillis());

        }
    }

    public int reminderChatCardCount(String number) {
        return appDatabase.cardDoa().getContactCardCount(number);
    }

    public int reminderContactCardCount(String number) {

        return appDatabase.reminderContactDoa().chatCardPresent(number);
    }

    void insertContactCard(String number, String name, int count, boolean selection, boolean visibility, long time) {
        setSharedValues(false, true, number);                //insert contact card
        appDatabase.reminderContactDoa().inserChatCard(new ReminderContact(number, name, count, selection, visibility, time));
    }

    void updateContactCard(String number, String name, int count, boolean selection, boolean visibility, long time) {
        setSharedValues(true, false, number);
        appDatabase.reminderContactDoa().updateChatCard(new ReminderContact(number, name, count, selection, visibility, time));
    }


    public void setSharedValues(boolean val1, boolean val2, String number) {
        sharedPreferenceSingleton.saveAs(context, ConstantVar.UPDATION, val1);
        sharedPreferenceSingleton.saveAs(context, ConstantVar.INSERTION, val2);
        sharedPreferenceSingleton.saveAs(context, ConstantVar.UPDATED_NUMBER, number);

    }
}
