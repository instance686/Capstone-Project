package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 31/12/17.
 */

public class ContactsFragmentViewModel extends ViewModel {

    private LiveData<List<Contact_Entity>> contactList;
    public LiveData<List<Contact_Entity>> getContactList(AppDatabase appDatabase) {
        contactList = appDatabase.contactDao().getAll();
        return contactList;
    }

    public ArrayList<Contact_Entity> getContacts() {
        return (ArrayList<Contact_Entity>) contactList.getValue();
    }

}
