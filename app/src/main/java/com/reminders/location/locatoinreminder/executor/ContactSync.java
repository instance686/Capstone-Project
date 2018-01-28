package com.reminders.location.locatoinreminder.executor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.ContactEntity;
import com.reminders.location.locatoinreminder.pojo.ContactFetch;
import com.reminders.location.locatoinreminder.pojo.User;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ayush on 31/12/17.
 */

public class ContactSync extends AsyncTask<Void, Void, Void> {
    String remoteContacts;
    TaskRunning taskRunning;
    private Context context;
    private AppDatabase appDatabase;
    private ContentResolver contentResolver;
    private ArrayList<ContactEntity> appContacts;
    private HashMap<String, ContactFetch> allContactsList;
    private DatabaseReference databaseReference;
    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();


    public ContactSync(Context context, ContentResolver contentResolver, AppDatabase appDatabase, TaskRunning taskRunning) {
        this.appDatabase = appDatabase;
        this.context = context;
        this.taskRunning = taskRunning;
        this.contentResolver = contentResolver;
        appContacts = new ArrayList<>();
        remoteContacts = "";
        allContactsList = new HashMap<>();
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantVar.USERS);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        taskRunning.taskRunning(true);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getContacts();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        taskRunning.taskRunning(false);

    }

    private void getContacts() {
        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY +" "+ConstantVar.COLLATE_LOCALIZED_ASC;
        final String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        ArrayList<String> numberList = new ArrayList<>();
        Cursor data = contentResolver.query(CONTENT_URI, PROJECTION, null, null, sortOrder);

        while (data.moveToNext()) {
            String name = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
            String number = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (number.length() >= 10) {
                String key=number.replaceAll(" ", "");
                if(number.contains(" ")) {
                    key = number.replaceAll(" ", "");
                }
                else if(number.contains("-")){
                    key = number.replaceAll("-", "");

                }
                if(!sharedPreferenceSingleton.getSavedString(context,ConstantVar.CONTACT_SELF_NUMBER).contains(key)
                        || !key.contains(sharedPreferenceSingleton.getSavedString(context,ConstantVar.CONTACT_SELF_NUMBER))){
                    allContactsList.put(key, new ContactFetch(name, key));
                    numberList.add(key);
                }

            }
        }
        data.close();
        if (allContactsList.size() != 0)
            getAppContacts(numberList);

    }

    private void getAppContacts(ArrayList<String> numberList) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    User user = post.getValue(User.class);
                    remoteContacts += "" + user.getPhone() + "//s";
                }
                if (ContactSync.this.getStatus() == Status.RUNNING) {

                } else {
                    appContacts = (ArrayList<ContactEntity>) syncContacts(remoteContacts, numberList);
                    AsyncTask.execute(() ->
                    {
                        appDatabase.contactDao().clearContacts();
                        appDatabase.contactDao().insertFeed(appContacts);
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public List<ContactEntity> syncContacts(String remoteContacts, ArrayList<String> numberList) {

        ContactEntity ce = new ContactEntity(sharedPreferenceSingleton.getSavedString(context, ConstantVar.CONTACT_SELF_NUMBER),
                sharedPreferenceSingleton.getSavedString(context, ConstantVar.CONSTANT_SELF_NAME), false);
        appContacts.add(ce);
        String appCon = "";
        for (String number : numberList) {
            if (remoteContacts.contains(number + "//s")) {
                if (!appCon.contains(number + "//s")) {
                    ContactFetch cf = allContactsList.get(number);
                    appContacts.add(new ContactEntity(cf.getContact_number(), cf.getContact_name(), false));
                }
                appCon += number + "//s";
            }
        }
        return appContacts;
    }
}

