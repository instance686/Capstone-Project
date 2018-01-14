package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ReminderContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 12/1/18.
 */

public class ReminderChatViewModel extends ViewModel {
    private LiveData<List<ReminderContact>> contactList;
    public LiveData<List<ReminderContact>> getContactList(AppDatabase appDatabase) {
        contactList = appDatabase.reminderContactDoa().getCurrentChats();
        return contactList;
    }

    public ArrayList<ReminderContact> getContacts() {
        return (ArrayList<ReminderContact>) contactList.getValue();
    }

}
