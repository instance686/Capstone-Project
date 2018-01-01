package com.reminders.location.locatoinreminder.executor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;
import com.reminders.location.locatoinreminder.pojo.ContactFetch;

import java.util.ArrayList;

/**
 * Created by ayush on 31/12/17.
 */

public class ContactSync extends AsyncTask<Void,Void,Void>{
    private AppDatabase appDatabase;
    private ContentResolver contentResolver;
    private ArrayList<Contact_Entity> appContacts;
    private ArrayList<ContactFetch> allContactsList;


    public ContactSync(ContentResolver contentResolver,AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.contentResolver = contentResolver;
        appContacts=new ArrayList<>();
        allContactsList=new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getContacts();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        for(ContactFetch contactFetch:allContactsList){
            Log.v("Contact",contactFetch.getContact_name()+" "+contactFetch.getContact_number());
        }


    }

    private void getContacts(){
        Uri CONTENT_URI= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String sortOrder=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC";
        final String[] PROJECTION={
          ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        ArrayList<String> numberList = new ArrayList<>();
        Cursor data = contentResolver.query(CONTENT_URI, PROJECTION, null, null, sortOrder);
        while (data.moveToNext()){
            String name=data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
            String number=data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            allContactsList.add(new ContactFetch(name,number));
            numberList.add(number);
        }
        allContactsList.add(new ContactFetch("Self","Number"));
        numberList.add("Number");


        data.close();
        appDatabase.contactDao().clearContacts();


        if (allContactsList.size() != 0)
            getAppContacts(numberList);

    }

    private void getAppContacts(ArrayList<String> numberList){
        for(ContactFetch contactFetch:allContactsList){
            if(contactFetch.getContact_name().equalsIgnoreCase("Self")){
            appContacts.add(new Contact_Entity(contactFetch.getContact_number(),contactFetch.getContact_name(),false));
            }
            appDatabase.contactDao().insertFeed(appContacts);
        }

    }
    }

