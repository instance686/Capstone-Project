package com.reminders.location.locatoinreminder.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ContactEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 31/12/17.
 */

public class ContactsActivityViewModel extends ViewModel {

    private LiveData<List<ContactEntity>> contactList;

    public LiveData<List<ContactEntity>> getContactList(AppDatabase appDatabase) {
        contactList = appDatabase.contactDao().getAll();
        return contactList;
    }

    public ArrayList<ContactEntity> getContacts() {
        return (ArrayList<ContactEntity>) contactList.getValue();
    }

}
