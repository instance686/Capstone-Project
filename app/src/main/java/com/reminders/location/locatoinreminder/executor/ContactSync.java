package com.reminders.location.locatoinreminder.executor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reminders.location.locatoinreminder.constants.ConstantVar;
import com.reminders.location.locatoinreminder.database.AppDatabase;
import com.reminders.location.locatoinreminder.database.entity.Contact_Entity;
import com.reminders.location.locatoinreminder.pojo.ContactFetch;
import com.reminders.location.locatoinreminder.pojo.User;
import com.reminders.location.locatoinreminder.singleton.SharedPreferenceSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ayush on 31/12/17.
 */

public class ContactSync extends AsyncTask<Void,Void,Void>{
    private Context context;
    private AppDatabase appDatabase;
    private ContentResolver contentResolver;
    private ArrayList<Contact_Entity> appContacts;
    String remoteContacts;
    TaskRunning taskRunning;
    private HashMap<String,ContactFetch> allContactsList;
    private DatabaseReference databaseReference;
    private SharedPreferenceSingleton sharedPreferenceSingleton = new SharedPreferenceSingleton();



    public ContactSync(Context context,ContentResolver contentResolver,AppDatabase appDatabase,TaskRunning taskRunning) {
        this.appDatabase = appDatabase;
        this.context=context;
        this.taskRunning=  taskRunning;
        this.contentResolver = contentResolver;
        appContacts=new ArrayList<>();
        remoteContacts="";
        allContactsList=new HashMap<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
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
            if(number.length()>=10) {
                String key=number.replaceAll(" ","");
                allContactsList.put(key, new ContactFetch(name, key));
                numberList.add(key);
                Log.v("Numbers",""+name+" "+key);

            }
        }
        data.close();
        if (allContactsList.size() != 0)
            getAppContacts(numberList);

    }

    private void getAppContacts(ArrayList<String> numberList){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post:dataSnapshot.getChildren()){
                    User user=post.getValue(User.class);
                        Log.v("Remote", user.getPhone());
                        remoteContacts+=""+user.getPhone()+"//s";
                }
                if(ContactSync.this.getStatus()==Status.RUNNING){

                }else {
                    appContacts= (ArrayList<Contact_Entity>) syncContacts(remoteContacts,numberList);
                    AsyncTask.execute(() ->
                    {   appDatabase.contactDao().clearContacts();
                        appDatabase.contactDao().insertFeed(appContacts);
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public List<Contact_Entity>  syncContacts(String remoteContacts,ArrayList<String> numberList){

        Contact_Entity ce=new Contact_Entity(sharedPreferenceSingleton.getSavedString(context, ConstantVar.CONTACT_SELF_NUMBER),
                sharedPreferenceSingleton.getSavedString(context,ConstantVar.CONSTANT_SELF_NAME),false);
        appContacts.add(ce);
        Log.v("RemoteContacts", remoteContacts);
        String appCon="";
        for(String number:numberList){
            if(remoteContacts.contains(number+"//s")){
                Log.v("ContainsResult",number);
                if(!appCon.contains(number+"//s"))
                {
                ContactFetch cf=allContactsList.get(number);
                appContacts.add(new Contact_Entity(cf.getContact_number(),cf.getContact_name(),false));
                }
                appCon+=number+"//s";
            }
        }
        return appContacts;
    }
    }

